package com.questionnaire.common.core.captcha;

import cn.hutool.captcha.CaptchaUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.questionnaire.common.core.cache.CacheKey;
import com.questionnaire.common.core.cache.RedisCacheTool;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@Component
public class CaptchaTool {

    private final RedisCacheTool cache;

    /**
     * 获取验证码
     *
     * @return Result<Object>
     */
    public Map<Object, Object> getCaptcha() {
        // 生成验证码的UUID-Key 用以用户登陆时到Redis中获取验证码
        var uuidKey = IdUtil.simpleUUID();
        //定义图形验证码的长和宽
        var lineCaptcha = CaptchaUtil.createLineCaptcha(110, 40);
        var byteArrayOutputStream = new ByteArrayOutputStream();
        // 输出验证码，可以写出到文件也可以写出到流，这里选择写入到流中可以不用生成文件在本地
        // 另外write内部已经帮我们关闭了流所以无需再手动去关闭
        lineCaptcha.write(byteArrayOutputStream);

        //记录验证码
        log.info("key: {}, captcha: {}", uuidKey, lineCaptcha.getCode());

        //验证生成的图形验证码是否有效
        var verify = lineCaptcha.verify(lineCaptcha.getCode());
        if (verify) {
            //存入缓存 1分钟过期
            cache.hashPut(CacheKey.CAPTCHA_KEY, uuidKey, lineCaptcha.getCode(), 60);
            //成功时返回
            return MapUtil.builder()
                    .put("key", uuidKey)
                    //将图片的字节数组转成base64字符串返回
                    .put("image", StrUtil.format("data:image/jpeg;base64,{}", Base64.encodeBase64String(byteArrayOutputStream.toByteArray())))
                    .build();
        }
        //无效时返回
        log.error(">>>>>>>>>>>>>>>>> 生成失败 >>>>>>>>>>>>>>>>>");
        return MapUtil.builder().build();
    }

    /**
     * 校验验证码
     *
     * @param key redis缓存key
     * @param code    验证码
     * @return Result<Object>
     */
    public boolean checkedCaptcha(String key, String code) {
        if (StrUtil.isBlank(key) || StrUtil.isBlank(code)) {
            return false;
        }
        //从缓存中获取验证码
        var captcha = (String) cache.hashGet(CacheKey.CAPTCHA_KEY, key);
        //判断验证码是否过期 null则表示已过期
        if (StrUtil.isBlank(captcha)) {
            log.error(">>>>>>>>>>>>>>>>> 验证码已过期 >>>>>>>>>>>>>>>>>");
            return false;
        }
        //判断验证码是否正确
        return StrUtil.equals(captcha, code);
    }


}
