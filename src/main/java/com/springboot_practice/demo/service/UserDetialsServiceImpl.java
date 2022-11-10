package com.springboot_practice.demo.service;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springboot_practice.demo.repository.UserInfoDao;

/*
 * 實作驗證功能repository
 */
@Service
public class UserDetialsServiceImpl implements UserDetailsService {

    // @Autowired
    private UserInfoDao userInfoDao = new UserInfoDao();

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("[loadUserByName] " + username);
        System.out.println("[loadUserByName-userInfoDao] " + userInfoDao.users );
        // 查詢用戶是否存在
        Optional<Entry<String, Map<String, String>>> opt = userInfoDao.users
                    .entrySet()
                    .stream()
                    .filter(e -> e.getKey().equals(username))
                    .findFirst();
        if (!opt.isPresent()) throw new UsernameNotFoundException("Not found !");

        // 取得相關資料進行密碼比對
        Map<String, String> info = opt.get().getValue();
        String password = info.get("password");
        String authority = info.get("authority");
        return new User(username, password, AuthorityUtils.commaSeparatedStringToAuthorityList(authority));

    }
    
}
