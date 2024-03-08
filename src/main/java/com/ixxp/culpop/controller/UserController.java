package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.popup.PopupResponse;
import com.ixxp.culpop.dto.user.ProfileResponse;
import com.ixxp.culpop.dto.user.ProfileUpdateRequest;
import com.ixxp.culpop.dto.user.UserLoginRequest;
import com.ixxp.culpop.dto.user.UserSignupRequest;
import com.ixxp.culpop.security.UserDetailsImpl;
import com.ixxp.culpop.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import com.ixxp.culpop.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "회원가입 성공");
        userService.signup(userSignupRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity<StatusResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest, HttpServletResponse response) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그인 성공");
        userService.login(userLoginRequest, response);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 로그인 확인
    @GetMapping("/verify")
    public ResponseEntity<String> verifyLogin(HttpServletRequest request) {
        String token = jwtUtil.getAccessToken(request);
        boolean isLogin = token != null && jwtUtil.validateToken(token);

        return ResponseEntity.ok("{\"isLogin\": " + isLogin + "}");
    }

    // 프로필 수정
    @PatchMapping("/profile")
    public ResponseEntity<StatusResponse> updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                        @RequestBody ProfileUpdateRequest profileUpdateRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "프로필 수정 완료");
        userService.updateProfile(userDetails.getUser().getId(), profileUpdateRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }

    // 프로필 조회
    @GetMapping("/profile")
    public ResponseEntity<ProfileResponse> getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                      @RequestParam(name = "page", defaultValue = "1") int page) {
        ProfileResponse profileResponse = userService.getProfile(userDetails.getUser().getId(), page);
        return new ResponseEntity<>(profileResponse, HttpStatus.OK);
    }

    // 프로필 관심 팝업 조회
    @GetMapping("/profile/popupLike")
    public ResponseEntity<List<PopupResponse>> getProfilePopup(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @RequestParam(name = "sort", defaultValue = "전체") String sort,
                                                               @RequestParam(name = "page", defaultValue = "1") int page) {
        List<PopupResponse> popupResponses = userService.getProfilePopup(userDetails.getUser(), sort, page);
        return new ResponseEntity<>(popupResponses, HttpStatus.OK);
    }
}
