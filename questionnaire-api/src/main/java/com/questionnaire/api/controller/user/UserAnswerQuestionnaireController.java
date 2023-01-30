package com.questionnaire.api.controller.user;

import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.service.IAnswerQuestionnaireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 用户问卷填写
 *
 * @author ahui
 * @since 2022-12-27
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping("/user/answer")
public class UserAnswerQuestionnaireController {

    private final IAnswerQuestionnaireService answerQuestionnaireService; // 问卷填写service

    /**
     * 查询填答的问卷列表
     * @param currentPage 当前页
     * @param pageSize 页面显示条数
     * @return result
     */
    @GetMapping("/list/{currentPage}/{pageSize}")
    public Result<Object> queryUserAnswerQuestionnaireList(
            @PathVariable Integer currentPage,
            @PathVariable Integer pageSize
    ) {

        return answerQuestionnaireService.queryAnswerQuestionnaireList(currentPage, pageSize);
    }

}
