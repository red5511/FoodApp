package com.foodapp.foodapp.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;


@OpenAPIDefinition(
        info = @Info(
                description = "OpenApi documentation for Spring Security",
                termsOfService = "OpenApi spec - FoodApp",
                version = "1.0"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JTW auth description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JTW",
        in = SecuritySchemeIn.HEADER

)
public class OpenApiConfig {
}
