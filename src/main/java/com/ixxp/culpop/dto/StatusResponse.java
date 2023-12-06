package com.ixxp.culpop.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StatusResponse {
    private int statusCode;
    private String statusMessage;
}