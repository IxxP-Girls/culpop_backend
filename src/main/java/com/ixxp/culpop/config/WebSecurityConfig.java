package com.ixxp.culpop.config;

import com.ixxp.culpop.security.AdminDetailsServiceImpl;
import com.ixxp.culpop.security.UserDetailsServiceImpl;
import com.ixxp.culpop.util.jwtutil.JwtAuthFilter;
import com.ixxp.culpop.util.jwtutil.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
public class WebSecurityConfig implements WebMvcConfigurer {
    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AdminDetailsServiceImpl adminDetailsService;

    // 패스워드 암호화
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);

        // 기본 설정인 Session 방식은 사용하지 않고 JWT 방식을 사용하기 위한 설정
        http.sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
        .authorizeHttpRequests(
                authorize -> authorize
                                .antMatchers("/users/signup").permitAll()
                                .antMatchers("/users/login").permitAll()
                                .antMatchers("/admin/signup").permitAll()
                                .antMatchers("/admin/login").permitAll()
                                .antMatchers(HttpMethod.GET,"/popup/**").permitAll()
                                .antMatchers(HttpMethod.GET,"/posts/**").permitAll()
                        .anyRequest().authenticated()
        );

        http.addFilterBefore(new JwtAuthFilter(jwtUtil, userDetailsService, adminDetailsService), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS", "HEAD")
                .allowedOrigins("http://localhost:8080", "http://localhost:5173", "http://43.202.13.38:8081/")
                .exposedHeaders("Authorization", "RefreshToken");
    }
}
