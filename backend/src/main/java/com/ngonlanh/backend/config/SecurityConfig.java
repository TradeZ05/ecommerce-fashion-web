package com.ngonlanh.backend.config;

import org.springframework.beans.factory.annotation.Autowired; // THÊM IMPORT
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy; // THÊM IMPORT
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter; // THÊM IMPORT

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Gọi Filter kiểm tra JWT vào đây
    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable()) 
            
            // THAY ĐỔI: Tắt Session (vì JWT tự mang theo thông tin rồi, server không cần nhớ)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // 2. THAY ĐỔI LUẬT BẢO MẬT
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/**").permitAll() // Mở cửa cho /api/auth/register và /api/auth/login
                .anyRequest().authenticated() // CÁC API KHÁC BẮT BUỘC PHẢI CÓ TOKEN MỚI ĐƯỢC VÀO
            );

        // 3. Đặt JWT Filter đứng chặn ở cửa trước
        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}