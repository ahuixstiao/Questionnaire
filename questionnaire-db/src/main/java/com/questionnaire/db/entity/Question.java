package com.questionnaire.db.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.questionnaire.parent.ParentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * TODO 问题
 *
 * @author ahui
 * @since 2022-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("q_question")
public class Question extends ParentEntity implements Serializable {

    private static final long serialVersionUID = -7832796567072884048L;

    /**
     * 问题标题
     */
    private String title;

    /**
     * 问题描述
     */
    private String description;

    /**
     * 问题的类型 1 Dropdown下拉列表、 2 SingleChoice单选、3 MultipleChoice多选
     */
    private Integer type;

    /**
     * 是否必答 0否 1是
     */
    private Integer mustAnswer;

    /**
     * 问卷ID
     */
    private Integer questionnaireId;

    /**
     *  选项集合
     */
    @TableField(exist = false) //表示该属性不是数据库字段，操作数据库时忽略该属性
    private List<Options> optionsList;

}
