package com.questionnaire.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.questionnaire.parent.ParentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * TODO 选项
 *
 * @author ahui
 * @since 2022-11-19
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("q_options")
public class Options extends ParentEntity implements Serializable {

    private static final long serialVersionUID = 7241131863093789262L;

    /**
     * 选项内容
     */
    private String content;

    /**
     * 选项顺序 a-z
     */
    private String sequence;

    /**
     * 问题ID
     */
    private Integer questionId;


}
