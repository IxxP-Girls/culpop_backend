package com.ixxp.culpop.dto.user;

import com.ixxp.culpop.dto.post.PostList;
import com.ixxp.culpop.dto.post.PostResponse;
import com.ixxp.culpop.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class ProfileResponse {
    private int userId;
    private String username;
    private String email;
    private PostResponse postList;

    public ProfileResponse(User user, PostResponse postList) {
        this.userId = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.postList = postList;
    }
}
