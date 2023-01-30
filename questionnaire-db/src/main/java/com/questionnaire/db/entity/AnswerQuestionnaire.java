package com.questionnaire.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.questionnaire.parent.ParentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * TODO 问卷填写
 *
 * @author ahui
 * @since 2022-12-27
 */

@Data
@EqualsAndHashCode(callSuper = true)
@TableName("q_answer_questionnaire")
public class AnswerQuestionnaire extends ParentEntity implements Serializable {

    private static final long serialVersionUID = -3184270618607098514L;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 问卷ID
     */
    private Integer questionnaireId;

    /**
     * 问题ID
     */
    private Integer questionId;

    /**
     * 单个或多个选项ID
     */
    private String optionsId;


}
