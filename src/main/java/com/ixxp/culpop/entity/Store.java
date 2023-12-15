package com.ixxp.culpop.entity;

import io.hypersistence.utils.hibernate.type.json.JsonType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import java.io.Serializable;

@Getter
@NoArgsConstructor
@Entity
public class Store implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String storeName;

    @Type(JsonType.class)
    @Column(nullable = false, columnDefinition = "json")
    private String image;

    public Store(String storeName, String image) {
        this.storeName = storeName;
        this.image = image;
    }
}
