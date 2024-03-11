package com.ixxp.culpop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixxp.culpop.dto.popup.*;
import com.ixxp.culpop.entity.*;
import com.ixxp.culpop.mapper.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpHeaders;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopupService {
    private final PopupMapper popupMapper;
    private final StoreMapper storeMapper;
    private final TagMapper tagMapper;
    private final PopupTagMapper popupTagMapper;
    private final PopupLikeMapper popupLikeMapper;

    // 팝업 등록
    @Transactional
    public void createPopup(Admin admin, PopupRequest popupRequest) {
        Store store = saveStore(popupRequest.getStore(), popupRequest.getImageList());
        String time = convertTimeToJson(popupRequest.getTime());
        Popup popup = insertPopup(admin, store, popupRequest, time);
        saveTags(popupRequest.getTagList(), popup);
    }

    // MainPage 팝업 조회
    public List<PopupResponse> getPopup(User user, String date) {
        List<Popup> popups = popupMapper.selectPopupMain(date);
        return convertToPopupResponseList(user, popups);
    }

    // MainPage Carousel 조회
    public List<PopupCarouselResponse> getPopupCarousel() {
        List<Popup> popups = popupMapper.selectCarousel();
        List<PopupResponse> popupResponses = convertToPopupResponseList(null, popups);

        return popupResponses.stream()
                .map(response -> new PopupCarouselResponse(response.getPopupId(), response.getImage(), response.getTitle(),
                        response.getAddress(), response.getStartDate(), response.getEndDate()))
                .collect(Collectors.toList());
    }

    // ListPage 팝업 조회
    public List<PopupResponse> getPopupList(User user, String area, String startDate, String endDate, int page) {
        int offset = (page - 1) * 10;
        List<Popup> popups = popupMapper.selectPopupList(area, startDate, endDate, offset);

        return convertToPopupResponseList(user, popups);
    }

    // 팝업 상세 조회
    @Transactional
    public PopupDetailResponse getPopupDetail(User user, int popupId) {
        Popup popup = getValidPopup(popupId);

        List<String> imageList = getImageList(popup);
        List<Map<String, Object>> timeList = convertTimeToList(popup.getTime());
        List<String> tagList = getTagList(popup);

        updatePopupCounts(popupId);
        boolean likeCheck = (user != null) && popupLikeMapper.checkPopupLike(user.getId(), popupId);

        String startDate = popup.getStartDate().replace("-", ".");
        String endDate = popup.getEndDate().replace("-", ".");

        return new PopupDetailResponse(popupId, popup.getStore().getStoreName(), imageList,
                popup.getTitle(), popup.getContent(), timeList, popup.getAddress(),
                startDate, endDate, popup.getLatitude(), popup.getLongitude(),
                popup.getNotice(), popup.getStoreUrl(), popup.getSnsUrl(), popup.isParking(),
                popup.isFee(), popup.isNoKids(), popup.isPet(), popup.isWifi(),
                getLikeCountByPopupId(popupId), getViewCountByPopupId(popupId), likeCheck, tagList);
    }

    // 팝업 수정
    @Transactional
    public void updatePopup(Admin admin, int popupId, PopupRequest popupRequest) {
        Popup popup = getValidPopup(popupId);
        validateAdmin(popup, admin);

        Store store = updateStore(popupRequest.getStore(), popupRequest.getImageList(), popup);

        String time = convertTimeToJson(popupRequest.getTime());

        popup.updatePopup(admin, store, popupRequest.getTitle(), popupRequest.getContent(), time, popupRequest.getAddress(),
                popupRequest.getStartDate(), popupRequest.getEndDate(), popupRequest.getLatitude(), popupRequest.getLongitude(),
                popupRequest.getNotice(), popupRequest.getStoreUrl(), popupRequest.getSnsUrl(), popupRequest.isParking(),
                popupRequest.isFee(), popupRequest.isNoKids(), popupRequest.isPet(), popupRequest.isWifi());
        popupMapper.updatePopup(popup);

        updateTags(popup, popupRequest.getTagList());
    }

    // 팝업 삭제
    @Transactional
    public void deletePopup(Admin admin, int popupId) {
        Popup popup = getValidPopup(popupId);
        validateAdmin(popup, admin);

        List<PopupTag> popupTags = popupTagMapper.selectPopupTag(popup.getId());
        deletePopupTagsAndTags(popupTags, popup);

        popupMapper.deletePopup(popup);
        storeMapper.deleteStore(popup.getStore().getId());
    }

    // 팝업 좋아요
    @Transactional
    public void likePopup(User user, int popupId) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Access-Control-Allow-Methods","GET,POST,OPTIONS,DELETE,PUT");
        Popup popup = getValidPopup(popupId);
        validatePopupLike(user, popupId);

        PopupLike popupLike = new PopupLike(user, popup);
        popupLikeMapper.insertPopupLike(popupLike);
    }

    // 팝업 좋아요 취소
    @Transactional
    public void unlikePopup(User user, int popupId) {
        Popup popup = getValidPopup(popupId);
        validatePopupUnlike(user, popupId);

        PopupLike popupLike = new PopupLike(user, popup);
        popupLikeMapper.deletePopupLike(popupLike);
    }

    // 팝업 검색
    @Transactional
    public List<PopupResponse> getSearchPopup(User user, String word, int page) {
        int offset = (page - 1) * 10;
        List<Popup> popups = popupMapper.selectSearchPopup(word, offset);

        return convertToPopupResponseList(user, popups);
    }

    private Store saveStore(String storeName, List<String> imageList) {
        String image = JSONArray.toJSONString(imageList);
        Store store = new Store(storeName, image);
        storeMapper.insertStore(store);
        return store;
    }

    private String convertTimeToJson(List<Map<String, Object>> time) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(time);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("popup의 time을 JSON으로 변환하는데 실패했습니다.", e);
        }
    }

    private Popup insertPopup(Admin admin, Store store, PopupRequest popupRequest, String time) {
        Popup popup = new Popup(admin, store, popupRequest.getTitle(), popupRequest.getContent(), time,
                popupRequest.getAddress(), popupRequest.getStartDate(), popupRequest.getEndDate(),
                popupRequest.getLatitude(), popupRequest.getLongitude(), popupRequest.getNotice(),
                popupRequest.getStoreUrl(), popupRequest.getSnsUrl(), popupRequest.isParking(),
                popupRequest.isFee(), popupRequest.isNoKids(), popupRequest.isPet(), popupRequest.isWifi());
        popupMapper.insertPopup(popup);
        return popup;
    }

    private void saveTags(String tagList, Popup popup) {
        String[] tagNameList = tagList.split(",");
        for (String tagName : tagNameList) {
            Tag tag = new Tag(tagName);
            tagMapper.insertTag(tag);
            popupTagMapper.insertPopupTag(new PopupTag(popup, tag));
        }
    }

    public List<PopupResponse> convertToPopupResponseList(User user, List<Popup> popups) {
        return popups.stream()
                .map(popup -> {
                    org.json.JSONArray jsonArray = new org.json.JSONArray(popup.getStore().getImage());
                    String image = jsonArray.getString(0);

                    String fullAddress = popup.getAddress();
                    String address = fullAddress.substring(0, fullAddress.indexOf(" ", fullAddress.indexOf(" ") + 1));

                    String startDate = popup.getStartDate().replace("-", ".");
                    String endDate = popup.getEndDate().replace("-", ".");

                    boolean likeCheck = (user != null) && popupLikeMapper.checkPopupLike(user.getId(), popup.getId());

                    return new PopupResponse(popup.getId(), image, popup.getTitle(), address, startDate, endDate, likeCheck);
                })
                .collect(Collectors.toList());
    }

    private Popup getValidPopup(int popupId) {
        Popup popup = popupMapper.selectPopup(popupId);
        if (popup == null) {
            throw new EntityNotFoundException("Popup이 존재하지 않습니다.");
        }
        return popup;
    }

    private List<String> getImageList(Popup popup) {
        org.json.JSONArray imageList = new org.json.JSONArray(popup.getStore().getImage());
        return imageList.toList().stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .collect(Collectors.toList());
    }

    private List<Map<String, Object>> convertTimeToList(String time) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(time, new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("popup의 time을 JSON으로 변환하는데 실패했습니다.", e);
        }
    }

    private List<String> getTagList(Popup popup) {
        List<PopupTag> popupTags = popupTagMapper.selectPopupTag(popup.getId());
        return popupTags.stream()
                .map(tag -> tag.getTag().getTagName())
                .collect(Collectors.toList());
    }

    private void updatePopupCounts(int popupId) {
        popupMapper.updateViewCount(popupId);
    }

    private int getLikeCountByPopupId(int popupId) {
        return popupLikeMapper.countLikesByPopupId(popupId);
    }

    private int getViewCountByPopupId(int popupId) {
        return popupMapper.selectViewCount(popupId);
    }

    private void validateAdmin(Popup popup, Admin admin) {
        if (popup.getAdmin().getId() != admin.getId()) {
            throw new AccessDeniedException("작성자만 수정 가능합니다.");
        }
    }

    private Store updateStore(String storeName, List<String> imageList, Popup popup) {
        String image = JSONArray.toJSONString(imageList);
        Store store = storeMapper.selectStore(popup.getStore().getId());
        store.updateStore(storeName, image);
        storeMapper.updateStore(store);
        return store;
    }

    private void updateTags(Popup popup, String tagList) {
        List<PopupTag> popupTags = popupTagMapper.selectPopupTag(popup.getId());
        deletePopupTagsAndTags(popupTags, popup);
        saveTags(tagList, popup);
    }

    private void deletePopupTagsAndTags(List<PopupTag> popupTags, Popup popup) {
        popupTags.forEach(popupTag -> {
            popupTagMapper.deletePopupTag(popup.getId());
            tagMapper.deleteTag(popupTag.getTag().getId());
        });
    }

    private void validatePopupLike(User user, int popupId) {
        if (popupLikeMapper.checkPopupLike(user.getId(), popupId)) {
            throw new DuplicateKeyException("이미 좋아요를 눌렀습니다.");
        }
    }

    private void validatePopupUnlike(User user, int popupId) {
        if (!popupLikeMapper.checkPopupLike(user.getId(), popupId)) {
            throw new NoSuchElementException("팝업 좋아요를 누르지 않았습니다.");
        }
    }
}
