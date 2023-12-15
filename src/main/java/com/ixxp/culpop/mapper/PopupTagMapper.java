package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.PopupTag;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PopupTagMapper {
    void insertPopupTag(PopupTag popupTag);
    List<PopupTag> selectPopupTag(int popupId);
}
