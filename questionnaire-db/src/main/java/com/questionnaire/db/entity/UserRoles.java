package com.questionnaire.db.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * TODO 用户角色关系
 *
 * @author ahui
 * @since 2022-10-18
 */
@Data
@TableName("q_user_roles")
public class UserRoles implements Serializable {

    private static final long serialVersionUID = 8033135533449914229L;
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 角色ID
     */
    private Integer roleId;
}
