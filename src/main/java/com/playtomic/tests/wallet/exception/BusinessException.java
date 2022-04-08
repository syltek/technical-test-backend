package com.playtomic.tests.wallet.exception;

import com.playtomic.tests.wallet.service.BusinessExceptionCodeEnum;
import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {

    private final BusinessExceptionCodeEnum code;

    public BusinessException(BusinessExceptionCodeEnum code) {
        super(code.getDescription());
        this.code = code;
    }

    public BusinessException(BusinessExceptionCodeEnum code, String message) {
        super(message);
        this.code = code;
    }
}
