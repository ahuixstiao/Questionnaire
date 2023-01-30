package com.questionnaire.db.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.questionnaire.common.core.result.FailEnum;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.common.utils.BeanConvertUtil;
import com.questionnaire.common.utils.Flag;
import com.questionnaire.db.entity.Options;
import com.questionnaire.db.entity.Question;
import com.questionnaire.db.entity.Questionnaire;
import com.questionnaire.db.entity.vo.QuestionnaireVo;
import com.questionnaire.db.mapper.QuestionnaireMapper;
import com.questionnaire.db.service.IOptionsService;
import com.questionnaire.db.service.IQuestionService;
import com.questionnaire.db.service.IQuestionnaireService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 问卷服务实现类
 *
 * @author ahui
 * @since 2022-11-02
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class QuestionnaireServiceImpl extends ServiceImpl<QuestionnaireMapper, Questionnaire> implements IQuestionnaireService {

    private final IQuestionService questionService;
    private final IOptionsService optionsService;

    /**
     * 创建问卷信息
     *
     * @param questionnaireVo 问卷内容
     * @return result
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<Object> createQuestionnaire(QuestionnaireVo questionnaireVo) {
        // 拷贝对象
        Questionnaire questionnaire = BeanConvertUtil.convert(questionnaireVo, Questionnaire.class);
        // 设置问卷作者ID与昵称
        questionnaire.setAuthor(StpUtil.getLoginId().toString());
        questionnaire.setCreatedBy(StpUtil.getExtra("nickName").toString());
        // 创建问卷
        boolean createQuestionnaire = save(questionnaire);
        if (!createQuestionnaire) {
            return Result.fail("问卷创建失败");
        }
        // 取出问题列表
        List<Question> questionList = questionnaireVo.getQuestionList()
                .stream().filter(Objects::nonNull).collect(Collectors.toList());
        // 获取回写的问卷ID并插入question实体中
        questionList.forEach(question -> question.setQuestionnaireId(questionnaire.getId()));
        // 创建问题
        questionService.saveBatch(questionList);
        // 遍历问题的选项
        questionList.forEach(question -> {
            // 给选项设置对应的问题ID
            question.getOptionsList().stream()
                    .filter(Objects::nonNull) // 过滤空元素
                    .forEach(options -> options.setQuestionId(question.getId()));
            // 创建选项
            optionsService.saveBatch(question.getOptionsList());
        });
        return Result.ok("创建成功");
    }

    /**
     * 查询问卷详情
     *
     * @param id 问卷ID
     * @return Result 问卷、问题集合、选项集合
     */
    @Override
    @Transactional(readOnly = true)
    public Result<Object> queryQuestionnaireDetails(Integer id) {
        //查询问卷
        var questionnaire = lambdaQuery()
                .eq(Questionnaire::getId, id)
                .eq(Questionnaire::getFlag, Flag.FLAG_NORMAL).one();
        if (ObjectUtil.isNull(questionnaire)) {
            return Result.fail(FailEnum.QUESTIONNAIRE_DOES_NOT_EXIST);
        }

        //查询问题
        var questionList = questionService.lambdaQuery()
                .eq(Question::getQuestionnaireId, questionnaire.getId())
                .eq(Question::getFlag, Flag.FLAG_NORMAL).list();
        if (questionList.isEmpty()) {
            return Result.fail();
        }

        //保存问题选项 key问题ID value选项
        Map<Integer, List<Options>> optionsMap = new LinkedHashMap<>();

        //查询全部选项
        var optionsList = optionsService.lambdaQuery().eq(Options::getFlag, Flag.FLAG_NORMAL).list();
        questionList.forEach(question -> {
            List<Options> collect = optionsList.stream() //用流来处理数据
                    // 根据问题ID过滤选项
                    .filter(options -> question.getId().equals(options.getQuestionId()))
                    .collect(Collectors.toList());
            optionsMap.put(question.getId(), collect);
        });

        return Result.ok(
                MapUtil.builder()
                        .put("questionnaire", questionnaire) //问卷
                        .put("questionList", questionList) // 问题
                        .put("optionsList", optionsMap) // KEY为问题的ID，Value为选项
                        .build()
        );
    }

    /**
     * 查询问卷列表
     *
     * @param currentPage 当前页
     * @param pageSize    当前页显示信息条数
     * @param flag        数据状态 0正常 1删除
     * @return Result<Object>
     */
    @Override
    @Transactional(readOnly = true)
    public Result<Object> queryQuestionnaireList(Integer currentPage, Integer pageSize, Integer flag) {
        // 获取当前用户问卷列表
        String userId = StpUtil.getLoginId().toString();
        // 分页查询列表
        Page<Questionnaire> page = new Page<>(
                Optional.ofNullable(currentPage).orElse(1),
                Optional.ofNullable(pageSize).orElse(5)
        );
        Page<Questionnaire> questionnairePage = lambdaQuery()
                .eq(Questionnaire::getAuthor, userId)
                .eq(Questionnaire::getFlag, Optional.ofNullable(flag).orElse(0)).page(page);
        // 返回
        return Result.ok(questionnairePage);
    }

    /**
     * 根据问卷ID将问卷移入/移出回收站
     *
     * @param id   问卷ID
     * @param flag 问卷数据状态 0正常 1删除
     * @return result
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public Result<Object> removeOrRecoveryQuestionnaireById(Integer id, Integer flag) {

        // 构建条件并执行修改
        boolean state = lambdaUpdate()
                .set(true, Questionnaire::getFlag, flag)
                .eq(Questionnaire::getId, id).update();

        if (state) {
            return Result.ok();
        }

        return Result.fail("操作失败");
    }

    /**
     * 根据问卷ID彻底删除问卷信息
     *
     * @param id 问卷ID
     * @return result
     */
    @Override
    public Result<Object> deleteQuestionnaireById(Integer id) {
        boolean state = removeById(id);
        if (state) {
            return Result.ok();
        }
        return Result.fail();
    }


}
