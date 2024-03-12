package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.post.PostDetailResponse;
import com.ixxp.culpop.dto.post.PostRequest;
import com.ixxp.culpop.dto.post.PostResponse;
import com.ixxp.culpop.entity.Category;
import com.ixxp.culpop.entity.Post;
import com.ixxp.culpop.entity.PostLike;
import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.mapper.CategoryMapper;
import com.ixxp.culpop.mapper.CommentMapper;
import com.ixxp.culpop.mapper.PostLikeMapper;
import com.ixxp.culpop.mapper.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostService {
    private final CategoryMapper categoryMapper;
    private final PostMapper postMapper;
    private final PostLikeMapper postLikeMapper;
    private final CommentMapper commentMapper;

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
             new PostResponse(post.getId(), post.getUser().getUsername(), post.getTitle(), post.getCategory().getCateName(), postMapper.selectPostViewCount(post.getId()), posts.size(), post.getCreatedAt())
        ).collect(Collectors.toList());
    }

    // 게시글 개별 조회
    public PostDetailResponse getPostDetail(User user, int postId) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new EntityNotFoundException("post가 존재하지 않습니다.");
        }

        boolean likeCheck = (user != null) && postLikeMapper.checkPostLike(user.getId(), postId);
        int likeCount = postLikeMapper.countLikesByPostId(postId);

        postMapper.updatePostViewCount(postId);
        int viewCount = postMapper.selectPostViewCount(postId);

        int commentCount = commentMapper.countCommentsByPostId(postId);

        return new PostDetailResponse(postId, post.getUser().getUsername(), post.getTitle(), post.getContent()
                , post.getCreatedAt(), post.getModifiedAt(), post.getCategory().getCateName(), likeCheck, likeCount, viewCount, commentCount);
    }

    // 게시글 수정
    @Transactional
    public void updatePost(User user, int postId, PostRequest postRequest) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new EntityNotFoundException("post가 존재하지 않습니다.");
        }

        if (post.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("작성자만 수정 가능합니다.");
        }

        String cateName = postRequest.getCateName();
        Category category = post.getCategory();
        if (!Objects.equals(cateName, post.getCategory().getCateName())) {
            category = categoryMapper.selectCategory(cateName);
            if (category == null) {
                category = new Category(cateName);
                categoryMapper.insertCategory(category);
            }
        }

        post.updatePost(user, category, postRequest.getTitle(), postRequest.getContent());
        postMapper.updatePost(post);
    }

    // 게시글 삭제
    @Transactional
    public void deletePopup(User user, int postId) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new EntityNotFoundException("post가 존재하지 않습니다.");
        }

        if (post.getUser().getId() != user.getId()) {
            throw new AccessDeniedException("작성자만 수정 가능합니다.");
        }

        postMapper.deletePost(postId);
    }

    // 게시글 좋아요
    @Transactional
    public void likePost(User user, int postId) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new EntityNotFoundException("post가 존재하지 않습니다.");
        }
        if (postLikeMapper.checkPostLike(user.getId(), postId)) {
            throw new DuplicateKeyException("이미 좋아요를 눌렀습니다.");
        }

        PostLike postLike = new PostLike(user, post);
        postLikeMapper.insertPostLike(postLike);
    }

    // 게시글 좋아요 취소
    @Transactional
    public void unlikePost(User user, int postId) {
        Post post = postMapper.selectPostDetail(postId);
        if (post == null) {
            throw new EntityNotFoundException("post가 존재하지 않습니다.");
        }
        if (!postLikeMapper.checkPostLike(user.getId(), postId)) {
            throw new NoSuchElementException("좋아요를 누르지 않았습니다.");
        }

        PostLike postLike = new PostLike(user, post);
        postLikeMapper.deletePostLike(postLike);
    }

    // 게시글 검색
    public List<PostResponse> getSearchPost(String word, int page) {
        int offset = (page - 1) * 10;
        List<Post> posts = postMapper.selectSearchPost(word, offset);
        return posts.stream().map(post ->
                new PostResponse(post.getId(), post.getUser().getUsername(), post.getTitle(), post.getCategory().getCateName(), postMapper.selectPostViewCount(post.getId()), posts.size(), post.getCreatedAt())
        ).collect(Collectors.toList());
    }
}
