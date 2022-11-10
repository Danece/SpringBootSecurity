package com.springboot_practice.demo.repository;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

/*
 * 建立使用者資訊
 */
@Repository
public class UserInfoDao {
    public Map<String, Map<String, String>> users;
    {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Map<String, String> userInfo_admin = new LinkedHashMap<>();
        userInfo_admin.put("password", passwordEncoder.encode("admin"));
        userInfo_admin.put("authority", "admin, normal, ROLE_manager");

        Map<String, String> userInfo_user01 = new LinkedHashMap<>();
        userInfo_user01.put("password", passwordEncoder.encode("123"));
        userInfo_user01.put("authority", "normal, ROLE_employee");

        users = new LinkedHashMap<>();
        users.put("admin", userInfo_admin);
        users.put("user01", userInfo_user01);

        System.out.println("[UserInfoDao] " + users);
    }
}


