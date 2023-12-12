package com.ixxp.culpop.dto;

import lombok.Getter;

@Getter
public class SecurityExceptionDto {
    private int statusCode;
    private String statusMessage;

    public SecurityExceptionDto(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }
}
