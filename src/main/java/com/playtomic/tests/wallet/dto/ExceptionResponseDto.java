package com.playtomic.tests.wallet.dto;

import lombok.Data;
import lombok.NonNull;

import java.util.Date;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Data
public class ExceptionResponseDto {

    @NonNull
    private int code;
    @NonNull
    private String status;
    private String message;
    @NonNull
    private Date timestamp;

    public ExceptionResponseDto(String status, int code, String message) {
        this.timestamp = new Date();
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
