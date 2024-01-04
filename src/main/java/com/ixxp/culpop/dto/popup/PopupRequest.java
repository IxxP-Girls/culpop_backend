package com.ixxp.culpop.dto.popup;

import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class PopupRequest {
    private String store;  // 스토어 이름
    private List<String> imageList;  // 이미지
    private String title;  // 제목
    private String content;  // 팝업스토어 소개
    private List<Map<String, Object>> time; // 운영 시간
    private String address;  // 주소
    private String startDate;  // 시작하는 날짜
    private String endDate;  // 끝나는 날짜
    private String latitude;  // 위도
    private String longitude;  // 경도
    private String notice;  // 안내 및 주의사항
    private String storeUrl;  // 브랜드 홈페이지
    private String snsUrl;  // SNS
    private boolean parking;  // 주차 가능 여부
    private boolean fee;  // 입장료 유료 여부
    private boolean noKids;  // 노키즈존 여부
    private boolean pet;  // 반려동물 가능 여부
    private boolean wifi;  // 와이파이 가능 여부
    private String tagList;  // 태그
}
