package com.kfyty.quartz;

import com.kfyty.quartz.job.HelloJob;
import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import java.util.Date;

/**
 * 描述:
 *
 * @author kfyty725
 * @date 2021/6/5 12:23
 * @email kfyty725@hotmail.com
 */
public class QuartzDemo {

    public static void main(String[] args) throws Exception {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        JobDetail jobDetail = JobBuilder.newJob(HelloJob.class)
                .withIdentity("hello-job", "hello-group")
                .usingJobData("cnt", 0)
                .usingJobData("name", "tom")
                .build();
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("hello-job-trigger", "hello-group")
                .startNow()
                .endAt(new Date(System.currentTimeMillis() + 10000L))                       // 10s 后结束
                .withPriority(1)
                .withSchedule(CronScheduleBuilder.cronSchedule("* * * * * ?"))              // 每秒执行一次
                .build();
        scheduler.scheduleJob(jobDetail, trigger);
        scheduler.start();
    }
}
