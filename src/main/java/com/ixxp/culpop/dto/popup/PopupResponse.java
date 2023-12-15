package com.ixxp.culpop.dto.popup;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopupResponse {
    private int popupId;
    private String image;
    private String title;
    private String address;
    private String startDate;
    private String endDate;
    private boolean likeCheck;

    public PopupResponse(int popupId, String image, String title, String address, String startDate, String endDate, boolean likeCheck) {
        this.popupId = popupId;
        this.image = image;
        this.title = title;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
        this.likeCheck = likeCheck;
    }
}
