package com.questionnaire.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * @Author: ahui
 * @Description: 跨域配置类
 * @DateTime: 2022/4/29 - 18:31
 **/
@Configuration
public class CorsConfig {

    // 30天
    private static final long MAX_AGE = 30 * 24 * 60 * 60;

    @Bean
    public CorsFilter corsWebFilter() {
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("*"); // 设置访问源地址
        config.addAllowedHeader("*"); // 设置访问源请求头
        config.addAllowedMethod("*"); // 设置访问源请求方法
        config.setMaxAge(MAX_AGE);    // 跨域请求最大有效时长

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config); // 对接口配置跨域设置

        return new CorsFilter(source);
    }
}