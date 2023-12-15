package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Popup;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PopupMapper {
    void insertPopup(Popup popup);
    Popup selectPopup(int popupId);
    List<Popup> selectPopupMain(String date);
    List<Popup> selectPopupList();
}
