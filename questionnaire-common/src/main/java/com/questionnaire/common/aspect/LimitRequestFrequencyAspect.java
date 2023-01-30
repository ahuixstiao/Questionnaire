package com.questionnaire.common.aspect;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.questionnaire.common.aspect.annotation.LimitRequestFrequency;
import com.questionnaire.common.core.cache.CacheKey;
import com.questionnaire.common.core.cache.RedisCacheTool;
import com.questionnaire.common.core.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author: ahui
 * @Description: 请求频率限制
 * @DateTime: 2022/5/24 - 22:17
 **/
@Slf4j
@Aspect
@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LimitRequestFrequencyAspect {
    private final RedisCacheTool cache;

    /**
     * 定义切点 使用注解的方法都会被增强
     */
    @Pointcut("@annotation(limitRequestFrequency)")
    public void pointCut(LimitRequestFrequency limitRequestFrequency) {
    }

    /**
     * @param pdJoinPoint 切面支持
     * @return Result
     * @throws Throwable 异常
     */
    @Around(value = "pointCut(limitRequestFrequency)", argNames = "pdJoinPoint, limitRequestFrequency")
    public Object doAround(ProceedingJoinPoint pdJoinPoint, LimitRequestFrequency limitRequestFrequency) throws Throwable {
        //获取请求的属性
        var requestAttributes = RequestContextHolder.getRequestAttributes();
        // 获取 HttpServletRequest对象
        var request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);
        // 限制的客户端地址
        var limitClientAddress = StrUtil.format("{}{}", request.getRemoteAddr() ,request.getRequestURI());
        // 从缓存中获取请求访问次数
        var cacheCount = cache.hashGet(CacheKey.LIMIT_REQUEST, limitClientAddress);
        // 创建具有原子性的Integer对象
        AtomicInteger count = new AtomicInteger();
        // 判断缓存是否不为空
        if(ObjectUtil.isNotNull(cacheCount)) {
            count.addAndGet(Integer.parseInt(cacheCount.toString()));
        }
        // 若为0则表示缓存已过期删除 则重新计数
        if(count.get() == 0){
            // 第一次请求时默认没有缓存，创建缓存并设置有效时间
            cache.hashPut(CacheKey.LIMIT_REQUEST, limitClientAddress, count.addAndGet(1), limitRequestFrequency.seconds());
        } else {
            // 判断请求次数是否大于限制请求次数
            if (count.get() >= limitRequestFrequency.count()) {
                log.error("URL: {} 请求频繁", request.getRequestURL());
                return Result.fail("请求频繁, 请稍后重试.");
            } else {
                // 当次数未超过限制访问次数时， 记录加一
                cache.hashPut(CacheKey.LIMIT_REQUEST, limitClientAddress, count.addAndGet(1), limitRequestFrequency.seconds());
            }
        }
        // ProceedingJoinPoint 继承了 JoinPoint。是在JoinPoint的基础上暴露出 proceed 这个方法。
        // proceed很重要，这个是aop代理链执行的方法。
        return pdJoinPoint.proceed();
    }

}
