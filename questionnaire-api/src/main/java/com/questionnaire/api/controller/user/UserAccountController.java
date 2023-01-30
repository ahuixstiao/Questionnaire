package com.questionnaire.api.controller.user;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.map.MapUtil;
import com.questionnaire.common.core.cache.RedisCacheTool;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.service.IUsersService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.time.Instant;

/**
 * @Author: ahui
 * @Description: TODO 账号控制
 * @DateTime: 2022/12/5 - 17:29
 **/
@Slf4j
@Tag(name = "用户账号控制模块", description = "负责用户的封禁、账号访问管控等业务")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping("/account")
public class UserAccountController {
    private final IUsersService usersService;
    private final RedisCacheTool cache;

    /**
     * 封禁账号
     * 对于正在登录的账号，将其封禁并不会使它立即掉线，如果我们需要它即刻下线，可采用先踢再封禁的策略
     * 1.31.0版本之后 StpUtil.login()方法不再会自动校验账号是否被封禁
     * @param userId 用户ID
     * @param disableTime 封禁时间 单位：秒
     * @return Result<Object>
     */
    @PutMapping("/disable/{userId}/{disableTime}")
    public Result<Object> accountDisable(
            @PathVariable @NotNull String userId,
            @PathVariable Integer disableTime
    ) {
        // 将账号踢下线 由于登陆时设置的loginId与userId一致所以可以通过userId来处理用户账号
        StpUtil.kickout(userId);
        // 封禁账号 封禁时间，单位：秒，此为 86400秒 = 1天（此值为 -1 时，代表永久封禁)
        StpUtil.disable(userId, disableTime);
        // 返回封禁时间
        return Result.ok();
    }

    /**
     * 获取用户封禁时间
     * @param userId 用户ID
     * @return Result<Object>
     */
    @GetMapping("/get/{userId}")
    public Result<Object> getDisableTime(@PathVariable @NotNull String userId) {
        // 获取当前时间并加上剩余的封禁时间（秒）然后转换成对应的年月日时分秒
        Instant disableTime = Instant.now().plusSeconds(StpUtil.getDisableTime(userId));
        // 返回封禁时间
        return Result.ok(
                MapUtil.builder().put("disableTime", disableTime).build()
        );
    }
}
