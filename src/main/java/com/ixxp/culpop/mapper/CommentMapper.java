package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper {
    void insertComment(Comment comment);
    Comment selectCommentDetail(int commentId);
    int countCommentsByPostId(int postId);
    void updateComment(Comment comment);
    void deleteComment(int commentId);
}
