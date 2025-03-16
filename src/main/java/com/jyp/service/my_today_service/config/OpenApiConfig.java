package com.jyp.service.my_today_service.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

// swagger 설정
@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        // v1.0/sign-in custom swagger 생성
        // spring security 사용으로 인한 custom swagger 생성
        Operation loginOperation = new Operation()
                .tags(Collections.singletonList("인증 API"))
                .summary("사용자 로그인")
                .description("userId와 password로 JWT 토큰을 발급받습니다.")
                .requestBody(new RequestBody()
                        .content(new Content()
                                .addMediaType("application/json", new MediaType()
                                        .schema(new Schema<>().$ref("#/components/schemas/LoginRequest")))))
                .responses(new ApiResponses()
                        .addApiResponse("200", new ApiResponse()
                                .description("성공")
                                .content(new Content()
                                        .addMediaType("application/json", new MediaType()
                                                .schema(new Schema<>().example("{\"token\": \"eyJhbGciOiJIUzI1NiJ9...\"}")))))
                        .addApiResponse("401", new ApiResponse().description("인증 실패")));

        PathItem loginPath = new PathItem()
                .post(loginOperation);

        return new OpenAPI()
                .info(new Info().title("My Today Service API").version("1.0"))
                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
                .components(new Components()
                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
                                .type(SecurityScheme.Type.HTTP)
                                .scheme("bearer")
                                .bearerFormat("JWT"))
                        .addSchemas("LoginRequest", new Schema<>()
                                .type("object")
                                .addProperty("userId", new Schema<>().type("string").example("test"))
                                .addProperty("password", new Schema<>().type("string").example("1234"))))
                .path("/v1.0/sign-in", loginPath);
    }
}
