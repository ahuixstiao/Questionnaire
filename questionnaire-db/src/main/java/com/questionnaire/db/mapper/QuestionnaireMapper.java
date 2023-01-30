package com.questionnaire.db.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.questionnaire.db.entity.Questionnaire;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 投票问卷表 Mapper 接口
 * </p>
 *
 * @author ahui
 * @since 2022-11-02
 */
@Mapper
public interface QuestionnaireMapper extends BaseMapper<Questionnaire> {

}
