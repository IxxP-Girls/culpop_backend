package com.ixxp.culpop.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostResponse {
    private int postId;
    private String username;
    private String title;
    private String cateName;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public PostResponse(int postId, String username, String title, String cateName, LocalDateTime createdAt) {
        this.postId = postId;
        this.username = username;
        this.title = title;
        this.cateName = cateName;
        this.createdAt = createdAt;
    }
}
