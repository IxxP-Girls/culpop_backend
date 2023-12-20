package com.ixxp.culpop.controller;

import com.ixxp.culpop.dto.StatusResponse;
import com.ixxp.culpop.dto.admin.AdminLoginRequest;
import com.ixxp.culpop.dto.admin.AdminSignupRequest;
import com.ixxp.culpop.service.AdminService;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    // 관리자 회원가입
    @PostMapping("/signup")
    public ResponseEntity<StatusResponse> signupAdmin(@RequestBody @Valid AdminSignupRequest adminSignupRequest) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.CREATED.value(), "회원가입 성공");
        adminService.signupAdmin(adminSignupRequest);
        return new ResponseEntity<>(statusResponse, HttpStatus.CREATED);
    }

    // 관리자 로그인
    @PostMapping("/login")
    public ResponseEntity<StatusResponse> loginAdmin(@RequestBody @Valid AdminLoginRequest adminLoginRequest, HttpServletResponse response) {
        StatusResponse statusResponse = new StatusResponse(HttpStatus.OK.value(), "로그인 성공");
        adminService.loginAdmin(adminLoginRequest, response);
        return new ResponseEntity<>(statusResponse, HttpStatus.OK);
    }
}
