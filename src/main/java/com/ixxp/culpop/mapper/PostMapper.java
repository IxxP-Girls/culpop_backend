package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Post;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostMapper {
    void insertPost(Post post);
}
