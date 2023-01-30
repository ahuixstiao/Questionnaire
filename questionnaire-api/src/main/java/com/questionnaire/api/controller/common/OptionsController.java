package com.questionnaire.api.controller.common;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 选项表 前端控制器
 * </p>
 *
 * @author ahui
 * @since 2022-11-19
 */
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@RequestMapping("/options")
public class OptionsController {

}
