package com.playtomic.tests.wallet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.playtomic.tests.wallet.api.model.DepositByCreditCardModel;
import com.playtomic.tests.wallet.exception.ErrorModel;
import com.playtomic.tests.wallet.service.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@ActiveProfiles(profiles = "test")
@AutoConfigureMockMvc
@Slf4j
public class WalletApplicationIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    StripeService stripeService;

    @Autowired
    public ObjectMapper mapper;

    private final Long NOT_EXISTING_WALLET = 99l;

    private final Long WALLET1 = 101l;
    private final Long WALLET2 = 102l;
    private final Long WALLET3 = 103l;
    private final Long WALLET4 = 103l;
    private final Long WALLET5 = 103l;

    private final BigDecimal minAmount = new BigDecimal(15);
    private final BigDecimal tooLowAmount = new BigDecimal(5);
    private final BigDecimal validAmount = new BigDecimal(100);
    private final String creditCard = "mock-card";

    private final String paymentId = "mock-payment-id";

    //our goal is not to test stripe services here. so we need to mock them

    public void configureStripeServiceMock() {
        log.info("configuring Stripe Service xx Mock");
        doAnswer(invocation -> {
                    if (((BigDecimal) invocation.getArgument(1)).compareTo(minAmount) < 0)
                        throw new StripeAmountTooSmallException();
                    else return new Payment(paymentId);
                }
        ).when(stripeService).charge(any(), any());

    }


    @Test
    public void test_getWalletById_wallet_not_exists() throws Exception {

        var action = mockMvc.perform(get("/api/v1/wallet/" + NOT_EXISTING_WALLET)
                .contentType(MediaType.APPLICATION_JSON));

        var respBytes =
                action.andExpect(status().is5xxServerError())
                        .andReturn()
                        .getResponse().getContentAsByteArray();

        var error = mapper.readValue(respBytes, ErrorModel.class);

        Assert.assertEquals("Unexpected error code", error.getErrorCode(), BusinessExceptionCodeEnum.ENTITY_NOT_FOUND.name());
    }

    @Test
    public void test_getWalletById_wallet_exists() throws Exception {
        var wallet = getWalletById(WALLET1);
        Assert.assertNotNull("wallet should exist",wallet);
    }


    @Test
    public void test_deposit_money_success() throws Exception {

        configureStripeServiceMock();

        var wallet2 = getWalletById(WALLET2);
        var balance = wallet2.getCurrentBalance();
        var validAmount = this.validAmount;
        var bytes = depositMoneyAction(wallet2.getId(), new DepositByCreditCardModel(validAmount, creditCard))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();


        var historyRecord = this.mapper.readValue(bytes, WalletBalanceHistory.class);
        Assert.assertEquals("invalid wallet id", historyRecord.getWalletId(), wallet2.getId());
        Assert.assertEquals("invalid old balance", balance, historyRecord.getOldBalance());
        Assert.assertEquals("invalid new balance", balance.add(validAmount), historyRecord.getNewBalance());
        Assert.assertEquals("invalid change reason", WalletBalanceChangeReasonEnum.DEPOSIT, historyRecord.getChangeReason());
        Assert.assertEquals("invalid payment id", paymentId, historyRecord.getRelatedPaymentId());

    }

    @Test
    public void test_deposit_money_too_low() throws Exception {

        configureStripeServiceMock();
        var wallet2 = getWalletById(WALLET2);
        var balance = wallet2.getCurrentBalance();

        var bytes = depositMoneyAction(wallet2.getId(), new DepositByCreditCardModel(tooLowAmount, creditCard))
                .andExpect(status().isInternalServerError())
                .andReturn()
                .getResponse()
                .getContentAsByteArray();

        var error = mapper.readValue(bytes, ErrorModel.class);

        Assert.assertEquals("Error code invalid", error.getErrorCode(), BusinessExceptionCodeEnum.PAYMENT_SERVICE_ERROR.name());
        Assert.assertEquals("Error message invalid", error.getErrorMessage(), StripeAmountTooSmallException.class.getSimpleName());


        var wallet2New = getWalletById(WALLET2);

        //be sure balance not changed
        Assert.assertEquals("Balance should not change", wallet2.getCurrentBalance(), wallet2New.getCurrentBalance());
    }

    @Test
    public void test_balance_changes() throws Exception {
        configureStripeServiceMock();

        var wallet = getWalletById(WALLET3);

        var balance = getWalletBalance(WALLET3);

        Assert.assertEquals("balance is not equal to wallet balance", balance, wallet.getCurrentBalance());


        depositMoneyAction(WALLET3, new DepositByCreditCardModel(validAmount, creditCard));

        wallet = getWalletById(WALLET3);

        Assert.assertEquals("balance is not equal", balance.add(validAmount), wallet.getCurrentBalance() );

        balance = getWalletBalance(WALLET3);

        Assert.assertEquals("balance is not equal", balance, wallet.getCurrentBalance() );

    }


    private Wallet getWalletById(long id) throws Exception {

        var action = mockMvc.perform(get("/api/v1/wallet/" + id)
                .contentType(MediaType.APPLICATION_JSON));

        var respBytes =
                action.andExpect(status().isOk())
                        .andReturn()
                        .getResponse().getContentAsByteArray();

        return mapper.readValue(respBytes, Wallet.class);
    }

    private BigDecimal getWalletBalance(long id) throws Exception {

        var action = mockMvc.perform(get("/api/v1/wallet/" + id+"/balance")
                .contentType(MediaType.APPLICATION_JSON));

        var respBytes =
                action.andExpect(status().isOk())
                        .andReturn()
                        .getResponse().getContentAsByteArray();

        return mapper.readValue(respBytes, BigDecimal.class);
    }


    private ResultActions depositMoneyAction(long walletId, DepositByCreditCardModel model) throws Exception {
        var action = mockMvc.perform(post("/api/v1/wallet/" + walletId + "/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.mapper.writeValueAsString(
                        model)));
        return action;
    }
}
