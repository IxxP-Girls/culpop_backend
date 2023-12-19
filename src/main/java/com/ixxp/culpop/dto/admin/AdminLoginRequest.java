package com.ixxp.culpop.dto.admin;

import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AdminLoginRequest {
    @NotBlank
    private String email;
    @NotBlank
    private String pwd;
}
