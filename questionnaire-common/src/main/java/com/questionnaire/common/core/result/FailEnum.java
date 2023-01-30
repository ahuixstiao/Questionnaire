package com.questionnaire.common.core.result;

/**
 * @Author: ahui
 * @Description: 状态码与信息
 * @DateTime: 2022/10/3 - 19:35
 **/
public enum FailEnum {

    /**
     * 登录
     */
    NOT_LOGIN(511, "未登录!"),
    LOGIN_FAIL(512, "登录失败，请重试!"),

    /**
     * 账号
     */
    ACCOUNT_OR_PASSWORD_ERROR(522, "账号或密码错误!"),
    ACCOUNT_HAS_BEEN_REGISTER(524, "账号已注册, 请登陆!"),
    ACCOUNT_REGISTER_FAIL(525, "账号注册失败, 请重试或联系客服!"),
    ACCOUNT_IS_LOGGED_IN(526, "账号已登陆, 请勿重复登录!"),
    ACCOUNT_IN_DISABLE(527, "账号已被禁用!"),
    ACCOUNT_PASSWORD_IS_INCORRECT(528, "原密码不正确!"),

    /**
     * 验证码
     */
    CAPTCHA_ERROR(531, "验证码错误!"),
    CAPTCHA_EXPIRED(532, "验证码过期!"),
    CAPTCHA_IS_NULL(533, "验证码为空!"),

    /**
     * 文件
     */
    FILE_UPLOAD_FAIL(541, "文件上传失败!"),
    FILE_IS_EMPTY(542, "文件为空!"),

    /**
     * 用户信息
     */
    PHONE_NUMBER_INCORRECT(551, "手机号码不正确!"),

    /**
     * 参数为空
     */
    PARAMETER_IS_NULL(561,"参数为空!"),

    /**
     * 问卷
     */
    QUESTIONNAIRE_DOES_NOT_EXIST(571, "问卷不存在!");

    // 状态码
    private final Integer errorStatus;
    // 消息
    private final String errorMessage;

    FailEnum(Integer errorStatus, String errorMessage) {
        this.errorStatus = errorStatus;
        this.errorMessage = errorMessage;
    }

    public Integer getErrorStatus() {
        return errorStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
