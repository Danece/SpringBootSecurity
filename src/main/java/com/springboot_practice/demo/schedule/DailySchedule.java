package com.springboot_practice.demo.schedule;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


/*
 *  ┌───────────── second (0-59)
    │ ┌───────────── minute (0 - 59)
    │ │ ┌───────────── hour (0 - 23)
    │ │ │ ┌───────────── day of the month (1 - 31)
    │ │ │ │ ┌───────────── month (1 - 12) (or JAN-DEC)
    │ │ │ │ │ ┌───────────── day of the week (0 - 7)
    │ │ │ │ │ │          (0 or 7 is Sunday, or MON-SUN)
    │ │ │ │ │ │
    * * * * * *

    * : 表示匹配該域的任意值，比如在秒*, 就表示每秒都會觸發事件。；
    ? : 只能用在每月第幾天和星期兩個域。表示不指定值，當2個子表示式其中之一被指定了值以後，為了避免衝突，需要將另一個子表示式的值設為“?”；
    – : 表示範圍，例如在分域使用5-20，表示從5分到20分鐘每分鐘觸發一次  
    / : 表示起始時間開始觸發，然後每隔固定時間觸發一次，例如在分域使用5/20,則意味著5分，25分，45分，分別觸發一次.  
    , : 表示列出列舉值。例如：在分域使用5,20，則意味著在5和20分時觸發一次。
    # : 用於確定每個月第幾個星期幾，只能出現在每月第幾天域。例如在1#3，表示某月的第三個星期日。
 */

@Component
public class DailySchedule {
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Scheduled(cron = "0 0 11 * * ?") 
    public void testSchedule() {
        logger.info("[ testSchedule ] SUCCESS ##################33");
    }
}
