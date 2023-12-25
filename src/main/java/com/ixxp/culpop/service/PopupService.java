package com.ixxp.culpop.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixxp.culpop.dto.popup.*;
import com.ixxp.culpop.entity.*;
import com.ixxp.culpop.mapper.*;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
            Tag tag = new Tag(tagName);
            tagMapper.insertTag(tag);
            popupTagMapper.insertPopupTag(new PopupTag(popup, tag));
        }
    }

    // MainPage 팝업 조회
    public List<PopupResponse> getPopup(User user, String date) {
        List<Popup> popups = popupMapper.selectPopupMain(date);
        List<PopupResponse> popupResponses = new ArrayList<>();
        for (Popup popup : popups) {
            org.json.JSONArray jsonArray = new org.json.JSONArray(popup.getStore().getImage());
            String image = jsonArray.getString(0);

            String fullAddress = popup.getAddress();
            String address = fullAddress.substring(0,fullAddress.indexOf(" ", fullAddress.indexOf(" ") + 1));

            String startDate = popup.getStartDate().replace("-", ".");
            String endDate = popup.getEndDate().replace("-", ".");

            boolean likeCheck = false;
            if (user != null) {
                likeCheck = popupLikeMapper.checkPopupLike(user.getId(), popup.getId());
            }

            popupResponses.add(new PopupResponse(popup.getId(), image, popup.getTitle(), address, startDate, endDate, likeCheck));
        }
        return popupResponses;
    }

    // MainPage Carousel 조회
    public List<PopupCarouselResponse> getPopupCarousel() {
        // 좋아요 많은 순 6개만 가져오기
        List<Popup> popups = popupMapper.selectCarousel();
        List<PopupCarouselResponse> popupCarouselResponses = new ArrayList<>();
        for (Popup popup : popups) {
            org.json.JSONArray jsonArray = new org.json.JSONArray(popup.getStore().getImage());
            String image = jsonArray.getString(0);

            String fullAddress = popup.getAddress();
            String address = fullAddress.substring(0,fullAddress.indexOf(" ", fullAddress.indexOf(" ") + 1));

            String startDate = popup.getStartDate().replace("-", ".");
            String endDate = popup.getEndDate().replace("-", ".");

            popupCarouselResponses.add(new PopupCarouselResponse(popup.getId(), image, popup.getTitle(), address, startDate, endDate));
        }
        return popupCarouselResponses;
    }

    // ListPage 팝업 조회
    public List<PopupResponse> getPopupList(User user, String area, String startDate, String endDate, int page, int size) {
        int offset = (page - 1) * size;
        List<Popup> popups = popupMapper.selectPopupList(area, startDate, endDate, offset, size);
        List<PopupResponse> popupResponses = new ArrayList<>();
        for (Popup popup : popups) {
            org.json.JSONArray jsonArray = new org.json.JSONArray(popup.getStore().getImage());
            String image = jsonArray.getString(0);

            String fullAddress = popup.getAddress();
            String address = fullAddress.substring(0,fullAddress.indexOf(" ", fullAddress.indexOf(" ") + 1));

            String start = popup.getStartDate().replace("-", ".");
            String end = popup.getEndDate().replace("-", ".");

            boolean likeCheck = false;
            if (user != null) {
                likeCheck = popupLikeMapper.checkPopupLike(user.getId(), popup.getId());
            }

            popupResponses.add(new PopupResponse(popup.getId(), image, popup.getTitle(), address, start, end, likeCheck));
        }
        return popupResponses;
    }

    // 팝업 상세 조회
    @Transactional
    public PopupDetailResponse getPopupDetail(User user, int popupId) {
        Popup popup = popupMapper.selectPopupDetail(popupId);
        if (popup == null) {
            throw new IllegalArgumentException("Popup 이 존재하지 않습니다.");
        }

        org.json.JSONArray jsonArray = new org.json.JSONArray(popup.getStore().getImage());
        List<String> imageList = new ArrayList<>();
        for (Object image : jsonArray) {
            if (image instanceof String) {
                imageList.add((String) image);
            }
        }

        ObjectMapper objectMapper = new ObjectMapper();
        List<Map<String, Object>> time = null;
        try {
            time = objectMapper.readValue(popup.getTime(), new TypeReference<List<Map<String, Object>>>() {});
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        String startDate = popup.getStartDate().replace("-", ".");
        String endDate = popup.getEndDate().replace("-", ".");

        int likeCount = popupLikeMapper.countLikesByPopupId(popupId);
        popupMapper.updateViewCount(popupId);
        int viewCount = popupMapper.selectViewCount(popupId);

        boolean likeCheck = false;
        if (user != null) {
            likeCheck = popupLikeMapper.checkPopupLike(user.getId(), popup.getId());
        }

        List<PopupTag> popupTag = popupTagMapper.selectPopupTag(popupId);
        List<String> tagList = new ArrayList<>();
        for (PopupTag tag : popupTag) {
            tagList.add(tag.getTag().getTagName());
        }

        return new PopupDetailResponse(popupId, popup.getStore().getStoreName(), imageList,
                popup.getTitle(), popup.getContent(), time, popup.getAddress(), startDate, endDate,
                popup.getLatitude(), popup.getLongitude(), popup.getNotice(), popup.getStoreUrl(), popup.getSnsUrl(),
                popup.isParking(), popup.isFee(), popup.isNoKids(), popup.isPet(), popup.isWifi(), likeCount, viewCount, likeCheck, tagList);
    }

    // 팝업 수정
    @Transactional
    public void updatePopup(Admin admin, int popupId, PopupUpdateRequest popupUpdateRequest) {
        Popup popup = popupMapper.selectPopup(popupId);

        if (popup == null) {
            throw new IllegalArgumentException("popup 이 존재하지 않습니다.");
        }
        if (popup.getAdmin().getId() != admin.getId()) {
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }

        String image = JSONArray.toJSONString(popupUpdateRequest.getImageList());
        Store store = storeMapper.selectStore(popup.getStore().getId());
        store.updateStore(popupUpdateRequest.getStore(), image);
        storeMapper.updateStore(store);

        ObjectMapper objectMapper = new ObjectMapper();
        String time = null;
        try {
            time = objectMapper.writeValueAsString(popupUpdateRequest.getTime());
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        popup.updatePopup(admin, new Store(popupUpdateRequest.getStore(), image), popupUpdateRequest.getTitle(), popupUpdateRequest.getContent(), time, popupUpdateRequest.getAddress(),
                popupUpdateRequest.getStartDate(), popupUpdateRequest.getEndDate(), popupUpdateRequest.getLatitude(), popupUpdateRequest.getLongitude(),
                popupUpdateRequest.getNotice(), popupUpdateRequest.getStoreUrl(), popupUpdateRequest.getSnsUrl(), popupUpdateRequest.isParking(),
                popupUpdateRequest.isFee(), popupUpdateRequest.isNoKids(), popupUpdateRequest.isPet(), popupUpdateRequest.isWifi());
        popupMapper.updatePopup(popup);


        popupTagMapper.deletePopupTag(popupId);
        insertTagsInSeparateTransaction(popup, popupUpdateRequest.getTagList());
    }

    @Transactional
    public void insertTagsInSeparateTransaction(Popup popup, String tagList) {
        String[] tagNameList = tagList.split(",");
        for (String tagName : tagNameList) {
            Tag tag = new Tag(tagName);
            tagMapper.insertTag(tag);
            popupTagMapper.insertPopupTag(new PopupTag(popup, tag));
        }
    }

    // 팝업 삭제
    @Transactional
    public void deletePopup(Admin admin, int popupId) {
        Popup popup = popupMapper.selectPopup(popupId);

        if (popup == null) {
            throw new IllegalArgumentException("popup 이 존재하지 않습니다.");
        }
        if (popup.getAdmin().getId() != admin.getId()) {
            throw new IllegalArgumentException("작성자만 수정 가능합니다.");
        }

        List<PopupTag> popupTags = popupTagMapper.selectPopupTag(popupId);

        popupTagMapper.deletePopupTag(popupId);
        for (PopupTag popupTag : popupTags) {
            tagMapper.deleteTag(popupTag.getTag().getId());
        }
        popupMapper.deletePopup(popup);
        storeMapper.deleteStore(popup.getStore().getId());
    }

    // 팝업 좋아요
    public void likePopup(User user, int popupId) {
        Popup popup = popupMapper.selectPopup(popupId);
        if (popup == null) {
            throw new IllegalArgumentException("Popup 이 존재하지 않습니다.");
        }

        if (popupLikeMapper.checkPopupLike(user.getId(), popupId)) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        PopupLike popupLike = new PopupLike(user, popup);
        popupLikeMapper.insertPopupLike(popupLike);
    }

    // 팝업 좋아요 취소
    public void unlikePopup(User user, int popupId) {
        Popup popup = popupMapper.selectPopup(popupId);
        if (popup == null) {
            throw new IllegalArgumentException("Popup 이 존재하지 않습니다.");
        }

        if (!popupLikeMapper.checkPopupLike(user.getId(), popupId)) {
            throw new IllegalArgumentException("popup 좋아요를 누르지 않았습니다.");
        }

        PopupLike popupLike = new PopupLike(user, popup);
        popupLikeMapper.deletePopupLike(popupLike);
    }

    // 팝업 검색
    @Transactional
    public List<PopupResponse> getSearchPopup(User user, String word, int page, int size) {
        int offset = (page - 1) * size;
        List<Popup> popups = popupMapper.selectSearchPopup(word, offset, size);
        List<PopupResponse> popupResponses = new ArrayList<>();
        for (Popup popup : popups) {
            org.json.JSONArray jsonArray = new org.json.JSONArray(popup.getStore().getImage());
            String image = jsonArray.getString(0);

            String fullAddress = popup.getAddress();
            String address = fullAddress.substring(0,fullAddress.indexOf(" ", fullAddress.indexOf(" ") + 1));

            String startDate = popup.getStartDate().replace("-", ".");
            String endDate = popup.getEndDate().replace("-", ".");

            boolean likeCheck = false;
            if (user != null) {
                likeCheck = popupLikeMapper.checkPopupLike(user.getId(), popup.getId());
            }

            popupResponses.add(new PopupResponse(popup.getId(), image, popup.getTitle(), address, startDate, endDate, likeCheck));
        }
        return popupResponses;
    }
}
