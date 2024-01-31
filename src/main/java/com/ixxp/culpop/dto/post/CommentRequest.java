package com.ixxp.culpop.dto.post;

import lombok.Getter;
import org.springframework.lang.Nullable;

@Getter
public class CommentRequest {
    private String content;
    private boolean secret;
    private int parentId;
}
