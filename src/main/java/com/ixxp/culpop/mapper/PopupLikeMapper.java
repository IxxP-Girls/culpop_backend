package com.ixxp.culpop.mapper;

import com.ixxp.culpop.entity.PopupLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PopupLikeMapper {
    void insertPopupLike(PopupLike popupLike);
    boolean checkPopupLike(int userId);
    void deletePopupLike(PopupLike popupLike);
}
