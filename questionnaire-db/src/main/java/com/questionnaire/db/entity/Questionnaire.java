package com.questionnaire.db.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.questionnaire.common.utils.DateUtil;
import com.questionnaire.parent.ParentEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TODO 投票问卷
 *
 * @author ahui
 * @since 2022-11-02
 */
@Data
@EqualsAndHashCode(callSuper = true)
@TableName("q_questionnaire")
public class Questionnaire extends ParentEntity implements Serializable {

    private static final long serialVersionUID = -4831739706073846523L;

    /**
     * 问卷标题
     */
    //@IndexField(fieldType = FieldType.KEYWORD, fieldData = true)
    private String title;

    /**
     * 问卷内容
     */
    //@IndexField(fieldType = FieldType.KEYWORD, fieldData = true)
    private String description;

    /**
     * 问卷发起者
     */
    //@IndexField(fieldType = FieldType.KEYWORD, fieldData = true)
    private String author;

    /**
     * 问卷过期时间
     */
    @JsonFormat(pattern = DateUtil.PATTERN, timezone = DateUtil.TIMEZONE)
    private LocalDateTime expireTime;

    /**
     * 问卷状态 0 正常 1 过期 2 用户主动停止答题
     */
    //@IndexField(fieldType = FieldType.INTEGER)
    private Integer status;

    /**
     * 类别ID
     */
    //@IndexField(fieldType = FieldType.INTEGER)
    private Integer categoryId;


}
