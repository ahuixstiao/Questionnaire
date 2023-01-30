package com.questionnaire.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ahui
 * @Description: TODO OpenAPi配置
 * @DateTime: 2022/11/13 - 19:50
 **/
@Configuration
public class SwaggerConfig {

    @Bean
    public GroupedOpenApi userApi() {
        return GroupedOpenApi.builder().group("用户模块")
                .pathsToMatch("/user/**")
                .packagesToScan("com.questionnaire.api.controller.user").build();
    }

    @Bean
    public GroupedOpenApi commonApi() {
        return GroupedOpenApi.builder().group("公共模块")
                .pathsToMatch("/common/**")
                .packagesToScan("com.questionnaire.api.controller.common").build();
    }
    @Bean
    public GroupedOpenApi authApi() {
        return GroupedOpenApi.builder().group("认证模块")
                .pathsToMatch("/auth/**")
                .packagesToScan("com.questionnaire.api.controller.auth").build();
    }
    @Bean
    public GroupedOpenApi systemApi() {
        return GroupedOpenApi.builder().group("系统模块")
                .pathsToMatch("/sys/**")
                .packagesToScan("com.questionnaire.api.controller.system").build();
    }

    /**
     * 配置文档信息
     *
     * @return OpenAPI
     */
    @Bean
    public OpenAPI customOpenAPI() {
        Contact contact = new Contact()
                .name("ahui")
                .url("http://huichat.icu")
                .email("ahuixst@qq.com");
        return new OpenAPI()
                .info(new Info()
                        .contact(contact)
                        .title("在线问卷调查系统 RESTFul APIs") //标题
                        .description("在线问卷调查系统APIs文档") // 描述
                        .version("1.0")//版本
                        .license(new License().name("Apache 2.0")) // 许可证
                );

    }
}
