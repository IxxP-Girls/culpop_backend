package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    void insertPost(Post post);
    List<Post> selectPost(String category, int offset);
}
