package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.popup.PopupCreateRequest;
import com.ixxp.culpop.security.AdminDetailsImpl;
import com.ixxp.culpop.security.UserDetailsImpl;
import com.ixxp.culpop.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/popup")
public class PopupController {
    private final PopupService popupService;

    // 팝업 등록
    @PostMapping()
    public ResponseEntity<StatusResponse> createPopup(@AuthenticationPrincipal AdminDetailsImpl adminDetails,
                                                      @RequestBody PopupCreateRequest popupCreateRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "popup 등록 완료");
        popupService.createPopup(adminDetails.getAdmin(), popupCreateRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 팝업 좋아요
    @PostMapping("/{popupId}/like")
    public ResponseEntity<StatusResponse> likePopup (@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                     @PathVariable int popupId) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "popup 좋아요 완료");
        popupService.likePopup(userDetails.getUser(), popupId);
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }
}
