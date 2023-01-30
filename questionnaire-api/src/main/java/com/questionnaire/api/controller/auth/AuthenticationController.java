package com.questionnaire.api.controller.auth;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.util.StrUtil;
import com.questionnaire.common.aspect.annotation.LimitRequestFrequency;
import com.questionnaire.common.core.result.FailEnum;
import com.questionnaire.common.core.result.Result;
import com.questionnaire.db.entity.Users;
import com.questionnaire.db.service.IUsersService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

/**
 * 认证中心
 *
 * @author ahui
 * @since 2022-04-14
 */
@Slf4j
@Tag(name = "认证中心模块", description = "负责用户的登录、注册、注销等业务")
@RequiredArgsConstructor(onConstructor = @__(@Autowired)) // 将所有private final的引用类型使用构造器注入并采用@Autowired注解注释
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final IUsersService iUsersService; // 用户信息Service

    /**
     * TODO 登陆
     * 提供token用于后续的访问验证
     * 前后端分离的项目则需要手动返回token 前端访问时将参数设在Headers中 格式为{tokenName : tokenValue}
     *
     * @return Result
     */
    @Operation(summary = "登录")
    @Parameters(value = {
            @Parameter(name = "account", description = "账号", required = true),
            @Parameter(name = "password", description = "密码", required = true)
    })
    @LimitRequestFrequency
    @GetMapping("/{account}/{password}/{isAdmin}")
    public Result<Object> ordinaryUsersLogin(
            @PathVariable @NotNull String account,
            @PathVariable @NotNull String password,
            @PathVariable boolean isAdmin
    ) {
        // 账号是否登陆
        if (StpUtil.isLogin()){
            return Result.fail(FailEnum.ACCOUNT_IS_LOGGED_IN);
        }

        //返回
        return iUsersService.login(account, password, isAdmin);
    }

    /**
     * 注册
     *
     * @param users 用户信息
     * @return Result
     */
    @Operation(summary = "注册")
    @LimitRequestFrequency
    @PostMapping("/")
    public Result<Object> userRegister(@RequestBody Users users) {
        // 参数是否为空
        if (StrUtil.isBlank(users.getAccount()) || StrUtil.isBlank(users.getPassword())) {
            return Result.fail(FailEnum.PARAMETER_IS_NULL);
        }
        // 新增并返回用户的token信息
        return iUsersService.userRegister(users);
    }

    /**
     * 注销
     *
     * @return Result
     */
    @Operation(summary = "注销", description = "用户注销接口，调用后用户将下线")
    @LimitRequestFrequency
    @GetMapping("/logout")
    public Result<Object> logOut() {
        //检查是否登陆 会自动获取当前会话的loginId进行判断
        if (StpUtil.isLogin()) {
            log.info("将要注销的 UserId: {}", StpUtil.getLoginId());
            StpUtil.logout();
            //由于SaToken默认处理注销的账号缓存 所以无需手动回收redis缓存
            log.info(">>>>>>>>>>>>>>>>> 账号注销成功 >>>>>>>>>>>>>>>>> ");
            return Result.ok();
        }
        log.info(">>>>>>>>>>>>>>>>> 账号未登录 >>>>>>>>>>>>>>>>> ");
        return Result.fail(FailEnum.NOT_LOGIN);
    }
}