package com.ixxp.culpop.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "commentId", nullable = false)
    private Comment comment;

    public CommentLike(User user, Comment comment) {
        this.user = user;
        this.comment = comment;
    }
}
