package com.questionnaire.db.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.common.utils.DateUtil;
import com.questionnaire.db.entity.AnswerQuestionnaire;
import com.questionnaire.db.entity.Questionnaire;
import com.questionnaire.db.mapper.AnswerQuestionnaireMapper;
import com.questionnaire.db.service.IAnswerQuestionnaireService;
import com.questionnaire.db.service.IQuestionnaireService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 问卷填写表 服务实现类
 * </p>
 *
 * @author ahui
 * @since 2022-12-27
 */
@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AnswerQuestionnaireServiceImpl extends ServiceImpl<AnswerQuestionnaireMapper, AnswerQuestionnaire> implements IAnswerQuestionnaireService {

    private final IQuestionnaireService questionnaireService; // 问卷 service

    /**
     * 查询用户已填问卷列表
     *
     * @param currentPage 当前页
     * @param pageSize    页面显示条数
     * @return result
     */
    @Override
    public Result<Object> queryAnswerQuestionnaireList(Integer currentPage, Integer pageSize) {
        // 1、查询用户的已填问卷表
        List<AnswerQuestionnaire> answerQuestionnaireList = lambdaQuery()
                .eq(AnswerQuestionnaire::getUserId, StpUtil.getLoginId().toString()).list();
        // 2、取出问卷ID 查询问卷信息
        List<Integer> questionnaireIds = new LinkedList<>();
        // 使用streamAPI的map函数返回内容为问卷ID的流，随后使用forEach遍历流将id保存。
        answerQuestionnaireList.stream().map(AnswerQuestionnaire::getQuestionnaireId).forEach(questionnaireIds::add);
        // 3、查询问卷信息
        List<Questionnaire> questionnaireList = questionnaireService.lambdaQuery().in(Questionnaire::getId, questionnaireIds).list();
        // 4、取出所需信息返回至前端 标题、状态、发布者、填答时间
        List<Map<String, Object>> resultList = new LinkedList<>();
        answerQuestionnaireList.stream()
                .collect(Collectors.toConcurrentMap( //此处使用了线程安全的map转换方式
                        AnswerQuestionnaire::getQuestionnaireId,
                        AnswerQuestionnaire::getCreatedTime,
                        (oldValue, newValue) -> oldValue) // 表示遇到重复的值时返回哪个
                ).forEach((key, value) -> questionnaireList.stream()
                        // 根据用户已填问卷的ID获取问卷内容
                        .filter(questionnaire -> key.equals(questionnaire.getId()))
                        .forEach(questionnaire -> {
                            Map<String, Object> temporaryMap = new LinkedHashMap<>();
                            // 问卷标题 、问卷状态、发布者、填答时间
                            temporaryMap.put("title", questionnaire.getTitle());
                            temporaryMap.put("status", questionnaire.getStatus());
                            temporaryMap.put("author", questionnaire.getCreatedBy());
                            // 获取填写时间 由于参数脱离了元对象所以传递时并不会再被格式化需要手动格式化一次.
                            temporaryMap.put("time", LocalDateTimeUtil.format(value, DateUtil.PATTERN));
                            //保存
                            resultList.add(temporaryMap);
                        }));

        return Result.ok(resultList);
    }
}
