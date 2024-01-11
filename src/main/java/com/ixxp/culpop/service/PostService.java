package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.post.PostRequest;
import com.ixxp.culpop.dto.post.PostResponse;
import com.ixxp.culpop.entity.Category;
import com.ixxp.culpop.entity.Post;
import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.mapper.CategoryMapper;
import com.ixxp.culpop.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    // 게시글 전체 조회
    public List<PostResponse> getPost(String category, int page) {
        int offset = (page - 1) * 10;
        List<Post> posts = postMapper.selectPost(category, offset);
        return posts.stream().map(post ->
             new PostResponse(post.getId(), post.getUser().getUsername(), post.getTitle(), post.getCategory().getCateName(), post.getCreatedAt())
        ).collect(Collectors.toList());
    }
}
