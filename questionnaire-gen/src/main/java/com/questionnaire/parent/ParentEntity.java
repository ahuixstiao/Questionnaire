package com.questionnaire.parent;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.questionnaire.common.utils.DateUtil;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @Author: ahui
 * @Description: 父类
 * @DateTime: 2022/10/18 - 10:39
 **/
@Data
public class ParentEntity implements Serializable {

    private static final long serialVersionUID = -4390488865653151243L;

    /**
     * 主键ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    @ExcelProperty("ID")
    //@IndexId(type = cn.easyes.annotation.rely.IdType.CUSTOMIZE)
    private Integer id;

    /**
     * 创建人
     */
    @ExcelIgnore
    @TableField(value = "created_by",fill = FieldFill.INSERT)
    private String createdBy;

    /**
     * 创建时间
     */
    @ExcelIgnore
    @TableField(value = "created_time", fill = FieldFill.INSERT)
    @JsonFormat(pattern = DateUtil.PATTERN, timezone = DateUtil.TIMEZONE)
    private LocalDateTime createdTime;

    /**
     * 修改人
     */
    @ExcelIgnore
    @TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
    private String updatedBy;

    /**
     * 修改时间
     */
    @ExcelIgnore
    @TableField(value = "updated_time", fill = FieldFill.INSERT_UPDATE)
    @JsonFormat(pattern = DateUtil.PATTERN, timezone = DateUtil.TIMEZONE)
    private LocalDateTime updatedTime;

    /**
     * 乐观锁
     */
    @Version
    @ExcelIgnore
    private Integer version;

    /**
     * 逻辑删除状态 0:正常 1:禁用
     */
    @ExcelProperty("用户状态 0正常 1禁用")
    private Integer flag;

    /**
     * 备注
     */
    @ExcelProperty("备注")
    private String remark;

}
