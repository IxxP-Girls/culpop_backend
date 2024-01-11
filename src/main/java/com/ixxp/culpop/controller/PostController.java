package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.post.PostDetailResponse;
import com.ixxp.culpop.dto.post.PostRequest;
import com.ixxp.culpop.dto.post.PostResponse;
import com.ixxp.culpop.security.UserDetailsImpl;
import com.ixxp.culpop.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    // 게시글 전체 조회
    @GetMapping()
    public ResponseEntity<List<PostResponse>> getPost(@RequestParam(name = "category", defaultValue = "all") String category
                                                    , @RequestParam(name = "page", defaultValue = "1") int page) {
        return ResponseEntity.ok(postService.getPost(category, page));
    }

    // 게시글 개별 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> getPostDetail(@PathVariable int postId) {
        return ResponseEntity.ok(postService.getPostDetail(postId));
    }

    // 게시글 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<StatusResponse> updatePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable int postId,
                                                     @RequestBody PostRequest postRequest) {
        postService.updatePost(userDetails.getUser(), postId, postRequest);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "post 수정 완료");
        return ResponseEntity.ok(statusResponse);
    }

    // 게시글 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<StatusResponse> deletePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable int postId) {
        postService.deletePopup(userDetails.getUser(), postId);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "post 삭제 완료");
        return ResponseEntity.ok(statusResponse);
    }
}
