package com.questionnaire.schedule.config;

import com.questionnaire.schedule.job.SysLoggingJob;
import org.quartz.*;
import org.springframework.context.annotation.Configuration;

/**
 * @Author: ahui
 * @Description: TODO 定时任务配置类
 * @DateTime: 2023/1/9 - 18:39
 **/
@Configuration
public class QuartzConfig {

    /**
     * TODO 构建具体的定时任务类
     *
     * @return JobDetail
     */
    //@Bean
    public JobDetail logDetail() {
        return JobBuilder.newJob(SysLoggingJob.class)
                // 任务详情ID
                .withIdentity("sysLogJob")
                .storeDurably()
                .build();
    }

    /**
     * TODO 构建对应任务的触发器
     *
     * @return Trigger
     */
    //@Bean
    public Trigger logTrigger() {
        // 返回触发器
        return TriggerBuilder.newTrigger()
                .forJob(logDetail()) // 指定任务详情
                .withIdentity("sysLogJob") // 触发器ID
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0/10 * * * ?")) // 触发条件
                .build();
    }

}
