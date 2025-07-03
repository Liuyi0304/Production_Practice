package org.example.sign_language.common.config;

import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Resource
    private JwtInterceptor jwtInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(jwtInterceptor).addPathPatterns("/**")
                .excludePathPatterns("/")
                .excludePathPatterns("/login")
                .excludePathPatterns("/register")
                .excludePathPatterns("/videos/**")
                .excludePathPatterns("/picture/**")
                .excludePathPatterns("/download/**")
                .excludePathPatterns("/words_to_videos/**")
                .excludePathPatterns("/video/**")
                .excludePathPatterns("/files/**");
//                .excludePathPatterns("/capture/**");
    }
}
