package com.questionnaire.common.config;

import cn.dev33.satoken.jwt.StpLogicJwtForSimple;
import cn.dev33.satoken.stp.StpLogic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ahui
 * @Description: sa-token配置
 * @DateTime: 2022/4/29 - 19:40
 **/
@Configuration
public class SaTokenConfig {

    /**
     * SaToken简单整合Jwt
     *
     * @return StpLogic
     */
    @Bean
    public StpLogic getStpLogicJwt() {
        return new StpLogicJwtForSimple();
    }

}
