package com.questionnaire.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * TODO 角色权限关系
 *
 * @author ahui
 * @since 2022-10-18
 */
@Data
@TableName("q_role_permissions")
public class RolePermissions implements Serializable {

    private static final long serialVersionUID = 2562414449729541030L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色ID
     */
    private Integer roleId;

    /**
     * 权限ID
     */
    private Integer permissionId;

}
