package com.questionnaire.api.controller.common;

import cn.hutool.core.util.StrUtil;
import com.questionnaire.common.aspect.annotation.LimitRequestFrequency;
import com.questionnaire.common.core.result.FailEnum;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.common.core.captcha.CaptchaTool;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: ahui
 * @Description: 验证码控制类
 * @DateTime: 2022/5/24 - 19:31
 **/
@Slf4j
@Tag(name = "验证码模块")
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@RestController
@RequestMapping("/common/captcha")
public class CaptchaController {

    private final CaptchaTool captchaTool;

    /**
     * 获取验证码
     *
     * @return Result 返回验证码的key和验证码图片
     */
    @LimitRequestFrequency //每1分钟超过自定义次数，则限制请求
    @GetMapping("/")
    public Result<Object> captcha() {
        // 生成并返回
        return Result.ok(captchaTool.getCaptcha());
    }

    /**
     * 校验验证码
     *
     * @param uuidKey 验证码UUID-Key 用于redis获取验证码进行校验
     * @param code    验证码
     * @return Result
     */
    @GetMapping("/{key}/{code}")
    public Result<Object> verifyCaptcha(@PathVariable("key") String key, @PathVariable("code") String code) {
        if (StrUtil.isBlank(key) || StrUtil.isBlank(code)) {
            return Result.ok(FailEnum.CAPTCHA_IS_NULL);
        }
        // 校验验证码
        if (captchaTool.checkedCaptcha(key, code)){
            return Result.ok();
        }
        return Result.fail(FailEnum.CAPTCHA_ERROR);
    }
}
