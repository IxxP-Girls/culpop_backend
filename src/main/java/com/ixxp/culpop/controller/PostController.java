package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.post.PostRequest;
import com.ixxp.culpop.security.UserDetailsImpl;
import com.ixxp.culpop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;

    // 게시글 등록
    @PostMapping()
    public ResponseEntity<StatusResponse> createPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestBody PostRequest postRequest) {
        postService.createPost(userDetails.getUser(), postRequest);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "post 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(statusResponse);
    }
}
