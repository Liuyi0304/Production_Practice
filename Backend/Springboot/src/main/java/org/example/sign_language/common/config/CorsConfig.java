package org.example.sign_language.common.config;

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
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
//        config.setAllowCredentials(true);
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }
}
