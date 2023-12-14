package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.popup.PopupCreateRequest;
import com.ixxp.culpop.security.AdminDetailsImpl;
import com.ixxp.culpop.service.PopupService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
