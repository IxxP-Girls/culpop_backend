package com.ixxp.culpop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "postId", nullable = false)
    private Post post;
    @Column(nullable = false)
    private String content;
    @Column(nullable = false)
    private boolean secret;
    private int parentId;
    @Column(nullable = false)
    private LocalDateTime createdAt;
    @Column(nullable = false)
    private LocalDateTime modifiedAt;

    public Comment(User user, Post post, String content, boolean secret, int parentId) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.secret = secret;
        this.parentId = parentId;
    }

    public void updateComment(User user, Post post, String content, boolean secret, int parentId) {
        this.user = user;
        this.post = post;
        this.content = content;
        this.secret = secret;
        this.parentId = parentId;
    }
}
