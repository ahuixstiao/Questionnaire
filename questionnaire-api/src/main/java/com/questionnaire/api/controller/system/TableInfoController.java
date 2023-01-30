package com.questionnaire.api.controller.system;

import com.questionnaire.common.aspect.annotation.LimitRequestFrequency;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.service.system.ITableInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ahui
 * @Description: TODO 表信息Controller
 * @DateTime: 2022/12/22 - 11:56
 **/
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/sys/table")
public class TableInfoController {

    private final ITableInfoService tableInfoService; // 表信息Service

    /**
     * 查询表信息集合
     * @return Result<Object> 表信息集合
     */
    @LimitRequestFrequency
    @RequestMapping("/list")
    public Result<Object> queryTableInfoList(String database) {

         return tableInfoService.queryTableInfoList(database);
    }

}
