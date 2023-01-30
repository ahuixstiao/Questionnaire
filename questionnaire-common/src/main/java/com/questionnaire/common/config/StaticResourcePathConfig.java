package com.questionnaire.common.config;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.ResourceUtils;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @Author: ahui
 * @Description: TODO 静态资源路径配置类
 * @DateTime: 2022/11/11 - 20:50
 **/
@Slf4j
@Configuration
public class StaticResourcePathConfig implements WebMvcConfigurer {

    private static final String FOLDER_PATH = "/files/";

    /**
     * Override this method to add resource handlers for serving static resources.
     *
     * @param registry
     * @see ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ApplicationHome applicationHome = new ApplicationHome();
        //File:/Users/ahui/WorkSpace/java_project/Questionnaire  /files/
        String property = StrUtil.format("File:{}{}",
                applicationHome.getDir().getParentFile().getAbsolutePath(), //项目绝对路径
                FOLDER_PATH);
        // 自定义静态资源路径为：http://服务器IP:8080/files/xxx.jpg
        // 为静态资源添加资源处理。调用处理程序 对于与指定的URL路径模式之一匹配的请求。
        registry.addResourceHandler(StrUtil.format("{}**", FOLDER_PATH))
                // 添加一个或多个用于提供静态内容的资源位置
                .addResourceLocations(property);

        // 类路径配置 http://服务器IP:8080/**
        registry.addResourceHandler("/**")
                .addResourceLocations(ResourceUtils.CLASSPATH_URL_PREFIX + "/");

        // 资源路径配置
        registry.addResourceHandler("/doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    public static void main(String[] args) {
        // /Users/ahui/WorkSpace/java_project/Questionnaire
        System.out.println(System.getProperty("user.dir"));
    }

}
