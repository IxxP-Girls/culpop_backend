package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.PostLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostLikeMapper {
    void insertPostLike(PostLike postLike);
    boolean checkPostLike(int userId, int postId);
}
