package com.questionnaire.db.service.impl;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.questionnaire.common.core.cache.CacheKey;
import com.questionnaire.common.core.cache.RedisCacheTool;
import com.questionnaire.common.utils.Flag;
import com.questionnaire.db.entity.Roles;
import com.questionnaire.db.entity.UserRoles;
import com.questionnaire.db.mapper.RolesMapper;
import com.questionnaire.db.mapper.UserRolesMapper;
import com.questionnaire.db.service.IRolePermissionsService;
import com.questionnaire.db.service.IUserRolesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 用户角色关系表 服务实现类
 * </p>
 *
 * @author ahui
 * @since 2022-10-18
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class UserRolesServiceImpl extends ServiceImpl<UserRolesMapper, UserRoles> implements IUserRolesService {
    private final RolesMapper rolesMapper; // 角色 mapper
    private final IRolePermissionsService rolePermissionsService; // 角色权限 service
    private final RedisCacheTool cache; // redis缓存工具

    /**
     * 绑定角色，并查询缓存中是否有权限,若没有则顺便添加权限集合到缓存中
     *
     * @param userId   用户ID
     * @param roleId 角色ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindUserRole(@NotNull String userId, @NotNull Integer roleId) {

        // 查询缓存中是否存在该用户的角色信息
        var cacheRole = (String) cache.hashGet(CacheKey.USER_ROLE_KEY, userId);
        if (StrUtil.isBlank(cacheRole)) {
            // 获取角色信息
            var roles = rolesMapper.selectOne(new LambdaQueryWrapper<Roles>()
                    .eq(Roles::getId, roleId).eq(Roles::getFlag, Flag.FLAG_NORMAL));
            // 查询是否已经绑定角色
            var userRoles = lambdaQuery()
                    .eq(UserRoles::getUserId, userId)
                    .eq(UserRoles::getRoleId, roleId).one();
            // 未绑定则进入绑定逻辑
            if (ObjectUtil.isNull(userRoles)){
                // 绑定用户角色
                userRoles = new UserRoles();
                userRoles.setUserId(userId);
                userRoles.setRoleId(roles.getId());
                var insertStatus = save(userRoles);
                if (!insertStatus) {
                    log.error(">>>>>>>>>>>>>>>>> 角色绑定失败 >>>>>>>>>>>>>>>>>");
                    return;
                }
            }
            // 已绑定则直接存入缓存
            cache.hashPut(CacheKey.USER_ROLE_KEY, userRoles.getUserId(), roles.getName());
            log.info(">>>>>>>>>>>>>>>>> 角色绑定成功 >>>>>>>>>>>>>>>>>");
            // 获取角色权限
            rolePermissionsService.bindRolePermissions(roles.getName());
        }
        //存在则不作任何操作
    }

    /**
     * 根据userId删除用户角色
     * @param userId 用户ID
     * @return boolean
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public void removeUserRoleByUserId(String userId) {
        LambdaQueryWrapper<UserRoles> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(UserRoles::getUserId, userId);
        remove(wrapper);
    }
}
