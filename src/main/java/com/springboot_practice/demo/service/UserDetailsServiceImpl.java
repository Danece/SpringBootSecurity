package com.springboot_practice.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot_practice.demo.databaseObjects.UserInfo;
import com.springboot_practice.demo.repository.UserInfoDao;
import com.springboot_practice.demo.repository.UserInfoRespository;

/*
 * 實作驗證功能repository
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserInfoDao userInfoDao = new UserInfoDao();

    @Autowired
    private UserInfoRespository userRespository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /* 
         *  查詢用戶是否存在
        */
        // 1. 來源 : 程式建立
        // Optional<Entry<String, Map<String, String>>> opt = userInfoDao.users
        //             .entrySet()
        //             .stream()
        //             .filter(e -> e.getKey().equals(username))
        //             .findFirst();
        // if (!opt.isPresent()) throw new UsernameNotFoundException("Not found !");

        // 2. 來源 : 資料庫
        UserInfo[] userInfos = userRespository.findAllUser();
        UserInfo userInfo = new UserInfo();
        boolean userExist = false;
        for (int i=0; i<userInfos.length; i++) {
            if (username.equals(userInfos[i].getName())) {
                userExist = true;
                userInfo = userInfos[i];
            }
        }
        if (!userExist) throw new UsernameNotFoundException("Not found !");

        /* 
        * 取得相關資料進行密碼比對
        */ 
        // 1. 來源 : 程式建立
        // Map<String, String> info = opt.get().getValue();
        // String password = info.get("password");
        // String authority = info.get("authority");
        // return new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(authority));

        // 2. 來源 : 資料庫
        return new User(username, userInfo.getPassword(), AuthorityUtils.commaSeparatedStringToAuthorityList(userInfo.getAuthority()));

    }
    
}
