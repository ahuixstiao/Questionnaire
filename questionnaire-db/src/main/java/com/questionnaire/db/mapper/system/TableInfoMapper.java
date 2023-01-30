package com.questionnaire.db.mapper.system;

import com.questionnaire.db.entity.system.TableInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: ahui
 * @Description: TODO 表信息Mapper
 * @DateTime: 2022/12/22 - 11:39
 **/
@Mapper
public interface TableInfoMapper {

    /**
     * 查询指定数据库所有表与描述
     * @param database 数据库名称
     * @return 表信息集合
     */
    List<TableInfo> queryTableInfoList(String database);
}
