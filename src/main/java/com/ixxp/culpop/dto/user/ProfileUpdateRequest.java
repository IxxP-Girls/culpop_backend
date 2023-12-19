package com.ixxp.culpop.dto.user;

import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileUpdateRequest {
    // username : 영문&숫자&한글 조합 2~8글자
    @Pattern(regexp = "^[a-zA-Z0-9가-힣]{2,8}$", message = "올바른 형식으로 입력해 주세요")
    private String username;
}
