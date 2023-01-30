package com.questionnaire.db.service.impl;

import com.questionnaire.db.entity.Permissions;
import com.questionnaire.db.mapper.PermissionsMapper;
import com.questionnaire.db.service.IPermissionsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 权限表 服务实现类
 * </p>
 *
 * @author ahui
 * @since 2022-10-18
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class PermissionsServiceImpl extends ServiceImpl<PermissionsMapper, Permissions> implements IPermissionsService {

}
