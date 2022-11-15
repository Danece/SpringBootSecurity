package com.springboot_practice.demo;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.springboot_practice.demo.databaseObjects.UserInfo;
import com.springboot_practice.demo.repository.UserInfoRespository;

import lombok.extern.slf4j.Slf4j;;

@Slf4j
@SpringBootTest
public class userInfoDatabaseTest {

    @Autowired
    private UserInfoRespository userRespository;

    @Test
    public void test() throws Exception {
        // userRespository.save(new UserInfo("AAA", "manager", 28));
        // userRespository.save(new UserInfo("BBB", "manager", 29));
        // userRespository.save(new UserInfo("CCC", "user", 30));
        // userRespository.save(new UserInfo("DDD", "user", 31));

        Assertions.assertEquals("manager", userRespository.findAll().size());

        // Assertions.assertEquals("manager", userRespository.findByName("AAA").getRole());

        // Assertions.assertEquals("user", userRespository.findUser("CCC").getRole());

        // Assertions.assertEquals("DDD", userRespository.findByNameAndRole("DDD", "user").getName());

        userRespository.delete(userRespository.findByName("CCC"));

        Assertions.assertEquals(9, userRespository.findAll().size());
    }
    
}
