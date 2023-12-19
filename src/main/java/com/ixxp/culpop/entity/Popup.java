package com.ixxp.culpop.entity;

import javax.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@NoArgsConstructor
@Entity
public class Popup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    @JoinColumn(name = "adminId", nullable = false)
    private Admin admin;
    @ManyToOne
    @JoinColumn(name = "storeId", nullable = false)
    private Store store;
    @Column(nullable = false)
    private String title;  // 제목
    @Column(nullable = false)
    private String content;  // 팝업스토어 소개
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(nullable = false, columnDefinition = "json")
    private String time; // 운영 시간
    @Column(nullable = false)
    private String address;  // 주소
    @Column(nullable = false)
    private String startDate;  // 시작하는 날짜
    @Column(nullable = false)
    private String endDate;  // 끝나는 날짜
    @Column(nullable = false)
    private String latitude;  // 위도
    @Column(nullable = false)
    private String longitude;  // 경도
    private String notice;  // 안내 및 주의사항
    private String storeUrl;  // 브랜드 홈페이지
    private String snsUrl;  // SNS 링크
    @Column(nullable = false)
    private boolean parking;  // 주차 가능 여부
    @Column(nullable = false)
    private boolean fee;  // 입장료 유료 여부
    @Column(nullable = false)
    private boolean noKids;  // 노키즈존 여부
    @Column(nullable = false)
    private boolean pet;  // 반려동물 가능 여부
    @Column(nullable = false)
    private boolean wifi;  // 와이파이 가능 여부

    @Column(nullable = false)
    private int viewCount; // 조회수

    public Popup(Admin admin, Store store, String title, String content, String time, String address, String startDate, String endDate,
                 String latitude, String longitude, String notice, String storeUrl, String snsUrl,
                 boolean parking, boolean fee, boolean noKids, boolean pet, boolean wifi) {
        this.admin = admin;
        this.store = store;
        this.title = title;
        this.content = content;
        this.time = time;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.notice = notice;
        this.storeUrl = storeUrl;
        this.snsUrl = snsUrl;
        this.parking = parking;
        this.fee = fee;
        this.noKids = noKids;
        this.pet = pet;
        this.wifi = wifi;
        this.viewCount = 0;
    }
}
