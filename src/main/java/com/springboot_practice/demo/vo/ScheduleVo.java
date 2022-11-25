package com.springboot_practice.demo.vo;
import javax.validation.constraints.NotEmpty;

public class ScheduleVo {
    private String scheduleName;
    private String cronString;

    @NotEmpty(message = "排程名稱不可為空")
    public String getScheduleName() {
        return this.scheduleName;
    }

    public void setScheduleName(String scheduleName) {
        this.scheduleName = scheduleName;
    }

    @NotEmpty(message = "排程時間不可為空")
    public String getCronString() {
        return this.cronString;
    }

    public void setCronString(String cronString) {
        this.cronString = cronString;
    }
}
