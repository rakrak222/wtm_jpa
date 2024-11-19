package org.wtm.web.common.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Profile images
        registry.addResourceHandler("/uploads/users/**")
                .addResourceLocations("file:" + baseUploadDir + "/" + uploadProfileDir + "/");

        // Menu images
        registry.addResourceHandler("/uploads/menus/**")
                .addResourceLocations("file:" + baseUploadDir + "/" + uploadMenuDir + "/");

        // Review images
        registry.addResourceHandler("/uploads/reviews/**")
                .addResourceLocations("file:" + baseUploadDir + "/" + uploadReviewDir + "/");

        // swagger-ui
        registry.
            addResourceHandler("/swagger-ui/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/springfox-swagger-ui/");
    }


}