package com.ixxp.culpop.dto.user;

import com.ixxp.culpop.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileResponse {
    private int userId;
    private String username;
    private String email;

    public ProfileResponse(User user) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
    }
}
