package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface CommentMapper {
    void insertComment(Comment comment);
    Comment selectCommentDetail(int commentId);
    List<Comment> selectComment(int postId, int offset);
    List<Comment> selectCommentByParentId(int commentId);
    int countCommentsByPostId(int postId);
    void updateComment(Comment comment);
    void deleteComment(int commentId);
}
