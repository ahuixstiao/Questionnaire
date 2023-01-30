package com.questionnaire.db.entity.system;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author: ahui
 * @Description: TODO 表信息
 * @DateTime: 2022/12/22 - 11:34
 **/
@Data
public class TableInfo implements Serializable {
    private static final long serialVersionUID = -254565206060727642L;

    //表名
    private String tableName;

    // 表描述
    private String tableComment;

}
