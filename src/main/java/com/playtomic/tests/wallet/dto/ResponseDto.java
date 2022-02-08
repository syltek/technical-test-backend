package com.playtomic.tests.wallet.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Data
@Builder
@AllArgsConstructor
public class ResponseDto {
    int code;
    String message;
}
