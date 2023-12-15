package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.PopupTag;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PopupTagMapper {
    void insertPopupTag(PopupTag popupTag);
}
