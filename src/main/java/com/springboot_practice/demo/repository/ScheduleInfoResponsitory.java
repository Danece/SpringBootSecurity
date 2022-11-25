package com.springboot_practice.demo.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.springboot_practice.demo.databaseObjects.ScheduleInfo;

@Repository
public interface ScheduleInfoResponsitory extends JpaRepository<ScheduleInfo, Long> {
    ScheduleInfo findByName(String name);

    @Query("from ScheduleInfo s where 1=1")
    ScheduleInfo[] findAllSchedule();
}
