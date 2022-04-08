package com.playtomic.tests.wallet.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ErrorModel {
    String errorCode;
    String errorMessage;
}
