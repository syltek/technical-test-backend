package com.playtomic.tests.wallet.service.impl;

import com.playtomic.tests.wallet.WalletApplication;
import com.playtomic.tests.wallet.cache.EmbeddedRedis;
import com.playtomic.tests.wallet.dto.ChargeRequestDto;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.enums.Currency;
import com.playtomic.tests.wallet.exception.WalletResponseException;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.payment.stripe.StripeAmountTooSmallException;
import com.playtomic.tests.wallet.service.wallet.WalletServiceImpl;
import lombok.SneakyThrows;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {WalletApplication.class})
@DataJpaTest
@ActiveProfiles("test")
public class WalletServiceTest {

    private static final String TEST_CREDIT_CARD = "4242424242424242";
    private static final String TEST_INVALID_CREDIT_CARD = "424242424242424";
    private static final int TIMEOUT = 20 * 1000;

    @Autowired
    private WalletRepository walletRepository;
    @Autowired
    private WalletServiceImpl walletService;
    @Autowired
    private EmbeddedRedis embeddedRedis;

    @BeforeEach
    public void setup() {
        List<Wallet> walletList =
                Arrays.asList(Wallet.builder().currency(Currency.USD).balance(BigDecimal.valueOf(100)).iban("ES2604879457091371855657").build(),
                        Wallet.builder().currency(Currency.USD).balance(BigDecimal.valueOf(100)).iban("ES0521005134909988165288").build(),
                        Wallet.builder().currency(Currency.EUR).balance(BigDecimal.valueOf(200)).iban("ES5420955393601824236215").build(),
                        Wallet.builder().currency(Currency.EUR).balance(BigDecimal.valueOf(200.2)).iban("ES6830047695641298588925").build());
        walletRepository.saveAll(walletList);
    }

    @Test
    public void test_getWalletByIdForExistingWallet() {
        WalletDto wallet = walletService.getWallet(1L);
        assertThat(wallet).isNotNull();
        assertThat(wallet.getId()).isEqualTo(1L);
        assertThat(wallet.getCurrency()).isEqualTo(Currency.USD);
        assertThat(wallet.getBalance()).isEqualTo("100.00");
        assertThat(wallet.getIban()).isEqualTo("ES2604879457091371855657");
    }

    @Test
    public void test_getWalletByIdForNotExistingWallet() {
        assertThatThrownBy(() -> {
            walletService.getWallet(0L);
        }).isInstanceOf(WalletResponseException.class)
                .hasMessageContaining("Wallet could not be found");
    }

    @Test
    public void test_chargeMoneyWalletByIdForValidRequestBody() {
        WalletDto walletDto = walletService.getWallet(1L);
        walletService.chargeMoneyWalletByCreditCard(1L,
                ChargeRequestDto.builder().currency(Currency.USD.name()).amount(new BigDecimal(10)).creditCardNumber(TEST_CREDIT_CARD).build());

        WalletDto updatedWalletDto = walletService.getWallet(1L);
        assertThat(updatedWalletDto.getBalance()).isEqualTo(walletDto.getBalance().add(BigDecimal.TEN));
        assertThat(updatedWalletDto.getUpdatedAt()).isNotEqualTo(walletDto.getUpdatedAt());
        assertThat(updatedWalletDto.getPaymentIds().size()).isEqualTo(walletDto.getPaymentIds().size()+1);
    }

    @Test
    public void test_chargeMoneyWalletByIdForInvalidCurrency() {
        assertThatThrownBy(() -> {
            walletService.chargeMoneyWalletByCreditCard(1L,
                    ChargeRequestDto.builder().currency(Currency.EUR.name()).amount(new BigDecimal(50)).creditCardNumber(TEST_CREDIT_CARD).build());
        }).isInstanceOf(WalletResponseException.class)
                .hasMessageContaining("Invalid currency");
    }

    @Test
    public void test_chargeMoneyWalletByIdForInvalidCreditCard() {
        assertThatThrownBy(() -> {
            walletService.chargeMoneyWalletByCreditCard(1L,
                    ChargeRequestDto.builder().currency(Currency.USD.name()).amount(new BigDecimal(50)).creditCardNumber(TEST_INVALID_CREDIT_CARD).build());
        }).isInstanceOf(WalletResponseException.class)
                .hasMessageContaining("Invalid credit card");
    }

    @Test
    public void test_chargeMoneyWalletByIdForTooSmallAmount() {
        assertThatThrownBy(() -> {
            walletService.chargeMoneyWalletByCreditCard(1L,
                    ChargeRequestDto.builder().currency(Currency.USD.name()).amount(new BigDecimal(1)).creditCardNumber(TEST_CREDIT_CARD).build());
        }).isInstanceOf(StripeAmountTooSmallException.class)
                .hasMessageContaining("Amount is too small to charge");
    }

    @SneakyThrows
    @Test
    public void test_chargeMoneyWalletByIdConcurrent() {

        embeddedRedis.getRedisLock().lock("2", "0", TIMEOUT);

        assertThatThrownBy(() -> {
            walletService.chargeMoneyWalletByCreditCard(2L,
                    ChargeRequestDto.builder().currency(Currency.USD.name()).amount(new BigDecimal(10)).creditCardNumber(TEST_CREDIT_CARD).build());
        }).isInstanceOf(WalletResponseException.class)
                .hasMessageContaining("The process is going on wallet. Please try later.");

    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public RestTemplateBuilder restTemplateBuilder() {
            return new RestTemplateBuilder();
        }
    }
}
