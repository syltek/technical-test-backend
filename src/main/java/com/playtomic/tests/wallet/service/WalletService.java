package com.playtomic.tests.wallet.service;

import com.playtomic.tests.wallet.exception.BusinessException;
import com.playtomic.tests.wallet.repository.WalletBalanceHistoryRepository;
import com.playtomic.tests.wallet.repository.WalletRepository;
import com.playtomic.tests.wallet.service.dto.DepositByCreditCardRequest;
import com.playtomic.tests.wallet.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class WalletService {


    @Autowired
    WalletRepository walletRepository;

    @Autowired
    WalletBalanceHistoryRepository balanceHistoryRepository;

    @Autowired
    StripeService stripeService;

    public Wallet fetchOneById(long id) {

        return EntityUtil.getOrThrowEx(walletRepository.findById(id));
    }


    @Transactional
    public WalletBalanceHistory deposit(DepositByCreditCardRequest request) {

        var wallet = EntityUtil.getOrThrowEx(walletRepository.findById(request.getWalletId()));

        try {

            //call payment service
            var payment = stripeService.charge(request.getCreditCard(), request.getAmount());

            var currentBalance = wallet.getCurrentBalance().add(request.getAmount());

            //create new history record
            var history = new WalletBalanceHistory();
            history.setWalletId(wallet.getId());
            history.setOldBalance(wallet.getCurrentBalance());
            history.setNewBalance(currentBalance);
            history.setChangeReason(WalletBalanceChangeReasonEnum.DEPOSIT);
            history.setRelatedPaymentId(payment.getId());
            history.setChangeNote("DEPOSIT_BY_CREDIT_CARD: payment_id" + payment.getId());
            balanceHistoryRepository.save(history);

            //update wallet balance
            wallet.setCurrentBalance(currentBalance);
            walletRepository.save(wallet);

            return history;

        } catch (StripeServiceException ex) {
            throw new BusinessException(BusinessExceptionCodeEnum.PAYMENT_SERVICE_ERROR,
                    Optional.ofNullable(ex.getMessage()).orElse(ex.getClass().getSimpleName())
            );
        }

    }

    public BigDecimal fetchCurrentBalanceById(long id) {
        var wallet = EntityUtil.getOrThrowEx(walletRepository.findById(id));
        return wallet.getCurrentBalance();
    }
}
