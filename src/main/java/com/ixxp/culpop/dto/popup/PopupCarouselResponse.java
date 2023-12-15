package com.ixxp.culpop.dto.popup;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PopupCarouselResponse {
    private int popupId;
    private String image;
    private String title;
    private String address;
    private String startDate;
    private String endDate;

    public PopupCarouselResponse(int popupId, String image, String title, String address, String startDate, String endDate) {
        this.popupId = popupId;
        this.image = image;
        this.title = title;
        this.address = address;
        this.startDate = startDate;
        this.endDate = endDate;
    }
}
