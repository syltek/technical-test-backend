package com.playtomic.tests.wallet.validator;

import com.playtomic.tests.wallet.dto.ChargeRequestDto;
import com.playtomic.tests.wallet.dto.WalletDto;
import com.playtomic.tests.wallet.enums.Currency;
import com.playtomic.tests.wallet.exception.WalletResponseException;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChargeMoneyWalletRequestValidator implements RequestValidator<ChargeRequestDto, WalletDto> {

    private final CreditCardValidator creditCardValidator = new CreditCardValidator();

    @Override
    public void validate(ChargeRequestDto chargeRequestDto, WalletDto walletDto) {
        if (!(chargeRequestDto.getAmount().compareTo(BigDecimal.ZERO) > 0)) {
            throw new WalletResponseException("Invalid amount");
        }
        List<String> currencyList =
                Arrays.stream(Currency.values()).map(Enum::toString).filter(v -> v.equalsIgnoreCase(chargeRequestDto.getCurrency())).collect(Collectors.toList());
        if (!(currencyList.size() > 0) || !walletDto.getCurrency().name().equalsIgnoreCase((chargeRequestDto).getCurrency())) {
            throw new WalletResponseException("Invalid currency");
        }
        if (!creditCardValidator.isValid(chargeRequestDto.getCreditCardNumber())) {
            throw new WalletResponseException("Invalid credit card");
        }
    }
}
