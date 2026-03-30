package com.ngonlanh.backend.config;

import com.ngonlanh.backend.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        try {
            // 1. Lấy JWT từ request
            String jwt = getJwtFromRequest(request);

            // 2. Nếu request có JWT và JWT đó hợp lệ
            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                // Lấy username từ chuỗi JWT
                String username = tokenProvider.getUsernameFromJWT(jwt);

                // Lấy thông tin người dùng từ DB
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);
                
                // Cài đặt thông tin xác thực vào SecurityContext (Phiên làm việc)
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception ex) {
            logger.error("Không thể thiết lập xác thực người dùng trong security context", ex);
        }

        // 3. Cho phép request đi tiếp tới Controller
        filterChain.doFilter(request, response);
    }

    // Hàm hỗ trợ bóc tách chuỗi Token (bỏ đi tiền tố "Bearer ")
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        // Kiểm tra xem header Authorization có chứa thông tin jwt không
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}