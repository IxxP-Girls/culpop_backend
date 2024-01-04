package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.popup.*;
import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.security.AdminDetailsImpl;
import com.ixxp.culpop.security.UserDetailsImpl;
import com.ixxp.culpop.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/popup")
public class PopupController {
    private final PopupService popupService;

    // 팝업 등록
    @PostMapping()
    public ResponseEntity<StatusResponse> createPopup(@AuthenticationPrincipal AdminDetailsImpl adminDetails,
                                                      @RequestBody PopupRequest popupRequest) {
        popupService.createPopup(adminDetails.getAdmin(), popupRequest);
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "popup 등록 완료");
        return ResponseEntity.status(HttpStatus.CREATED).body(statusResponse);
    }

    // MainPage 팝업 조회
    @GetMapping()
    public List<PopupResponse> getPopup(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestParam(value = "date", required = false) String date) {
        User user = Optional.ofNullable(userDetails).map(UserDetailsImpl::getUser).orElse(new User());
        return popupService.getPopup(user, date);
    }

    // MainPage Carousel 조회
    @GetMapping("/carousel")
    public ResponseEntity<List<PopupCarouselResponse>> getPopupCarousel() {
        return new ResponseEntity<>(popupService.getPopupCarousel(), HttpStatus.OK);
    }

    // ListPage 팝업 조회
    @GetMapping("/popups")
    public ResponseEntity<List<PopupResponse>> getPopupList(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                            @RequestParam("area") String area,
                                                            @RequestParam("startDate") String startDate,
                                                            @RequestParam("endDate") String endDate,
                                                            @RequestParam("page") int page,
                                                            @RequestParam("size") int size){
        User user = (userDetails != null) ? userDetails.getUser() : new User();
        List<PopupResponse> popupResponses = popupService.getPopupList(user, area, startDate, endDate, page, size);
        return new ResponseEntity<>(popupResponses, HttpStatus.OK);
    }

    // 팝업 상세 조회
    @GetMapping("/{popupId}")
    public ResponseEntity<PopupDetailResponse> getPopupDetail(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @PathVariable int popupId) {
        User user = (userDetails != null) ? userDetails.getUser() : new User();
        PopupDetailResponse popupDetailResponse = popupService.getPopupDetail(user, popupId);
        return new ResponseEntity<>(popupDetailResponse, HttpStatus.OK);
    }

    // 팝업 수정
    @PatchMapping("/{popupId}")
    public ResponseEntity<StatusResponse> updatePopup(@AuthenticationPrincipal AdminDetailsImpl adminDetails,
                                                      @PathVariable int popupId,
                                                      @RequestBody PopupUpdateRequest popupUpdateRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "popup 수정 완료");
        popupService.updatePopup(adminDetails.getAdmin(), popupId, popupUpdateRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 팝업 삭제
    @DeleteMapping("/{popupId}")
    public ResponseEntity<StatusResponse> deletePopup(@AuthenticationPrincipal AdminDetailsImpl adminDetails,
                                                      @PathVariable int popupId) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "popup 삭제 완료");
        popupService.deletePopup(adminDetails.getAdmin(), popupId);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 팝업 좋아요
    @PostMapping("/{popupId}/like")
    public ResponseEntity<StatusResponse> likePopup(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable int popupId) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "popup 좋아요 완료");
        popupService.likePopup(userDetails.getUser(), popupId);
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 팝업 좋아요 취소
    @DeleteMapping("/{popupId}/unlike")
    private ResponseEntity<StatusResponse> unlikePopup(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                       @PathVariable int popupId) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "popup 좋아요 취소 완료");
        popupService.unlikePopup(userDetails.getUser(), popupId);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 팝업 검색
    @GetMapping("/search")
    public ResponseEntity<List<PopupResponse>> getSearchPopup(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                              @RequestParam("word") String word,
                                                              @RequestParam("page") int page,
                                                              @RequestParam("size") int size) {
        User user = (userDetails != null) ? userDetails.getUser() : new User();
        List<PopupResponse> popupResponses = popupService.getSearchPopup(user, word, page, size);
        return new ResponseEntity<>(popupResponses, HttpStatus.OK);
    }
}
