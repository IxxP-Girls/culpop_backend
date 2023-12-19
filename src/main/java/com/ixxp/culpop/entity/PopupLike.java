package com.ixxp.culpop.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PopupLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    @ManyToOne
    @JoinColumn(name = "popupId", nullable = false)
    private Popup popup;

    public PopupLike(User user, Popup popup) {
        this.user = user;
        this.popup = popup;
    }
}
