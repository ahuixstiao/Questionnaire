package com.questionnaire.db.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.questionnaire.common.utils.DateUtil;
import com.questionnaire.db.entity.Question;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: ahui
 * @Description: TODO 问卷Vo
 * @DateTime: 2022/11/21 - 17:35
 **/
@Data
public class QuestionnaireVo implements Serializable {

    private static final long serialVersionUID = 9138000254724233372L;

    /**
     * 问卷标题
     */
    @Schema(name = "问卷标题")
    private String questionnaireTitle;

    /**
     * 问卷内容
     */
    @Schema(name = "问卷内容")
    private String questionnaireDescription;

    /**
     * 问卷发起者
     */
    @Schema(name = "问卷发起者")
    private String author;

    /**
     * 问卷过期时间
     */
    @JsonFormat(pattern = DateUtil.PATTERN, timezone = DateUtil.TIMEZONE)
    @Schema(name = "问卷过期时间")
    private LocalDateTime expireTime;

    /**
     * 过期状态 0否 1是
     */
    @Schema(name = "过期状态 0否 1是")
    private Integer status;

    /**
     * 类别ID
     */
    @Schema(name = "类别ID")
    private Integer categoryId;

    // 问题集合
    @Schema(name = "问题集合")
    private List<Question> questionList;

}
