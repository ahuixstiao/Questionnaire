package com.questionnaire.api.controller.user;

import cn.hutool.core.util.ObjectUtil;
import com.questionnaire.common.core.result.FailEnum;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.entity.vo.QuestionnaireVo;
import com.questionnaire.db.service.IQuestionnaireService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 用户问卷
 *
 * @author ahui
 * @since 2022-11-02
 */
@Tag(name = "问卷模块")
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@RestController
@RequestMapping("/user/questionnaire")
public class UserQuestionnaireController {

    private final IQuestionnaireService questionnaireService; // 问卷 service

    /**
     * 创建问卷
     *
     * @param questionnaireVo 问卷内容
     * @return result
     */
    @Operation(summary = "问卷创建")
    @Parameter(name = "questionnaireVo", description = "问卷实体，里面包含了问题和选项的集合")
    @PostMapping(value = "/save")
    public Result<Object> saveQuestionnaire(@RequestBody QuestionnaireVo questionnaireVo) {

        if (ObjectUtil.isNull(questionnaireVo)) {
            return Result.fail(FailEnum.PARAMETER_IS_NULL);
        }

        return questionnaireService.createQuestionnaire(questionnaireVo);
    }

    /**
     * 获取问卷详情
     *
     * @param id 问卷ID
     * @return Result
     */
    @Operation(summary = "问卷详情", description = "用户访问问卷时需要通过该接口返回")
    @GetMapping("/get/{id}")
    public Result<Object> queryQuestionnaireDetails(@PathVariable @NotNull Integer id) {
        return questionnaireService.queryQuestionnaireDetails(id);
    }

    /**
     * 查询问卷列表
     *
     * @param currentPage 当前页
     * @param pageSize    每页显示条数
     * @return page
     */
    @Operation(summary = "查询问卷列表")
    @GetMapping("/list/{flag}/{currentPage}/{pageSize}")
    public Result<Object> queryQuestionnaireList(
            @PathVariable Integer flag,
            @PathVariable(required = false) Integer currentPage,
            @PathVariable(required = false) Integer pageSize
    ) {
        return questionnaireService.queryQuestionnaireList(currentPage, pageSize, flag);
    }

    /**
     * 根据问卷ID将问卷移入/移出回收站
     *
     * @param id 问卷ID
     * @return result
     */
    @Operation(summary = "移入/移出回收站")
    @PutMapping("/operation/{id}/{flag}")
    public Result<Object> recoveryQuestionnaireInfoById(
            @PathVariable @NotNull Integer id,
            @PathVariable @NotNull Integer flag) {

        return questionnaireService.removeOrRecoveryQuestionnaireById(id, flag);
    }

    /**
     * 根据问卷ID将问卷彻底删除
     *
     * @param id 问卷ID
     * @return result
     */
    @Operation(summary = "删除问卷")
    @DeleteMapping("/delete/{id}")
    public Result<Object> removeQuestionnaireInfoById(@PathVariable @NotNull Integer id) {

        return questionnaireService.deleteQuestionnaireById(id);
    }

}
