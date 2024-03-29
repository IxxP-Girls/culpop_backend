package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Post;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PostMapper {
    void insertPost(Post post);
    List<Post> selectPost(String category, int offset);
    Post selectPostDetail(int postId);
    List<Post> selectSearchPost(String word, int offset);
    List<Post> selectPostByUserId(int userId, int offset);
    int selectCategoryPostCount(String category);
    int selectWordPostCount(String word);
    int selectUserPostCount(int userId);
    int selectPostViewCount(int postId);
    void updatePostViewCount(int postId);
    void updatePost(Post post);
    void deletePost(int postId);
}
