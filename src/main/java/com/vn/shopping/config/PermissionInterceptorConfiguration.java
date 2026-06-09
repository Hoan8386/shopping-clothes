package com.vn.shopping.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/", "/api/v1/auth/**",
                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                "/api/v1/san-pham",
                "/api/v1/san-pham/**",
                "/api/v1/chi-tiet-san-pham",
                "/api/v1/chi-tiet-san-pham/**",
                "/api/v1/bo-suu-tap",
                "/api/v1/bo-suu-tap/**",
                "/api/v1/kieu-san-pham",
                "/api/v1/kieu-san-pham/**",
                "/api/v1/thuong-hieu",
                "/api/v1/thuong-hieu/**",
                "/api/v1/mau-sac",
                "/api/v1/mau-sac/**",
                "/api/v1/kich-thuoc",
                "/api/v1/kich-thuoc/**",
                "/api/v1/danh-gia-san-pham",
                "/api/v1/danh-gia-san-pham/**",
                "/api/v1/data-ai/*",
                "/notification/*"
        };
        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);
    }
}
