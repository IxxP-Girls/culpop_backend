package com.ixxp.culpop.dto.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class CommentResponse {
    private int commentId;
    private int parentId; // null은 최상위댓글, 하위댓글은 상위댓글의 commentId를 parentId로 가짐
    private String username;
    private String content;
    private boolean secret;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime createdAt;
    private int likeCount;
    private boolean likeCheck;
    private List<CommentResponse> children;
}
