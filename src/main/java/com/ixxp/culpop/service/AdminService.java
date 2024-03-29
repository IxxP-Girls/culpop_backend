package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.admin.AdminLoginRequest;
import com.ixxp.culpop.dto.admin.AdminSignupRequest;
import com.ixxp.culpop.entity.Admin;
import com.ixxp.culpop.entity.UserRoleEnum;
import com.ixxp.culpop.mapper.AdminMapper;
import com.ixxp.culpop.util.jwtutil.JwtUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminMapper adminMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 관리자 회원가입
    public void signupAdmin(AdminSignupRequest adminSignupRequest) {
        String email = adminSignupRequest.getEmail();
        String pwd = passwordEncoder.encode(adminSignupRequest.getPwd());

        if (adminMapper.selectEmail(email) != null) {
            throw new IllegalArgumentException("사용 중인 email 입니다.");
        }
        UserRoleEnum role = UserRoleEnum.ADMIN;
        Admin admin = new Admin(email, pwd, role);
        adminMapper.insertAdmin(admin);
    }

    // 관리자 로그인
    public void loginAdmin(AdminLoginRequest adminLoginRequest, HttpServletResponse response) {
        String email = adminLoginRequest.getEmail();
        String pwd = adminLoginRequest.getPwd();

        Admin admin = adminMapper.selectEmail(email);
        if (admin == null) {
            throw new UsernameNotFoundException("해당 email 이 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(pwd, adminMapper.selectEmail(email).getPwd())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // accessToken 생성
        String accessToken = jwtUtil.createAdminToken(email, admin.getRole());

        // Cookie 로 accessToken 반환
        ResponseCookie cookie = ResponseCookie.from("AccessToken", accessToken)
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7일
                .httpOnly(true)
                .secure(true)
                .sameSite("None")
//                .domain("culpop.shop")
                .build();
        response.addHeader("Set-Cookie", cookie.toString());
    }
}
