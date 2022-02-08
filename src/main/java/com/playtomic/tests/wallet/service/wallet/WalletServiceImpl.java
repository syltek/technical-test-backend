package com.playtomic.tests.wallet.service.wallet;

import com.playtomic.tests.wallet.cache.EmbeddedRedis;
import com.playtomic.tests.wallet.dto.ChargeRequestDto;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.dto.PaymentDto;
import com.playtomic.tests.wallet.entity.Payment;
import com.playtomic.tests.wallet.entity.Wallet;
import com.playtomic.tests.wallet.exception.WalletResponseException;
import com.playtomic.tests.wallet.repository.PaymentRepository;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.payment.stripe.StripeService;
import com.playtomic.tests.wallet.validator.ChargeMoneyWalletRequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

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
    private final EmbeddedRedis embeddedRedis;

    @SneakyThrows
    @Override
    public WalletDto getWallet(Long id) {
        Optional<Wallet> wallet = walletRepository.findById(id);

        if (wallet.isPresent()) {
            log.info("Wallet is available with id: " + id);
            return modelMapper.map(wallet.get(), WalletDto.class);
        } else {
            throw new WalletResponseException("Wallet could not be found");
        }
    }

    @SneakyThrows
    @Override
    public PaymentDto getPayment(Long id) {
        Optional<Payment> payment = paymentRepository.findById(id);

        if (payment.isPresent()) {
            log.info("Payment is available with id: " + id);
            return modelMapper.map(payment.get(), PaymentDto.class);
        } else {
            throw new WalletResponseException("Payment could not be found");
        }
    }

    @SneakyThrows
    @Override
    public boolean chargeMoneyWalletByCreditCard(Long id, ChargeRequestDto body) {
        chargeMoneyWalletRequestValidator.validate(body);
        WalletDto walletDto = getWallet(id);

        String value = embeddedRedis.getRedisLock().tryLock(String.valueOf(id), String.valueOf(walletDto.getVersion()));
        if (Objects.isNull(value)) {
            throw new WalletResponseException("The process is going on wallet. Please try later.");
        }

        stripeService.charge(body.getCreditCardNumber(), body.getAmount());
        log.info("Payment is charged successfully. Amount is " + body.getAmount() + " in " + body.getCurrency());

        mapAndSaveForChargeOperation(walletDto, body);

        boolean isReleased = embeddedRedis.getRedisLock().unLock(String.valueOf(id),
                String.valueOf(walletDto.getVersion()));
        if (!isReleased) {
            throw new WalletResponseException("Wallet's lock could not be released");
        }

        return isReleased;
    }

    @SneakyThrows
    @Override
    public boolean chargeMoneyBackWalletByCreditCard(Long paymentId) {
        PaymentDto paymentDto = getPayment(paymentId);
        WalletDto walletDto = getWallet(paymentDto.getWalletId());

        String value = embeddedRedis.getRedisLock().tryLock(String.valueOf(walletDto.getId()),
                String.valueOf(walletDto.getVersion()));
        if (Objects.isNull(value)) {
            throw new WalletResponseException("The process is going on wallet. Please try later.");
        }
        stripeService.refund(String.valueOf(paymentId));
        log.info("Payment is charged back successfully for id: " + paymentId);

        mapAndSaveForChargeBackOperation(walletDto, paymentDto);

        boolean isReleased = embeddedRedis.getRedisLock().unLock(String.valueOf(walletDto.getId()),
                String.valueOf(walletDto.getVersion()));
        if (!isReleased) {
            throw new WalletResponseException("Wallet's lock could not be released");
        }

        return isReleased;
    }

    private void mapAndSaveForChargeOperation(WalletDto walletDto, ChargeRequestDto body){
        Wallet wallet = modelMapper.map(walletDto, Wallet.class);
        wallet.setBalance(walletDto.getBalance().add(body.getAmount()));
        wallet.setUpdatedAt(new Date());
        walletRepository.save(wallet);
        log.info("Wallet is charged for id: " + wallet.getId());

        Payment payment = Payment.builder().walletId(wallet.getId()).amount(body.getAmount()).build();
        paymentRepository.save(payment);
        log.info("Payment is created for walletId: " + payment.getWalletId() + " paymentId: " + payment.getId());
    }

    private void mapAndSaveForChargeBackOperation(WalletDto walletDto, PaymentDto paymentDto){
        Wallet wallet = modelMapper.map(walletDto, Wallet.class);
        wallet.setBalance(walletDto.getBalance().subtract(paymentDto.getAmount()));
        wallet.setUpdatedAt(new Date());
        walletRepository.save(wallet);
        log.info("Wallet is charged back for id: " + wallet.getId());

        Payment payment = modelMapper.map(paymentDto, Payment.class);
        payment.setRefunded(true);
        paymentRepository.save(payment);
        log.info("Payment is refunded for walletId: " + payment.getWalletId());
    }
}
