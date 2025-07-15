package com.recommend.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("推荐系统API文档")
                        .description("基于Spring Boot的推荐系统API接口文档")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("推荐系统团队")
                                .url("http://example.com")
                                .email("team@example.com"))
                        .license(new License()
                                .name("Apache License Version 2.0")
                                .url("http://www.apache.org/licenses/LICENSE-2.0")));
    }
} 