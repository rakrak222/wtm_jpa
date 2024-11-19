package org.wtm.web.common.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.File;


@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Value("${image.base-upload-dir}")
    private String baseUploadDir;

    @Value("${image.upload-profile-dir}")
    private String uploadProfileDir;

    @Value("${image.upload-menu-dir}")
    private String uploadMenuDir;

    @Value("${image.upload-review-dir}")
    private String uploadReviewDir;

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:3000") // 프론트엔드의 주소
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Profile images
        registry.addResourceHandler("/uploads/users/**")
                .addResourceLocations("file:" + baseUploadDir + File.separator + uploadProfileDir + File.separator);

        // Menu images
        registry.addResourceHandler("/uploads/menus/**")
                .addResourceLocations("file:" + baseUploadDir + File.separator + uploadMenuDir + File.separator);

        // Review images
        registry.addResourceHandler("/uploads/reviews/**")
                .addResourceLocations("file:" + baseUploadDir + File.separator + uploadReviewDir + File.separator);
    }
}