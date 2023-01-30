package com.questionnaire.db.entity;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.questionnaire.common.utils.DateUtil;
import com.questionnaire.parent.ParentEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * TODO 用户实体
 * @author ahui
 * @since 2022-05-23
 */
@HeadRowHeight(40)    //标题高度
@ColumnWidth(40)      //列宽
@ContentRowHeight(30) //文本行高
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER) // 使标题居中
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER) // 使文本居中
@Data
@EqualsAndHashCode(callSuper = true)
@Schema(name = "用户实体对象")
@TableName("q_users") // 数据库表名
//@IndexName(value = "users", shardsNum = 3, replicasNum = 2) //es索引
public class Users extends ParentEntity implements Serializable {

    private static final long serialVersionUID = 6008649541872794118L;

    /**
     * 用户ID
     */
    @ExcelProperty("用户ID")
    private String userId;

    /**
     * 用户账号
     */
    @ExcelProperty("用户账号")
    private String account;

    /**
     * 用户密码
     */
    @ExcelIgnore
    private String password;

    /**
     * 用户类型
     */
    @ExcelProperty("用户类型 0超级管理员 1管理员 2普通用户")
    //@IndexField(fieldType = FieldType.INTEGER, strategy = FieldStrategy.NOT_EMPTY)
    private Integer userType;

    /**
     * 手机号码
     */
    @ExcelProperty("手机号码")
    //@IndexField(strategy = FieldStrategy.NOT_EMPTY)
    private String phoneNumber;

    /**
     * 用户头像
     */
    @ExcelIgnore
    private String avatar;

    /**
     * 用户昵称
     */
    @ExcelProperty("用户昵称")
    //@IndexField(fieldType = FieldType.KEYWORD, fieldData = true)
    private String nickName;

    /**
     * 个性签名
     */
    @ExcelProperty("个性签名")
    private String signature;

    /**
     * 邮箱地址
     */
    @ExcelProperty("邮箱")
    private String email;

    /**
     * 联系地址
     */
    @ExcelProperty("联系地址")
    private String address;

    /**
     * 最后登录IP
     */
    @ExcelProperty("最后登录IP")
    //@IndexField(fieldType = FieldType.IP, strategy = FieldStrategy.NOT_EMPTY)
    private String loginIp;

    /**
     * 最后登录时间
     */
    @ExcelProperty("最后登录时间")
    @JsonFormat(pattern = DateUtil.PATTERN, timezone = DateUtil.TIMEZONE)
    private LocalDateTime loginDate;
}
