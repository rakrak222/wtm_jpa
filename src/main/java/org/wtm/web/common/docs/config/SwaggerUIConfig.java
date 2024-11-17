package org.wtm.web.common.docs.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "Boot API 01 Project OpenAPI",
        version = "1.0",
        description = "Boot API 01 Project API documentation"
    ),
    security = @SecurityRequirement(name = "Bearer Authentication") // 모든 API에 보안 요구 사항 적용
)
@SecurityScheme(
    name = "Bearer Authentication",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
public class SwaggerUIConfig {
// @SecurityScheme와 @OpenAPIDefinition을 통해 보안 설정을 간결하게 정의
}
