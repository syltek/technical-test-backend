package com.playtomic.tests.wallet.service;

import lombok.Getter;

@Getter
public enum BusinessExceptionCodeEnum {

    ENTITY_NOT_FOUND("Entity not found"),
    PAYMENT_SERVICE_ERROR("An error occured on payment service");

    private String description;

    BusinessExceptionCodeEnum(String description) {
        this.description = description;
    }
}
