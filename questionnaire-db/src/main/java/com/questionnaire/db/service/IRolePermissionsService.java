package com.questionnaire.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.questionnaire.db.entity.RolePermissions;

import java.util.List;

/**
 * <p>
 * 角色权限关系表 服务类
 * </p>
 *
 * @author ahui
 * @since 2022-10-18
 */
public interface IRolePermissionsService extends IService<RolePermissions> {

    /**
     * 绑定角色权限
     * @param roleName 角色名
     */
    void bindRolePermissions(String roleName);

    /**
     * 获取对应角色权限路径列表
     *
     * @param roleName 角色名称
     * @return List
     */
    List<String> getRolePermissionsList(String roleName);
}
