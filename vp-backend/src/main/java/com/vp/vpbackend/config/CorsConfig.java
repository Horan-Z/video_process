package com.vp.vpbackend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration config = new CorsConfiguration();
        // 允许所有域名进行跨域调用
        // 允许本地开发环境（端口任意）
        config.addAllowedOriginPattern("http://localhost:*");
        // 允许所有 xiaoming10086.com 的子域名（HTTP 和 HTTPS）
        config.addAllowedOriginPattern("http://*.xiaoming10086.com");
        config.addAllowedOriginPattern("https://*.xiaoming10086.com");
        config.addAllowedOriginPattern("*");
        // 允许任何请求头
        config.addAllowedHeader("*");
        // 允许任何方法（POST、GET等）
        config.addAllowedMethod("*");
        // 允许携带凭证
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        // 对所有接口都有效
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}