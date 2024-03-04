package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.post.CommentRequest;
import com.ixxp.culpop.entity.Comment;
import com.ixxp.culpop.entity.CommentLike;
import com.ixxp.culpop.entity.Post;
import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.mapper.CommentLikeMapper;
import com.ixxp.culpop.mapper.CommentMapper;
import com.ixxp.culpop.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final PostMapper postMapper;
    private final CommentMapper commentMapper;
    private final CommentLikeMapper commentLikeMapper;

    // 댓글 등록
    @Transactional
    public void createComment(User user, int postId, CommentRequest commentRequest) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new IllegalArgumentException("post가 존재하지 않습니다.");
        }

        Comment comment = new Comment(user, post, commentRequest.getContent(), commentRequest.isSecret(), commentRequest.getParentId());
        commentMapper.insertComment(comment);
    }

    // 댓글 조회

    // 댓글 수정
    @Transactional
    public void updateComment(User user, int postId, int commentId, CommentRequest commentRequest) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new IllegalArgumentException("post가 존재하지 않습니다.");
        }

        Comment comment = commentMapper.selectCommentDetail(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("comment가 존재하지 않습니다.");
        }

        if (comment.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }

        comment.updateComment(user, post, commentRequest.getContent(), commentRequest.isSecret(), commentRequest.getParentId());
        commentMapper.updateComment(comment);
    }


    // 댓글 삭제
    @Transactional
    public void deleteComment(User user, int postId, int commentId) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new IllegalArgumentException("post가 존재하지 않습니다.");
        }

        Comment comment = commentMapper.selectCommentDetail(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("comment가 존재하지 않습니다.");
        }

        if (comment.getUser().getId() != user.getId()) {
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }

        if (comment.getPost().getId() != postId) {
            throw new IllegalArgumentException("post를 잘못 입력하였습니다.");
        }

        commentMapper.deleteComment(commentId);
    }

    // 팝업 좋아요
    @Transactional
    public void likeComment(User user, int postId, int commentId) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new IllegalArgumentException("post가 존재하지 않습니다.");
        }

        Comment comment = commentMapper.selectCommentDetail(commentId);
        if (comment == null) {
            throw new IllegalArgumentException("comment가 존재하지 않습니다.");
        }

        if (comment.getPost().getId() != postId) {
            throw new IllegalArgumentException("post를 잘못 입력하였습니다.");
        }

        if (commentLikeMapper.checkCommentLike(user.getId(), commentId)) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        CommentLike commentLike = new CommentLike(user, comment);
        commentLikeMapper.insertCommentLike(commentLike);
    }
}
