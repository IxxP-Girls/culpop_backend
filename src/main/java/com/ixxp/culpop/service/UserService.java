package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.popup.PopupResponse;
import com.ixxp.culpop.dto.post.PostList;
import com.ixxp.culpop.dto.post.PostResponse;
import com.ixxp.culpop.dto.user.ProfileResponse;
import com.ixxp.culpop.dto.user.ProfileUpdateRequest;
import com.ixxp.culpop.dto.user.UserLoginRequest;
import com.ixxp.culpop.dto.user.UserSignupRequest;
import com.ixxp.culpop.entity.Popup;
import com.ixxp.culpop.entity.Post;
import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.entity.UserRoleEnum;
import com.ixxp.culpop.mapper.PopupMapper;
import com.ixxp.culpop.mapper.PostMapper;
import com.ixxp.culpop.mapper.UserMapper;
import com.ixxp.culpop.util.jwtutil.JwtUtil;

import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final PopupMapper popupMapper;
    private final PostMapper postMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final PopupService popupService;

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
            throw new UsernameNotFoundException("해당 email 이 존재하지 않습니다.");
        }

        if (!passwordEncoder.matches(pwd, user.getPwd())) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // accessToken 생성
        String accessToken = jwtUtil.createAccessToken(email, user.getRole());

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

    // 프로필 수정
    public void updateProfile(int userId, ProfileUpdateRequest profileUpdateRequest) {
        userMapper.updateProfile(userId, profileUpdateRequest.getUsername());
    }

    // 프로필 조회
    public ProfileResponse getProfile(int userId, int page) {
        int offset = (page - 1) * 5;
        User user = userMapper.getProfile(userId);

        List<Post> posts = postMapper.selectPostByUserId(userId, offset);
        List<PostList> postList = posts.stream().map(post ->
                new PostList(post.getId(), post.getUser().getUsername(), post.getTitle(), post.getCategory().getCateName(), postMapper.selectPostViewCount(post.getId()), post.getCreatedAt())
        ).collect(Collectors.toList());

        int total = postMapper.selectUserPostCount(userId);
        PostResponse postResponse = new PostResponse(postList, total);

        return new ProfileResponse(user, postResponse);
    }

    // 프로필 관심 팝업 조회
    @Transactional
    public List<PopupResponse> getProfilePopup(User user, String sort, int page) {
        int offset = (page - 1) * 10;
        List<Popup> popups = popupMapper.selectProfilePopup(user, sort, offset);
        return popupService.convertToPopupResponseList(user,popups);
    }
}
