package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Popup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PopupMapper {
    void insertPopup(Popup popup);
    Popup selectPopup(int popupId);
    List<Popup> selectPopupMain(String date);
    List<Popup> selectCarousel();
    List<Popup> selectPopupList(String area,String startDate, String endDate, int offset, int size);
    Popup selectPopupDetail(int popupId);
    int selectViewCount(int popupId);
    void updateViewCount(int popupId);
}
