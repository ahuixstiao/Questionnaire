package com.questionnaire.schedule.entity;

import lombok.Data;

/**
 * @Author: ahui
 * @Description: TODO 定时任务相关实体
 * @DateTime: 2023/1/9 - 18:34
 **/
@Data
public class JobInfo {

    /**
     * 定时任务ID
     */
    private String id;

    /**
     * 定时任务名称
     */
    private String jobName;

    /**
     * 定时任务执行类
     */
    private String jobClass;

    /**
     * 定时任务状态
     */
    private Integer status;

    /**
     * 定时任务运行时间表达式 0 0/10 * * * ? 表示每10分钟执行一次任务
     */
    private String cronExpression;

}
