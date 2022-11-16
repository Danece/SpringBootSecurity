package com.springboot_practice.demo.service;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot_practice.demo.databaseObjects.UserInfo;
import com.springboot_practice.demo.repository.UserInfoRespository;
import com.springboot_practice.demo.token.JwtTokenUtils;
import com.springboot_practice.demo.vo.ErrorCode;
import com.springboot_practice.demo.vo.UserInfoVo;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;

@Service
@RequiredArgsConstructor
public class APIService {
    
    @Autowired
    private UserInfoRespository userRespository;

    private ErrorCode errorCode = new ErrorCode();

    /*
     * 查詢使用者
     */
    public JSONObject getUserInfo(HashMap request) {
        JSONObject body = new JSONObject();

        if (null == request.get("name")) {
            UserInfo[] userInfo = userRespository.findAllUser();
            body.put("errorCode", errorCode.noError);
            body.put("content", userInfo);
            
        } else {
            UserInfo userInfo = userRespository.findUser(request.get("name").toString());
            body.put("errorCode", errorCode.noError);
            body.put("content", userInfo);

        }
        return body;
    }

    /*
     * 建立使用者
     */
    public JSONObject createOrUpdateUserInfo(UserInfoVo request) {
        JSONObject body = new JSONObject();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        UserInfo[] userInfo = userRespository.findAllUser();
        boolean alreadyExist = false;
        Long old_id = (long) -1;

        /* 比對資料庫判斷是否使用者已存在 */
        for (int i=0; i<userInfo.length; i++) {
            if ((request.getName().equals(userInfo[i].getName().toString()))) {
                alreadyExist = true;
                old_id = userInfo[i].getId();
                break;
            }
        }
            
        body.put("errorCode", errorCode.noError);
        if (!alreadyExist) {
            body.put("errorMsg", "使用者建立成功");
            userRespository.save(new UserInfo(request.getName(), passwordEncoder.encode( request.getPassword()), request.getAuthority()));
        
        } else {
            body.put("errorMsg", "使用者更新成功");
            userRespository.save(new UserInfo(old_id, request.getName(), passwordEncoder.encode( request.getPassword()), request.getAuthority()));
        
        }
        return body;
    }

    /*
     * 取得 JWT 加密結果
     */
    public JSONObject getJwtResult(HashMap request) {
        JSONObject body = new JSONObject();
        JwtTokenUtils jwtToken = new JwtTokenUtils();
        String token = jwtToken.generateToken(request);
        System.out.println("[ AccessToken ] " + token);
        body.put("UserName", request.get("userName"));
        body.put("Token", token);
        return body;
    }
}
