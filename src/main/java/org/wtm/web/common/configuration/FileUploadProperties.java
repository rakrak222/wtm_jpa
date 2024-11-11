package org.wtm.web.common.configuration;

import java.io.File;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "image")
@Setter
public class FileUploadProperties {

    // Setters for ConfigurationProperties
    private String baseUploadDir;
    private String uploadProfileDir;
    private String uploadMenuDir;
    private String uploadReviewDir;

    // Getter methods with OS-specific paths
    public String getBaseUploadDir() {
        return convertToOsSpecificPath(baseUploadDir);
    }

    public String getUploadProfileDir() {
        return File.separator + convertToOsSpecificPath(uploadProfileDir);
    }

    public String getUploadMenuDir() {
        return File.separator + convertToOsSpecificPath(uploadMenuDir);
    }

    public String getUploadReviewDir() {
        return File.separator + convertToOsSpecificPath(uploadReviewDir);
    }

    // Convert method
    private String convertToOsSpecificPath(String path) {
        return path.replace("/", File.separator);
    }

}