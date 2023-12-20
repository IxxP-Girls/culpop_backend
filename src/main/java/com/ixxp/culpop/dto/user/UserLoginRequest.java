package com.ixxp.culpop.dto.user;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserLoginRequest {
    @Email
    private String email;
    @NotBlank
    private String pwd;
}
