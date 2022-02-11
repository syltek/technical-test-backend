package com.playtomic.tests.wallet.services.topup;

import com.playtomic.tests.wallet.domain.TopUp;
import com.playtomic.tests.wallet.domain.Wallet;
import com.playtomic.tests.wallet.repositories.TopUpRepository;
import com.playtomic.tests.wallet.services.stripe.StripeService;
import com.playtomic.tests.wallet.web.mappers.TopUpMapper;
import com.playtomic.tests.wallet.web.model.StripeChargeResponse;
import com.playtomic.tests.wallet.web.model.TopUpDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopUpServiceImpl implements TopUpService {

    private final TopUpRepository topUpRepository;
    private final TopUpMapper topUpMapper;
    private final StripeService stripeService;


    @Override
    public void saveTopUp(TopUpDto topUpDto, Wallet wallet) {
        TopUp topUp = topUpMapper.topUpDtoToTopUp(topUpDto);
        topUp.setWallet(wallet);
        topUpRepository.saveAndFlush(topUp);
    }


    public void charge(TopUpDto topUpDto) {
        StripeChargeResponse charge = stripeService.charge(topUpDto.getCreditCardNumber(), topUpDto.getAmount());
        topUpDto.setPaymentId(charge.getPaymentId());
        topUpDto.setAmount(charge.getAmount());
    }

}
