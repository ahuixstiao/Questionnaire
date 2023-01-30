package com.questionnaire.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.questionnaire.db.entity.UserRoles;

/**
 * <p>
 * 用户角色关系表 服务类
 * </p>
 *
 * @author ahui
 * @since 2022-10-18
 */
public interface IUserRolesService extends IService<UserRoles> {

    /**
     * 绑定用户角色，并查询缓存中是否有权限
     * @param userId 用户ID
     * @param roleId 角色ID
     */
    void bindUserRole(String userId, Integer roleId);

    void removeUserRoleByUserId(String userId);
}
