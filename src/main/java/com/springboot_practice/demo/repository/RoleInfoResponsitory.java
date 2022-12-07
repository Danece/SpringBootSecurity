package com.springboot_practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot_practice.demo.databaseObjects.RoleInfo;

public interface RoleInfoResponsitory extends JpaRepository<RoleInfo, Long> {
    @Query("from RoleInfo s where 1=1")
    RoleInfo[] findAllRole();
}
