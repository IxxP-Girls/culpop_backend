package com.ixxp.culpop.security;

import com.ixxp.culpop.entity.User;
import com.ixxp.culpop.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final UserMapper userMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userMapper.selectEmail(email);

        if (user==null) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        return new UserDetailsImpl(user);
    }
}
