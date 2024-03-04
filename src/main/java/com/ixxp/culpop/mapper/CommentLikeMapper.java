package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.CommentLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentLikeMapper {
    void insertCommentLike(CommentLike commentLike);
    boolean checkCommentLike(int userId, int commentId);
    void deleteCommentLike(CommentLike commentLike);
}
