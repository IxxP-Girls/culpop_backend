package com.ixxp.culpop.dto.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class PostResponse {
    private List<PostList> data;
    private int total;

    public PostResponse(List<PostList> data, int total) {
        this.data = data;
        this.total = total;
    }
}
