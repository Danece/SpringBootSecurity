package com.springboot_practice.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.springboot_practice.demo.databaseObjects.UserInfo;

@Repository
public interface UserInfoRespository extends JpaRepository<UserInfo, Long> {
    UserInfo findByName(String name);

    UserInfo findByNameAndAuthority(String name, String authority);

    @Query("from UserInfo u where u.name=:name")
    UserInfo findUser(@Param("name") String name);

    @Query("from UserInfo u where 1=1")
    UserInfo[] findAllUser();
}