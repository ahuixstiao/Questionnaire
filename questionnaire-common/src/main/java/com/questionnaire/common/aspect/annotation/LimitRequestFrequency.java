package com.questionnaire.common.aspect.annotation;

import java.lang.annotation.*;

/**
 * @Author: ahui
 * @Description: 请求频率限制
 * @DateTime: 2022/5/24 - 22:12
 **/
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface LimitRequestFrequency {
    // 指定时间 (单位 秒)
    long seconds() default 10;
    // 指定时间内可请求的次数
    int count() default 5;
}
