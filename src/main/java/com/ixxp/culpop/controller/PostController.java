package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.post.CommentRequest;
import com.ixxp.culpop.dto.post.PostDetailResponse;
import com.ixxp.culpop.dto.post.PostRequest;
import com.ixxp.culpop.dto.post.PostResponse;
import com.ixxp.culpop.security.UserDetailsImpl;
import com.ixxp.culpop.service.CommentService;
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
    private final CommentService commentService;

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

    // 게시글 좋아요
    @PostMapping("/{postId}/like")
    public ResponseEntity<StatusResponse> likePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                   @PathVariable int postId) {
        postService.likePost(userDetails.getUser(), postId);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "post 좋아요 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(statusResponse);
    }

    // 게시글 좋아요 취소
    @DeleteMapping("/{postId}/like")
    public ResponseEntity<StatusResponse> unlikePost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable int postId) {
        postService.unlikePost(userDetails.getUser(), postId);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "popup 좋아요 취소 완료");
        return ResponseEntity.ok(statusResponse);
    }

    // 게시글 검색
    @GetMapping("/search")
    public ResponseEntity<List<PostResponse>> getSearchPost(@RequestParam("word") String word,
                                                            @RequestParam(name = "page", defaultValue = "1") int page) {
        List<PostResponse> postResponses = postService.getSearchPost(word, page);
        return ResponseEntity.ok(postResponses);
    }

    // 댓글 등록
    @PostMapping("/{postId}/comments")
    public ResponseEntity<StatusResponse> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable int postId,
                                                        @RequestBody CommentRequest commentRequest) {
        commentService.createComment(userDetails.getUser(), postId, commentRequest);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "comment 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(statusResponse);
    }

    // 댓글 조회

    // 댓글 수정
    @PatchMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<StatusResponse> updateComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable int postId,
                                                        @PathVariable int commentId,
                                                        @RequestBody CommentRequest commentRequest) {
        commentService.updateComment(userDetails.getUser(), postId, commentId, commentRequest);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "comment 수정 완료");
        return ResponseEntity.ok(statusResponse);
    }

    // 댓글 삭제
    @DeleteMapping("/{postId}/comments/{commentId}")
    public ResponseEntity<StatusResponse> deleteComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @PathVariable int postId,
                                                        @PathVariable int commentId) {
        commentService.deleteComment(userDetails.getUser(), postId, commentId);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "comment 삭제 완료");
        return ResponseEntity.ok(statusResponse);
    }

    // 댓글 좋아요
    @PostMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<StatusResponse> likeComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                    @PathVariable int postId,
                                                      @PathVariable int commentId) {
        commentService.likeComment(userDetails.getUser(), postId, commentId);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "comment 좋아요 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(statusResponse);
    }

    // 댓글 좋아요 취소
    @DeleteMapping("/{postId}/comments/{commentId}/like")
    public ResponseEntity<StatusResponse> unlikeComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @PathVariable int postId,
                                                      @PathVariable int commentId) {
        commentService.unlikeComment(userDetails.getUser(), postId, commentId);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "comment 좋아요 취소 완료");
        return ResponseEntity.ok(statusResponse);
    }
}
