package com.questionnaire.db.mapper;

import com.questionnaire.db.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 投票类别 Mapper 接口
 * </p>
 *
 * @author ahui
 * @since 2022-10-14
 */
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}
