package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.Popup;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PopupMapper {
    void insertPopup(Popup popup);
}
