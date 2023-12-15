package com.ixxp.culpop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixxp.culpop.dto.popup.PopupCreateRequest;
import com.ixxp.culpop.entity.*;
import com.ixxp.culpop.mapper.PopupMapper;
import com.ixxp.culpop.mapper.PopupTagMapper;
import com.ixxp.culpop.mapper.StoreMapper;
import com.ixxp.culpop.mapper.TagMapper;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PopupService {
    private final PopupMapper popupMapper;
    private final StoreMapper storeMapper;
    private final TagMapper tagMapper;
    private final PopupTagMapper popupTagMapper;

    // 팝업 등록
    public void createPopup(Admin admin, PopupCreateRequest popupCreateRequest) {
        // image Json 으로 변환 후 store 저장
        String image = JSONArray.toJSONString(popupCreateRequest.getImageList());
        Store store = new Store(popupCreateRequest.getStore(), image);
        storeMapper.insertStore(store);

        // time Json 으로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        String time = null;
        try {
            time = objectMapper.writeValueAsString(popupCreateRequest.getTime());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        // popup 저장
        Popup popup = new Popup(admin, store, popupCreateRequest.getTitle(), popupCreateRequest.getContent(), time, popupCreateRequest.getAddress(),
                popupCreateRequest.getStartDate(), popupCreateRequest.getEndDate(), popupCreateRequest.getLatitude(), popupCreateRequest.getLongitude(),
                popupCreateRequest.getNotice(), popupCreateRequest.getStoreUrl(), popupCreateRequest.getSnsUrl(), popupCreateRequest.isParking(),
                popupCreateRequest.isFee(), popupCreateRequest.isNoKids(), popupCreateRequest.isPet(), popupCreateRequest.isWifi());
        popupMapper.insertPopup(popup);

        // tag 저장
        String tagList = popupCreateRequest.getTagList();
        String[] tagNameList = tagList.split(",");
        for (String tagName : tagNameList) {
            Tag tag = tagMapper.selectTag(tagName);
            if (tag == null) {
                tag = new Tag(tagName);
                tagMapper.insertTag(tag);
                PopupTag popupTag = new PopupTag(popup, tag);
                popupTagMapper.insertPopupTag(popupTag);
            }
        }
    }
}
