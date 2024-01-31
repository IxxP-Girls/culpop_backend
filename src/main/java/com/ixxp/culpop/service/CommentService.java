package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.post.CommentRequest;
import com.ixxp.culpop.entity.Comment;
import com.ixxp.culpop.entity.Post;
import com.ixxp.culpop.entity.User;
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
}
