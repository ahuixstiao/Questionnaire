package com.questionnaire.db.mapper;

import com.questionnaire.db.entity.Question;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 问题表 Mapper 接口
 * </p>
 *
 * @author ahui
 * @since 2022-11-19
 */
@Mapper
public interface QuestionMapper extends BaseMapper<Question> {

}
