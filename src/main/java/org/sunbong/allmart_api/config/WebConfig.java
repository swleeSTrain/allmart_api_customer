package org.sunbong.allmart_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/v1/**")
                .allowedOrigins("http://localhost:5174")  // 프론트엔드 도메인
                .allowedMethods("GET", "POST", "PUT", "DELETE")
                .allowedHeaders("*")
                .allowCredentials(true);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        // 이미지 파일 설정 추가

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/upload/"); // 실제 업로드 경로
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/");
    }
}
