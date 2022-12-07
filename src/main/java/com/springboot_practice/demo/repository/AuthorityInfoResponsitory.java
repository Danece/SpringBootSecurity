package com.springboot_practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot_practice.demo.databaseObjects.AuthorityInfo;

public interface AuthorityInfoResponsitory extends JpaRepository<AuthorityInfo, Long> {
    @Query("from AuthorityInfo s where 1=1")
    AuthorityInfo[] findAllAuthority();
}