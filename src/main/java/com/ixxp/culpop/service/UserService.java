package com.ixxp.culpop.service;

import com.ixxp.culpop.dto.user.UserSignupRequest;
import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    // 회원가입
    public void signup(UserSignupRequest userSignupRequest) {
        String email = userSignupRequest.getEmail();
        String username = email.substring(0, email.indexOf("@"));
        String pwd = passwordEncoder.encode(userSignupRequest.getPwd());

        if (!userMapper.selectUserEmail(email).isEmpty()){
            throw new IllegalArgumentException("사용 중인 email 입니다.");
        }

        User user = new User(username, email, pwd);
        userMapper.insertUser(user);
    }
}
