package com.questionnaire.db.handler;

import cn.dev33.satoken.session.SaSessionCustomUtil;
import cn.dev33.satoken.stp.StpInterface;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.questionnaire.common.core.cache.CacheKey;
import com.questionnaire.common.core.cache.RedisCacheTool;
import com.questionnaire.db.entity.UserRoles;
import com.questionnaire.db.service.IRolePermissionsService;
import com.questionnaire.db.service.IUserRolesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @Author: ahui
 * @Description: 权限验证处理器
 * @DateTime: 2022/10/18 - 08:50
 **/
@Slf4j
// 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class PermissionAuthenticatingHandler implements StpInterface {

    private final RedisCacheTool cache; // redis工具
    private final IRolePermissionsService rolePermissionsService; // 角色权限 service
    private final IUserRolesService userRolesService; // 用户角色 service

    /**
     * 返回指定账号id所拥有的权限码集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的权限码集合
     */
    @Override
    public List<String> getPermissionList(Object loginId, String loginType) {
        // 1. 声明权限码集合
        List<String> permissionList = new ArrayList<>();
        // 2. 遍历角色列表，查询拥有的权限码
        for (var roleName : getRoleList(loginId, loginType)) {
            // 获取角色权限的session会话
            var roleSession = SaSessionCustomUtil.getSessionById("role: " + roleName);
            List<String> temporaryList = roleSession.get("rolePermissionList", () -> {
                // 判断缓存是否存在
                var exists =  cache.hashExists(CacheKey.PERMISSION_KEY, roleName);
                // 缓存中不存在时获取角色权限集合
                if(!exists) {
                    // 绑定角色的权限路径
                    rolePermissionsService.bindRolePermissions(roleName);
                }
                // 获取并返回角色权限路径列表
                return rolePermissionsService.getRolePermissionsList(roleName);
            });
            // 保存角色权限路径列表集合
            permissionList.addAll(temporaryList);
        }
        /*
         * 3、返回权限集合 需要注意的是框架会将角色的权限保存在custom项下对应的角色的缓存中.
         */
        return permissionList;
    }

    /**
     * 返回指定账号id所拥有的角色标识集合
     *
     * @param loginId   账号id
     * @param loginType 账号类型
     * @return 该账号id具有的角色标识集合
     */
    @Override
    public List<String> getRoleList(Object loginId, String loginType) {

        // 通过loginId去获取当前用户的会话
        var saSession = StpUtil.getSessionByLoginId(loginId);
        return saSession.get("roleList", ()-> {
            // 先从缓存中获取用户角色 为空则执行用户角色绑定逻辑
            var roleName = (String) cache.hashGet(CacheKey.USER_ROLE_KEY, loginId.toString());
            if (StrUtil.isBlank(roleName)){
                // 根据用户ID获取用户角色
                var userRoles = userRolesService.lambdaQuery().eq(UserRoles::getUserId, loginId).one();
                userRolesService.bindUserRole(loginId.toString(), userRoles.getRoleId());
                // 从缓存中获取用户角色
                roleName = cache.hashGet(CacheKey.USER_ROLE_KEY, loginId.toString()).toString();
            }
            return Collections.singletonList(roleName);
        });
    }
}
