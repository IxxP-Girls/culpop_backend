package com.ixxp.culpop.util.jwtutil;

import com.ixxp.culpop.entity.UserRoleEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.annotation.PostConstruct;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.util.WebUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtUtil {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_KEY = "auth";
//    public static final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 1시간
    public static final long ACCESS_TOKEN_TIME = 7* 24 * 60 * 60 * 1000L; // 7일

    @Value("${jwt.secret.key}")
    private String secretKey;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secretKey);
        key = Keys.hmacShaKeyFor(bytes);
    }

    // header Access 토큰을 가져오기
    public String getAccessToken(HttpServletRequest request) {
        String accessToken = null;
        Cookie cookie = WebUtils.getCookie(request, "AccessToken");
        if (cookie != null)
            accessToken = cookie.getValue();
        return accessToken;
    }

    // 토큰 생성
    public String createToken(String email, UserRoleEnum role, long TOKEN_TIME, String prefix) {
        Date date = new Date();

        return prefix +
                Jwts.builder()
                        .setSubject(email)
                        .claim(AUTHORIZATION_KEY, role)
                        .setExpiration(new Date(date.getTime() + TOKEN_TIME))
                        .setIssuedAt(date)
                        .signWith(key, signatureAlgorithm)
                        .compact();
    }

    //accessToken 생성
    public String createAccessToken(String email, UserRoleEnum role) {
        return createToken(email, role, ACCESS_TOKEN_TIME, "");
    }

    // admin token 생성
    public String createAdminToken(String email, UserRoleEnum role) {
        return createToken(email, role, ACCESS_TOKEN_TIME, "");
    }
    // 토큰 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT token, 만료된 JWT token 입니다.");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
        } catch (IllegalArgumentException e) {
            log.info("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }

    // 관리자 토큰 확인
    public boolean isAdminToken(String token) {
        Claims claims;
        if (token != null) {
            if (validateToken(token)) {
                claims = getUserInfoFromToken(token);
                return ((claims.get("auth").toString()).equals("ADMIN"));
            } else {
                throw new SecurityException();
            }
        }
        throw new SecurityException();
    }
}
