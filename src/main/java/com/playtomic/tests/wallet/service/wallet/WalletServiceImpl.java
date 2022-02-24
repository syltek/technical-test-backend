package com.playtomic.tests.wallet.service.wallet;

import com.playtomic.tests.wallet.cache.EmbeddedRedis;
import com.playtomic.tests.wallet.dto.ChargeRequestDto;
import com.playtomic.tests.wallet.dto.StripePaymentDto;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.dto.PaymentDto;
import com.playtomic.tests.wallet.entity.Payment;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.WalletResponseException;
import com.playtomic.tests.wallet.repository.PaymentRepository;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.payment.stripe.StripeService;
import com.playtomic.tests.wallet.validator.ChargeMoneyWalletRequestValidator;
import com.playtomic.tests.wallet.validator.StripePaymentResponseValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Service
@RequiredArgsConstructor
public class WalletServiceImpl implements WalletService {

    private static final Log log = LogFactory.getLog(WalletServiceImpl.class);

    private final WalletRepository walletRepository;
    private final PaymentRepository paymentRepository;
    private final ModelMapper modelMapper;
    private final StripeService stripeService;
    private final ChargeMoneyWalletRequestValidator chargeMoneyWalletRequestValidator;
    private final StripePaymentResponseValidator stripePaymentResponseValidator;
    private final EmbeddedRedis embeddedRedis;

    @Override
    public WalletDto getWallet(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);

        if (wallet.isPresent()) {
            log.info("Wallet is available with id: " + id);
            return getWalletDtoWithPayments(wallet.get());
        } else {
            throw new WalletResponseException("Wallet could not be found");
        }
    }

    @Override
    public PaymentDto getPayment(String id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            log.info("Payment is available with id: " + id);
            return modelMapper.map(payment.get(), PaymentDto.class);
        } else {
            throw new WalletResponseException("Payment could not be found");
        }
    }

    @Override
    public StripePaymentDto chargeMoneyWalletByCreditCard(Long id, ChargeRequestDto body) {
        WalletDto walletDto = getWallet(id);
        chargeMoneyWalletRequestValidator.validate(body, walletDto);

        String value = embeddedRedis.getRedisLock().tryLock(String.valueOf(id), String.valueOf(walletDto.getVersion()));
        if (Objects.isNull(value)) {
            throw new WalletResponseException("The process is going on wallet. Please try later.");
        }

        StripePaymentDto stripePaymentDto = stripeService.charge(body.getCreditCardNumber(), body.getAmount());
        stripePaymentResponseValidator.validate(body, stripePaymentDto);
        log.info("Payment is charged successfully. Amount is " + stripePaymentDto.getAmount() + " in " + body.getCurrency());

        mapAndSaveForChargeOperation(walletDto, body, stripePaymentDto);

        boolean isReleased = embeddedRedis.getRedisLock().unLock(String.valueOf(id),
                String.valueOf(walletDto.getVersion()));
        if (!isReleased) {
            throw new WalletResponseException("Wallet's lock could not be released");
        }

        return stripePaymentDto;
    }

    @Override
    public WalletDto chargeMoneyBackWalletByCreditCard(String paymentId) {
        PaymentDto paymentDto = getPayment(paymentId);
        WalletDto walletDto = getWallet(paymentDto.getWalletId());

        String value = embeddedRedis.getRedisLock().tryLock(String.valueOf(walletDto.getId()),
                String.valueOf(walletDto.getVersion()));
        if (Objects.isNull(value)) {
            throw new WalletResponseException("The process is going on wallet. Please try later.");
        }
        stripeService.refund(String.valueOf(paymentId));
        log.info("Payment is charged back successfully for id: " + paymentId);

        Wallet wallet = mapAndSaveForChargeBackOperation(walletDto, paymentDto);

        boolean isReleased = embeddedRedis.getRedisLock().unLock(String.valueOf(walletDto.getId()),
                String.valueOf(walletDto.getVersion()));
        if (!isReleased) {
            throw new WalletResponseException("Wallet's lock could not be released");
        }

        return modelMapper.map(wallet, WalletDto.class);
    }

    private void mapAndSaveForChargeOperation(WalletDto walletDto, ChargeRequestDto body, StripePaymentDto stripePaymentDto){
        Payment payment = Payment.builder().id(stripePaymentDto.getId()).walletId(walletDto.getId()).amount(body.getAmount()).build();
        paymentRepository.save(payment);
        log.info("Payment is created for walletId: " + payment.getWalletId() + " paymentId: " + payment.getId());

        Wallet wallet = modelMapper.map(walletDto, Wallet.class);
        wallet.setBalance(walletDto.getBalance().add(body.getAmount()));
        wallet.setUpdatedAt(new Date());
        walletRepository.save(wallet);
        log.info("Wallet is charged for id: " + wallet.getId());
    }

    private Wallet mapAndSaveForChargeBackOperation(WalletDto walletDto, PaymentDto paymentDto){
        Wallet wallet = modelMapper.map(walletDto, Wallet.class);
        wallet.setBalance(walletDto.getBalance().subtract(paymentDto.getAmount()));
        wallet.setUpdatedAt(new Date());
        walletRepository.save(wallet);
        log.info("Wallet is charged back for id: " + wallet.getId());

        Payment payment = modelMapper.map(paymentDto, Payment.class);
        payment.setRefunded(true);
        paymentRepository.save(payment);
        log.info("Payment with id " + paymentDto.getId() + "is refunded for walletId: " + payment.getWalletId());
        return wallet;
    }

    private WalletDto getWalletDtoWithPayments(Wallet wallet){
        WalletDto walletDto = modelMapper.map(wallet, WalletDto.class);
        List<Payment> paymentList =  paymentRepository.findByWalletId(wallet.getId());
        if(Objects.nonNull(paymentList) && paymentList.size()>0){
            walletDto.setPaymentIds(paymentList.stream().map(Payment::getId).collect(Collectors.toList()));
        }
        return walletDto;
    }
}
