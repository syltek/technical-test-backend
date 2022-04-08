package com.playtomic.tests.wallet.exception;

import com.playtomic.tests.wallet.service.BusinessExceptionCodeEnum;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;

@ControllerAdvice
public class BusinessExceptionInterceptor extends ResponseEntityExceptionHandler {


    @ExceptionHandler(BusinessException.class)
    public final ResponseEntity<Object> handleAllExceptions(BusinessException ex) {

        return new ResponseEntity(new ErrorModel(ex.getCode().name(), ex.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

