package com.questionnaire.api.controller.system;

import com.questionnaire.common.core.result.Result;
import com.questionnaire.gen.CodeGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @Author: ahui
 * @Description: 代码生成控制器
 * @DateTime: 2022/9/22 - 09:58
 **/
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@RestController
@RequestMapping("/sys/generator")
public class GeneratorController {

    private final CodeGenerator codeGenerator;

    /**
     * 逆向工程
     *
     * @param parameter 逆向工程参数
     * @return Result
     */
    @PostMapping("/constructor")
    public Result<Object> generator(@RequestBody Map<String, Object> parameter) {

        // 表名 多个逗号分割
        String tableName = parameter.get("tableName").toString();

        //执行
        codeGenerator.codeGenerator(tableName);

        return Result.ok();
    }

}
