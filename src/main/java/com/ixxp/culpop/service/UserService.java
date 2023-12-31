package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.popup.PopupResponse;
import com.ixxp.culpop.dto.user.ProfileResponse;
import com.ixxp.culpop.dto.user.ProfileUpdateRequest;
import com.ixxp.culpop.dto.user.UserLoginRequest;
import com.ixxp.culpop.dto.user.UserSignupRequest;
import com.ixxp.culpop.entity.Popup;
import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.entity.UserRoleEnum;
import com.ixxp.culpop.mapper.PopupLikeMapper;
import com.ixxp.culpop.mapper.PopupMapper;
import com.ixxp.culpop.mapper.UserMapper;
import com.ixxp.culpop.util.jwtutil.JwtUtil;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final PopupMapper popupMapper;
    private final PopupLikeMapper popupLikeMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // 회원가입
    public void signup(UserSignupRequest userSignupRequest) {
        String email = userSignupRequest.getEmail();
        String username = email.substring(0, email.indexOf("@"));
        String pwd = passwordEncoder.encode(userSignupRequest.getPwd());

        if (!userMapper.selectUserEmail(email).isEmpty()){
            throw new IllegalArgumentException("사용 중인 email 입니다.");
        }

        UserRoleEnum role = UserRoleEnum.USER;
        User user = new User(username, email, pwd, role);

        userMapper.insertUser(user);
    }

    // 로그인
    public void login(UserLoginRequest userLoginRequest, HttpServletResponse response) {
        String email = userLoginRequest.getEmail();
        String pwd = userLoginRequest.getPwd();

        User user = userMapper.selectEmail(email);

        if (user == null) {
            throw new IllegalArgumentException("해당 email 이 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(pwd, user.getPwd())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        // accessToken, refreshToken 생성
        String accessToken = jwtUtil.createAccessToken(email, user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(email, user.getRole());

        // Header 로 토큰 반환
        response.addHeader(JwtUtil.AUTHORIZATION_HEADER, accessToken);
        response.addHeader(JwtUtil.AUTHORIZATION_REFRESH, refreshToken);
    }

    // 프로필 수정
    public void updateProfile(int userId, ProfileUpdateRequest profileUpdateRequest) {
        userMapper.updateProfile(userId, profileUpdateRequest.getUsername());
    }

    // 프로필 조회
    public ProfileResponse getProfile(int userId) {
        User user = userMapper.getProfile(userId);
        return new ProfileResponse(user);
    }

    // 프로필 관심 팝업 조회
    @Transactional
    public List<PopupResponse> getProfilePopup(User user, String sort) {
        List<Popup> popups = popupMapper.selectProfilePopup(user, sort);
        List<PopupResponse> popupResponses = new ArrayList<>();
        for (Popup popup : popups) {
            org.json.JSONArray jsonArray = new org.json.JSONArray(popup.getStore().getImage());
            String image = jsonArray.getString(0);

            String fullAddress = popup.getAddress();
            String address = fullAddress.substring(0,fullAddress.indexOf(" ", fullAddress.indexOf(" ") + 1));

            String startDate = popup.getStartDate().replace("-", ".");
            String endDate = popup.getEndDate().replace("-", ".");

            boolean likeCheck = popupLikeMapper.checkPopupLike(user.getId(), popup.getId());

            popupResponses.add(new PopupResponse(popup.getId(), image, popup.getTitle(), address, startDate, endDate, likeCheck));
        }
        return popupResponses;
    }
}
