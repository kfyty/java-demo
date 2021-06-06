package com.kfyty.quartz.job;

import lombok.Setter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.PersistJobDataAfterExecution;

import java.util.concurrent.TimeUnit;

/**
 * 描述: setter 方法可以被 JobDataMap 中的 key 自动赋值，但会被触发器中相同的 key 覆盖
 * DisallowConcurrentExecution 注解可以避免并发执行 job，但并不会影响 job 应该被调用的次数
 * PersistJobDataAfterExecution 可以保存被修改的 JobDataMap
 *
 * @author kfyty725
 * @date 2021/6/5 12:23
 * @email kfyty725@hotmail.com
 */
@Slf4j
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class HelloJob implements Job {
    @Setter
    private Integer cnt;

    @Setter
    private String name;

    @Override
    @SneakyThrows
    public void execute(JobExecutionContext context) throws JobExecutionException {
        TimeUnit.SECONDS.sleep(2);
        context.getJobDetail().getJobDataMap().put("cnt", ++this.cnt);
        log.info("hello job: {}, cnt: {}", this.name, this.cnt);
    }
}
