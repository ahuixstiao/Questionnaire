package com.questionnaire.db.service.impl.system;

import cn.hutool.core.map.MapUtil;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.entity.system.TableInfo;
import com.questionnaire.db.mapper.system.TableInfoMapper;
import com.questionnaire.db.service.system.ITableInfoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: ahui
 * @Description: TODO 表信息ServiceImpl
 * @DateTime: 2022/12/22 - 11:52
 **/
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
public class TableInfoServiceImpl implements ITableInfoService {

    private final TableInfoMapper tableInfoMapper; // 表信息mapper

    /**
     * 查询指定数据库所有表与描述
     *
     * @param database 数据库名称
     * @return 表信息集合
     */
    @Override
    public Result<Object> queryTableInfoList(String database) {

        List<TableInfo> tableInfos = tableInfoMapper.queryTableInfoList(database);

        return Result.ok(MapUtil.builder().put("tableInfos", tableInfos).build());
    }
}
