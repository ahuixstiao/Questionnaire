package com.questionnaire.api.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ahui
 * @Description: 首页内容控制器
 * @DateTime: 2022/11/7 - 15:31
 **/
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@RequestMapping("common/index/")
public class IndexController {


}
