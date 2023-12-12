package com.ixxp.culpop.security;

import com.ixxp.culpop.entity.Admin;
import com.ixxp.culpop.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminDetailsServiceImpl implements UserDetailsService {
    private final AdminMapper adminMapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminMapper.selectEmail(email);

        if (admin == null) {
            throw new UsernameNotFoundException("관리자를 찾을 수 없습니다.");
        }

        return new AdminDetailsImpl(admin);
    }
}
