package com.playtomic.tests.wallet.validator;

import com.playtomic.tests.wallet.dto.RequestDto;
import com.playtomic.tests.wallet.dto.ChargeRequestDto;
import com.playtomic.tests.wallet.enums.Currency;
import com.playtomic.tests.wallet.exception.WalletResponseException;
import lombok.SneakyThrows;
import org.apache.commons.validator.routines.CreditCardValidator;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ChargeMoneyWalletRequestValidator implements RequestValidator {

    private final CreditCardValidator creditCardValidator = new CreditCardValidator();

    @SneakyThrows
    @Override
    public void validate(RequestDto requestDto) {
        ChargeRequestDto dto = (ChargeRequestDto) requestDto;
        if (!(dto.getAmount().compareTo(BigDecimal.ZERO) > 0)) {
            throw new WalletResponseException("Invalid amount");
        }
        List<String> currencyList =
                Arrays.stream(Currency.values()).map(Enum::toString).filter(v -> v.equalsIgnoreCase(dto.getCurrency())).collect(Collectors.toList());
        if (!(currencyList.size() > 0)) {
            throw new WalletResponseException("Invalid currency");
        }
        if (!creditCardValidator.isValid(dto.getCreditCardNumber())) {
            throw new WalletResponseException("Invalid credit card");
        }
    }
}
