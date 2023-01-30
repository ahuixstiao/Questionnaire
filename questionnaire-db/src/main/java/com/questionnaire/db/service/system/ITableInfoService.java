package com.questionnaire.db.service.system;

import com.questionnaire.common.core.result.Result;

/**
 * @Author: ahui
 * @Description: TODO 表信息Service
 * @DateTime: 2022/12/22 - 11:50
 **/
public interface ITableInfoService {

    /**
     * 查询指定数据库所有表与描述
     * @param database 数据库名称
     * @return 表信息集合
     */
    Result<Object> queryTableInfoList(String database);

}
