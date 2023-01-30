package com.questionnaire.db.mapper;

import com.questionnaire.db.entity.UserRoles;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户角色关系表 Mapper 接口
 * </p>
 *
 * @author ahui
 * @since 2022-10-18
 */
@Mapper
public interface UserRolesMapper extends BaseMapper<UserRoles> {

}
