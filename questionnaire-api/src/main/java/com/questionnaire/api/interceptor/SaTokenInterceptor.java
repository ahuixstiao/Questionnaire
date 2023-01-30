package com.questionnaire.api.interceptor;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: ahui
 * @Description: SaToken全局拦截器
 * @DateTime: 2022/10/31 - 18:55
 **/
@Configuration
@EnableWebMvc
public class SaTokenInterceptor implements WebMvcConfigurer {

    /**
     * 配置Sa-Token的拦截器
     * 拦截器是SpringMvc的一个标准组件，底层也是依赖于AOP切面的编程思想。
     * 拦截器工作主要是对程序员自定义拦截的路径进行一些操作。
     * 拦截器执行时机：请求前，请求后视图生成前，请求后视图生成后。
     *
     * @param registry 所有的自定义拦截器都添加到该拦截注册器中
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册注解拦截器
        registry.addInterceptor(new SaInterceptor(handler -> {
                    // 配置拦截规则
                    // 登录校验拦截
                    SaRouter
                            // /** 表示拦截全部路由
                            .match("/**")
                            // 放行路由 注册、登录、resources、验证码
                            .notMatch(excludePaths())
                            .check(r -> StpUtil.checkLogin())
                    ;
                    // 权限校验拦截 每个不同的拦截类别都需要加上不匹配的路径
                    var authPermissionsMap = getAuthPermissions();
                    for (var permission : authPermissionsMap.keySet()) {
                        SaRouter
                                // 不匹配列表 由于规则问题需要将不匹配的列表放在拦截规则前
                                .notMatch(excludePaths())
                                // 匹配列表
                                .match(permission, r -> StpUtil.checkPermissionOr(authPermissionsMap.get(permission)));
                    }
                    // 角色校验拦截
                    var authRolesMap = getAuthRoles();
                    for (var role : authRolesMap.keySet()) {
                        SaRouter
                                .notMatch(excludePaths())
                                .match(role, r -> StpUtil.checkRoleOr(authRolesMap.get(role)));
                    }
                }).isAnnotation(false) // 关闭注解鉴权，全部交由拦截器去鉴权
        ).addPathPatterns("/**");
    }


    /**
     * 配置需要忽略鉴权的path
     *
     * @return List<String>
     */
    private List<String> excludePaths() {
        // 此处暂时写死 后期可以用缓存或者保存到数据库中查询出来
        // 如果是RESTFul风格的请求需要在后面加上/** 或者路径上的占位符/{xxx}
        // 如需放行静态资源需要加上文件类型的后缀例如.jpg，或者设为.*号表示放行所有类型的文件
        return Arrays.asList(
                //接口路径
                "/auth/", "/auth/*/*/*",
                "/common/captcha/", "/common/captcha/**",
                // 静态资源路径
                "/files/*.*", "/resources/static/*.*",
                "**/swagger-ui.html", "/swagger-ui.html/**",
                "/doc.html/**", "/webjars/**", "/v3/**",
                "/swagger-resources/**", "/error", "/favicon.ico"
        );
    }

    /**
     * 动态获取鉴权规则
     *
     * @return Map<String, String>
     */
    private Map<String, String[]> getAuthPermissions() {
        // 此处暂时写死 后期可以用一个枚举类去配置或者保存到数据库中查询出来
        Map<String, String[]> permissionsMap = new LinkedHashMap<>();
        permissionsMap.put("/user/get/*", new String[]{"user:get", "admin:get", "super-admin:*"});
        permissionsMap.put("/user/list/**", new String[]{"admin:list", "super-admin:*"});
        permissionsMap.put("/user/update/", new String[]{"user:update", "admin:update", "super-admin:*"});
        permissionsMap.put("/user/delete/*", new String[]{"admin:delete", "super-admin:*"});
        permissionsMap.put("/common/file/**", new String[]{"user:upload", "admin:upload", "super-admin:*"});
        permissionsMap.put("/common/questionnaire/get/**", new String[]{"user:get", "admin:get", "super-admin:*"});
        permissionsMap.put("/common/questionnaire/get/list/**", new String[]{"user:list", "admin:list", "super-admin:*"});
        return permissionsMap;
    }

    /**
     * 动态获取角色规则
     *
     * @return Map<String, String>
     */
    private Map<String, String[]> getAuthRoles() {
        // 此处暂时写死 后期可以用一个枚举类去配置或者保存到数据库中查询出来
        Map<String, String[]> rolesMap = new LinkedHashMap<>();
        rolesMap.put("/user/**", new String[]{"user", "admin", "super-admin"});
        rolesMap.put("/common/**", new String[]{"user", "admin", "super-admin"});
        rolesMap.put("/admin/**", new String[]{"admin", "super-admin"});
        return rolesMap;
    }
}
