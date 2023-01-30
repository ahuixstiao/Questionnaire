package com.questionnaire.db.service.impl;

import com.questionnaire.db.entity.Category;
import com.questionnaire.db.mapper.CategoryMapper;
import com.questionnaire.db.service.ICategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 问卷类别 服务实现类
 * </p>
 *
 * @author ahui
 * @since 2022-10-14
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements ICategoryService {

}
