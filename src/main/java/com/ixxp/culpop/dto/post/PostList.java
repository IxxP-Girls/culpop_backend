package com.ixxp.culpop.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostList {
    private int postId;
    private String username;
    private String title;
    private String cateName;
    private int viewCount;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;

    public PostList(int postId, String username, String title, String cateName, int viewCount, LocalDateTime createdAt) {
        this.postId = postId;
        this.username = username;
        this.title = title;
        this.cateName = cateName;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
    }
}
