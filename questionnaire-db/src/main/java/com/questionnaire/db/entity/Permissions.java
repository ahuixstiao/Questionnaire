package com.questionnaire.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.questionnaire.parent.ParentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * TODO 权限
 *
 * @author ahui
 * @since 2022-10-18
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("q_permissions")
public class Permissions extends ParentEntity implements Serializable {

    private static final long serialVersionUID = -1962493415757535073L;

    /**
     * 权限
     */
    private String name;

    /**
     * 权限路径 user:select user:save
     */
    private String url;

    /**
     * 权限类型 例如 button 或 menu
     */
    private String type;

    /**
     * 父节点 属于谁的权限 0超级管理员 1管理员 2用户
     */
    private Integer pid;

}
