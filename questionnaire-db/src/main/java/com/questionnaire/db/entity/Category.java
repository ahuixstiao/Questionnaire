package com.questionnaire.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.questionnaire.parent.ParentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * TODO 问卷类别
 *
 * @author ahui
 * @since 2022-10-14
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("q_category")
public class Category extends ParentEntity implements Serializable {

    private static final long serialVersionUID = -4430768505049354634L;

    /**
     * 类别名称
     */
    private String categoryName;
}