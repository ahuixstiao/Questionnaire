package com.questionnaire.db.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.questionnaire.common.core.cache.CacheKey;
import com.questionnaire.common.core.cache.RedisCacheTool;
import com.questionnaire.common.utils.Flag;
import com.questionnaire.db.entity.RolePermissions;
import com.questionnaire.db.entity.Roles;
import com.questionnaire.db.mapper.PermissionsMapper;
import com.questionnaire.db.mapper.RolePermissionsMapper;
import com.questionnaire.db.mapper.RolesMapper;
import com.questionnaire.db.service.IRolePermissionsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 角色权限关系表 服务实现类
 * </p>
 *
 * @author ahui
 * @since 2022-10-18
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class RolePermissionsServiceImpl extends ServiceImpl<RolePermissionsMapper, RolePermissions> implements IRolePermissionsService {

    private final RedisCacheTool cache;
    private final RolesMapper rolesMapper;
    private final PermissionsMapper permissionsMapper;

    /**
     * 绑定角色权限
     *
     * @param roleName 角色名
     */
    @Override
    @Transactional(readOnly = true, rollbackFor = Exception.class)
    public void bindRolePermissions(@NotNull String roleName) {

        // 从缓存中获取对应角色的权限路径
        var exists = cache.hashExists(CacheKey.PERMISSION_KEY, roleName);
        // 判断是否存在权限路径 没有则查询数据库并放到缓存中
        if (!exists) {
            // 获取对应的角色信息
            var roles = rolesMapper.selectOne(new LambdaQueryWrapper<Roles>()
                    .eq(Roles::getName, roleName).eq(Roles::getFlag, Flag.FLAG_NORMAL));
            // 查询该角色的权限列表
            var rolePermissionsList = lambdaQuery().eq(RolePermissions::getRoleId, roles.getId()).list();
            List<Integer> permissionIds = new ArrayList<>();
            // 权限集合不为空则取出角色权限ID
            if (!rolePermissionsList.isEmpty()) {
                rolePermissionsList.forEach(
                        rolePermissions -> permissionIds.add(rolePermissions.getPermissionId())
                );
                // 根据权限ID查询权限路径
                var permissionsUrls = permissionsMapper.selectBatchIds(permissionIds);
                List<String> resultPermissionList = new LinkedList<>();
                // 取出权限路径
                permissionsUrls.stream()
                        .filter(permissions -> permissions.getFlag().equals(0))
                        .forEach(permissions -> resultPermissionList.add(permissions.getUrl()));
                // 缓存 用户角色的权限路径
                cache.hashPut(CacheKey.PERMISSION_KEY, roles.getName(), resultPermissionList.toString());
            } else {
                log.error(">>>>>>>>>>>>>>>>> 该 {} 角色的权限列表为空 >>>>>>>>>>>>>>>>>", roleName);
            }
        }
    }

    /**
     * 获取对应角色权限路径列表
     *
     * @param roleName 角色名称
     * @return List
     */
    public List<String> getRolePermissionsList(String roleName) {
        // 从缓存中获取对应角色的权限路径
        var exists = cache.hashExists(CacheKey.PERMISSION_KEY, roleName);
        if(exists) {
            // 从缓存中获取权限集合
            var perm = cache.hashGet(CacheKey.PERMISSION_KEY, roleName);
            // 格式化字符串
            var replace = perm.toString().replace("[", "").replace("]", "").replace(" ", "");
            return Arrays.asList(replace.split(","));
        }
        log.error("缓存中不存在");
        return Collections.emptyList();
    }
}
