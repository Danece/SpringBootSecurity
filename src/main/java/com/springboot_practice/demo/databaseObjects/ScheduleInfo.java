package com.springboot_practice.demo.databaseObjects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ScheduleInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String cron;

    public ScheduleInfo (Long id, String name, String cron) {
        this.id = id;
        this.name = name;
        this.cron = cron;
    }
}
