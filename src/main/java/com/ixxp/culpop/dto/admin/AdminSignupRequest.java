package com.ixxp.culpop.dto.admin;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminSignupRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String pwd;
}
