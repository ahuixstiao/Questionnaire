package com.questionnaire.schedule.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Component;

/**
 * @Author: ahui
 * @Description: TODO 日志入库定时任务
 * @DateTime: 2023/1/9 - 18:27
 **/
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class SysLoggingJob extends QuartzJobBean {

    /**
     *  系统日志记录
     *
     * @param context 任务容器
     * @throws JobExecutionException 异常
     */
    @Override
    protected void executeInternal(@NotNull JobExecutionContext context) throws JobExecutionException {

    }
}
