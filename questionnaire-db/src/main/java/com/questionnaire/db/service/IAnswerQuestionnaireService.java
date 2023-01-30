package com.questionnaire.db.service;

import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.entity.AnswerQuestionnaire;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 问卷填写表 服务类
 * </p>
 *
 * @author ahui
 * @since 2022-12-27
 */
public interface IAnswerQuestionnaireService extends IService<AnswerQuestionnaire> {

    /**
     * 查询用户已填问卷列表
     * @param currentPage 当前页
     * @param pageSize 页面显示条数
     * @return result
     */
    Result<Object> queryAnswerQuestionnaireList(Integer currentPage, Integer pageSize);

}
