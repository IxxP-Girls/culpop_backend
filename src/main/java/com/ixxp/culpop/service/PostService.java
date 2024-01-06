package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.post.PostRequest;
import com.ixxp.culpop.entity.Category;
import com.ixxp.culpop.entity.Post;
import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.mapper.CategoryMapper;
import com.ixxp.culpop.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CategoryMapper categoryMapper;
    private final PostMapper postMapper;

    // 게시글 등록
    @Transactional
    public void createPost(User user, PostRequest postRequest) {
        String cateName = postRequest.getCateName();
        Category category = categoryMapper.selectCategory(cateName);
        if (category==null) {
            category = new Category(cateName);
            categoryMapper.insertCategory(category);
        }

        Post post = new Post(user, category, postRequest.getTitle(), postRequest.getContent());
        postMapper.insertPost(post);
    }
}
