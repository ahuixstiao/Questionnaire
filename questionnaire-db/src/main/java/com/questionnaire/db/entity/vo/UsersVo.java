package com.questionnaire.db.entity.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.questionnaire.common.utils.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: ahui
 * @Description: 用户前台实体
 * @DateTime: 2022/11/11 - 10:31
 **/
@Data
public class UsersVo implements Serializable {

    private static final long serialVersionUID = 6310295775519273877L;

    /**
     * 主键
     */
    private Integer id;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * 用户账号
     */
    private String account;

    /**
     * 用户类型
     */
    private Integer userType;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 个性签名
     */
    private String signature;

    /**
     * 邮箱地址
     */
    private String email;

    /**
     * 联系地址
     */
    private String address;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    @JsonFormat(pattern = DateUtil.PATTERN, timezone = DateUtil.TIMEZONE)
    private LocalDateTime loginDate;

    /**
     * 用户状态 0 正常 1禁用
     */
    private Integer flag;
}
