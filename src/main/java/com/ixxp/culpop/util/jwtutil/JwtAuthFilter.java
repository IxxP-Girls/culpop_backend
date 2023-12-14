package com.ixxp.culpop.util.jwtutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixxp.culpop.dto.SecurityExceptionDto;
import com.ixxp.culpop.security.AdminDetailsServiceImpl;
import com.ixxp.culpop.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AdminDetailsServiceImpl adminDetailsService;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtUtil.getAccessToken(request);
        String refreshToken = jwtUtil.getRefreshToken(request);

        if(token != null) {
            if(!jwtUtil.validateToken(token)){
                jwtExceptionHandler(response, "토큰 값이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            if (jwtUtil.isAdminToken(token)) {
                Claims info = jwtUtil.getUserInfoFromToken(token);
                setAdminAuthentication(info.getSubject());
            } else {
                Claims info = jwtUtil.getUserInfoFromToken(token);
                setAuthentication(info.getSubject());
            }
        }

        if(refreshToken != null) {
            if(!jwtUtil.validateToken(refreshToken)){
                jwtExceptionHandler(response, "토큰 값이 유효하지 않습니다", HttpStatus.UNAUTHORIZED.value());
                return;
            }
            Claims info = jwtUtil.getUserInfoFromToken(refreshToken);
            setAuthentication(info.getSubject());
        }

        filterChain.doFilter(request,response);
    }

    public void setAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    public Authentication createAuthentication(String email) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }

    // 관리자 인증 설정
    public void setAdminAuthentication(String email) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAdminAuthentication(email);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 관리자 인증 객체 생성
    public Authentication createAdminAuthentication(String email) {
        UserDetails adminDetails = adminDetailsService.loadUserByUsername(email);
        return new UsernamePasswordAuthenticationToken(adminDetails, null, adminDetails.getAuthorities());
    }

    public void jwtExceptionHandler(HttpServletResponse response, String msg, int statusCode) {
        response.setStatus(statusCode);
        response.setContentType("application/json");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
