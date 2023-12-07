package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.user.UserLoginRequest;
import com.ixxp.culpop.dto.user.UserSignupRequest;
import com.ixxp.culpop.service.UserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signup(@RequestBody @Valid UserSignupRequest userSignupRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "회원가입 성공");
        userService.signup(userSignupRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 로그인
    @PostMapping("login")
    public ResponseEntity<StatusResponse> login(@RequestBody @Valid UserLoginRequest userLoginRequest, HttpServletResponse response) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그인 성공");
        userService.login(userLoginRequest, response);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
}
