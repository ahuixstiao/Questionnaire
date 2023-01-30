package com.questionnaire.db.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.entity.Questionnaire;
import com.questionnaire.db.entity.vo.QuestionnaireVo;

/**
 * 问卷信息服务接口
 *
 * @author ahui
 * @since 2022-11-02
 */
public interface IQuestionnaireService extends IService<Questionnaire> {

    /**
     * 创建问卷
     *
     * @param questionnaireVo 问卷内容
     * @return result
     */
    Result<Object> createQuestionnaire(QuestionnaireVo questionnaireVo);

    /**
     * 问卷详情查询
     *
     * @param id 问卷ID
     * @return Result 问卷、问题集合、选项集合
     */
    Result<Object> queryQuestionnaireDetails(Integer id);

    /**
     * 查询问卷列表
     *
     * @param currentPage 当前页
     * @param pageSize    当前页显示信息条数
     * @param flag        数据状态 0正常 1删除
     * @return Result<Object>
     */
    Result<Object> queryQuestionnaireList(Integer currentPage, Integer pageSize, Integer flag);

    /**
     * 根据问卷ID将问卷移入/移出回收站
     *
     * @param id 问卷ID
     * @param flag 问卷数据状态 0正常 1删除
     * @return result
     */
    Result<Object> removeOrRecoveryQuestionnaireById(Integer id, Integer flag);

    /**
     * 根据问卷ID彻底删除问卷信息
     * @param id 问卷ID
     * @return result
     */
    Result<Object> deleteQuestionnaireById(Integer id);

}
