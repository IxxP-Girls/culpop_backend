package com.ixxp.culpop.util.jwtutil;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ixxp.culpop.dto.SecurityExceptionDto;
import com.ixxp.culpop.security.AdminDetailsServiceImpl;
import com.ixxp.culpop.security.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
        // verify API 에 대해서는 필터를 실행하지 않도록 설정
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/users/verify")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = jwtUtil.getAccessToken(request);

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
        response.setContentType("application/json; charset=UTF-8");
        try {
            String json = new ObjectMapper().writeValueAsString(new SecurityExceptionDto(statusCode, msg));
            response.getWriter().write(json);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
