package com.ixxp.culpop.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class PopupTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "popupId", nullable = false)
    private Popup popup;
    @ManyToOne
    @JoinColumn(name = "tagId", nullable = false)
    private Tag tag;

    public PopupTag(Popup popup, Tag tag) {
        this.popup = popup;
        this.tag = tag;
    }
}
