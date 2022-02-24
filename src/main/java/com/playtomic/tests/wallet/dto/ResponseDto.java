package com.playtomic.tests.wallet.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Orkun Cavdar on 08/02/2022
 */
@Data
@Builder
@AllArgsConstructor
public class ResponseDto {
    int code;
    String message;
    Map<String,String> body;
}
