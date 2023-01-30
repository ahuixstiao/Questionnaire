package com.questionnaire.common.core.result;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @Author: ahui
 * @Description: 返回结果集封装
 * @DateTime: 2022/5/23 - 12:03
 **/
@Data
@Schema(name = "返回结果集对象")
@AllArgsConstructor
public class Result<T> {
    private static final Integer SUCCESS_STATUS_CODE = 200;
    private static final String SUCCESS_MESSAGE = "成功";
    private static final Integer FAILURE_STATUS_CODE = 500;
    private static final String FAILURE_MESSAGE = "错误, 请联系管理员";

    @Schema(name = "状态码")
    private Integer status;   // 状态码
    @Schema(name = "状态信息")
    private String message; // 信息
    @Schema(name = "数据")
    private T data;         // 数据

    /**
     * 无参构造
     */
    public Result() {}

    private Result(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public Result(FailEnum failEnum) {
        this.status = failEnum.getErrorStatus();
        this.message = failEnum.getErrorMessage();
    }

    // ================== 构建成功的返回 ==================

    //成功，无返回数据
    public static <T> Result<T> ok() {
        return new Result<>(SUCCESS_STATUS_CODE, SUCCESS_MESSAGE);
    }

    // 成功 有返回数据
    public static <T> Result<T> ok(T data) {
        return new Result<>(SUCCESS_STATUS_CODE, SUCCESS_MESSAGE, data);
    }

    // ================== 构建失败的返回 ==================

    //失败,无返回数据
    public static <T> Result<T> fail() {
        return new Result<>(FAILURE_STATUS_CODE, FAILURE_MESSAGE);
    }

    // 失败 有返回信息
    public static <T> Result<T> fail(String message) {
        return new Result<>(FAILURE_STATUS_CODE, message);
    }

    // ================== 具体错误 ==================

    /**
     *
     * @param failEnum 具体错误
     * @return Result
     */
    public static <T> Result<T> fail(FailEnum failEnum) {
        return new Result<>(failEnum);
    }
}
