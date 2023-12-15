package com.ixxp.culpop.dto.popup;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
public class PopupDetailResponse {
    private int popupId;
    private String store;
    private List<String> imageList;
    private String title;
    private String content;
    private List<Map<String, Object>> time;
    private String address;
    private String startDate;
    private String endDate;
    private String latitude;
    private String longitude;
    private String notice;
    private String storeUrl;
    private String snsUrl;
    private boolean parking;
    private boolean fee;
    private boolean noKids;
    private boolean pet;
    private boolean wifi;
    private int likeCount;
    private int viewCount;
    private boolean likeCheck;
    private List<String> tagList;
}
