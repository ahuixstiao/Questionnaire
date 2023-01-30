package com.questionnaire.db.service.impl;

import com.questionnaire.db.entity.Question;
import com.questionnaire.db.mapper.QuestionMapper;
import com.questionnaire.db.service.IQuestionService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问题表 服务实现类
 * </p>
 *
 * @author ahui
 * @since 2022-11-19
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question> implements IQuestionService {

}
