package com.springboot_practice.demo.schedule;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.Trigger;
import org.springframework.scheduling.TriggerContext;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
public class PrintA implements SchedulingConfigurer {
    public static String cron ;

    private static final Logger logger = LoggerFactory.getLogger(PrintA.class);
    
    public PrintA() {
        //默認情況是：每5秒執行一次.
        cron = "* * * * * *" ;
    }
    
    @Override
    public void configureTasks (ScheduledTaskRegistrar taskRegistrar) {
        Trigger trigger = new Trigger() {
            @Override
            public Date nextExecutionTime(TriggerContext triggerContext ) {
                //任務觸發，可修改任務的執行週期.
                CronTrigger trigger = new CronTrigger( cron );
                Date nextExec = trigger.nextExecutionTime( triggerContext );
                return nextExec ;
            }
        };
            
        taskRegistrar.addTriggerTask(outputA() , trigger );
        
    }

    public Runnable outputA() {
        return() -> logger.info("[A] " + (int) (Math.random() * 100) + 0);
    }

    public void setCron(String cron) {
        this.cron = cron;
    }
}
