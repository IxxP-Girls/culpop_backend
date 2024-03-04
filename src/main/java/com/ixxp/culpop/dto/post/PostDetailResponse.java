package com.ixxp.culpop.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PostDetailResponse {
    private int postId;
    private String username;
    private String title;
    private String content;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedAt;
    private String cateName;
    private boolean likeCheck;
    private int likeCount;
    private int viewCount;
    private int commentCount;
}
