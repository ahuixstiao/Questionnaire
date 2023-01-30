package com.questionnaire.api.exception;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import com.qiniu.common.QiniuException;
import com.questionnaire.common.core.result.FailEnum;
import com.questionnaire.common.core.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author: ahui
 * @Description: 全局异常处理器
 * @DateTime: 2022/5/28 - 11:56
 **/
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 登录异常拦截
     *
     * @return Result
     */
    @ExceptionHandler(NotLoginException.class)
    public Result<Object> handleNotLoginException(HttpServletRequest request, NotLoginException e) {
        var message = "";
        if (e.getType().equals(NotLoginException.NOT_TOKEN)) {
            message = "未能读取到有效Token";
        } else if (e.getType().equals(NotLoginException.INVALID_TOKEN)) {
            message = "Token无效";
        } else if (e.getType().equals(NotLoginException.TOKEN_TIMEOUT)) {
            // Token已过期
            message = "Token已过期";
        } else if (e.getType().equals(NotLoginException.BE_REPLACED)) {
            // Token已被顶下线
            message = "账号已被顶下线";
        } else if (e.getType().equals(NotLoginException.KICK_OUT)) {
            // Token已被踢下线
            message = "账号已被踢下线";
        } else {
            // 都不符合以上情况时 则认为前用户未登录
            message = NotLoginException.DEFAULT_MESSAGE;
        }
        logProcessing(request, e);
        return Result.fail(message);
    }

    /**
     * 权限异常处理
     *
     * @return Result
     */
    @ExceptionHandler(NotPermissionException.class)
    public Result<Object> handleNotPermissionException(HttpServletRequest request, NotPermissionException e) {
        logProcessing(request, e);
        return Result.fail(e.getMessage());
    }

    /**
     * 角色异常处理
     *
     * @return Result
     */
    @ExceptionHandler(NotRoleException.class)
    public Result<Object> handleNotRoleException(HttpServletRequest request, NotRoleException e) {
        logProcessing(request, e);
        return Result.fail(e.getMessage());
    }

    /**
     * 空指针异常处理
     *
     * @return Result
     */
    @ExceptionHandler(NullPointerException.class)
    public Result<Object> handleNullPointerException(HttpServletRequest request, NullPointerException e) {
        logProcessing(request, e);
        return Result.fail(FailEnum.PARAMETER_IS_NULL);
    }

    /**
     * 七牛云 异常处理
     * @param request
     * @param qiniuException
     * @return
     */
    @ExceptionHandler(QiniuException.class)
    public Result<Object> handleQiniuException(HttpServletRequest request, QiniuException qiniuException) {
        logProcessing(request, qiniuException);
        return Result.fail(qiniuException.response.toString());
    }

    // 日志打印处理
    private void logProcessing(HttpServletRequest request, Exception e) {
        var exStr = new StringBuilder();
        var stackTrace = e.getStackTrace();
        for (var stackTraceElement : stackTrace) {
            exStr.append("\tat ").append(stackTraceElement).append("\r\n");
        }
        log.error(
                ">>>>>>>>>>>>>>>>> IP: {} URL: {} Method: {} Exception: {} \n{}",
                request.getRemoteAddr(), request.getRequestURL(), request.getMethod(),
                e.getMessage(), exStr
        );
    }

}
