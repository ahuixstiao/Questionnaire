package com.questionnaire.gen;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import com.questionnaire.parent.ParentEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: ahui
 * @Description: 代码生成器
 * @DateTime: 2022/4/14 - 18:19
 **/
@Component
public class CodeGenerator {
    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    /**
     * 选择表进行逆向工程
     *
     * @param tableName 表名  如需全表生成则输入all 多表则用逗号分割
     */
    public void codeGenerator(String tableName) {
        FastAutoGenerator.create(url, username, password)
                // 全局配置
                .globalConfig(builder -> builder
                        // 设置作者
                        .author("ahui")
                        // 日期类型
                        .dateType(DateType.TIME_PACK)
                        // 指定生成文件的路径
                        .outputDir("../questionnaire-db/src/main/java/")
                        // 禁止生成后打开对应目录
                        .disableOpenDir()
                )
                // 包配置
                .packageConfig(builder -> builder
                        // 设置父包名
                        .parent("com.questionnaire.db")
                        // 设置.xml文件生成路径
                        .pathInfo(
                                Collections.singletonMap(
                                        OutputFile.xml, "../questionnaire-db/src/main/resources/mapper/"
                                )
                        )
                )
                // 策略配置
                .strategyConfig(builder -> builder
                        // 构建表配置
                        .addInclude(getTables(tableName)) // 需要逆向工程的表名 如全表生成则输入all
                        .addTablePrefix(getTables("q_")) //过滤表前缀

                        // 构建entity配置
                        .entityBuilder()
                        .enableLombok() // 实体类启用lombok
                        .versionColumnName("version") // 乐观锁字段名称
                        .logicDeleteColumnName("flag") // 逻辑删除字段名称
                        .logicDeletePropertyName("Integer") // 逻辑删除字段属性名
                        .naming(NamingStrategy.underline_to_camel) // 表映射实体命名策略 驼峰命名
                        .columnNaming(NamingStrategy.underline_to_camel) // 表字段映射实体命名策略 驼峰命名
                        .superClass(ParentEntity.class) // 继承父类
                        .addSuperEntityColumns( // 添加父类公共字段
                                "id",
                                "created_by", "created_time",
                                "updated_by", "updated_time",
                                "version", "flag", "remark"
                        )

                        // 构建controller配置
                        .controllerBuilder()
                        .enableRestStyle() // 生成Rest风格的Controller
                        .enableHyphenStyle() // 驼峰转连字符 _

                        // 构建mapper接口配置
                        .mapperBuilder()
                        .enableMapperAnnotation() // 使用@Mapper注解

                        // 构建完成
                        .build()
                )
                // 模板引擎
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                // 执行
                .execute();
    }

    // 处理 all 情况
    protected static List<String> getTables(String tables) {
        return "all".equals(tables) ? Collections.emptyList() : Arrays.asList(tables.split(","));
    }
}
