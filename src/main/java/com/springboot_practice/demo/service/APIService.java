package com.springboot_practice.demo.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.springboot_practice.demo.databaseObjects.AuthorityInfo;
import com.springboot_practice.demo.databaseObjects.RoleInfo;
import com.springboot_practice.demo.databaseObjects.ScheduleInfo;
import com.springboot_practice.demo.databaseObjects.UserInfo;
import com.springboot_practice.demo.repository.AuthorityInfoResponsitory;
import com.springboot_practice.demo.repository.RoleInfoResponsitory;
import com.springboot_practice.demo.repository.ScheduleInfoResponsitory;
import com.springboot_practice.demo.repository.UserInfoRespository;
import com.springboot_practice.demo.token.JwtTokenUtils;
import com.springboot_practice.demo.vo.ErrorCode;
import com.springboot_practice.demo.vo.ScheduleVo;
import com.springboot_practice.demo.vo.UserInfoVo;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONObject;

@Service
@RequiredArgsConstructor
public class APIService {
    
    @Autowired
    private UserInfoRespository userRespository;

    @Autowired
    private ScheduleInfoResponsitory scheduleInfoResponsitory;

    @Autowired
    private RoleInfoResponsitory roleInfoResponsitory;

    @Autowired
    private AuthorityInfoResponsitory authorityInfoResponsitory;

    @Autowired
    private DailyScheduleService dailyScheduleService;

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
            UserInfo[] userInfo = userRespository.findUser(request.get("name").toString(), request.get("authority").toString());
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
        String operation = request.getOperation();
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

        switch(operation) {
            case "create":
                if (alreadyExist) {
                    body.put("errorMsg", "使用者已存在");
                    body.put("errorCode", errorCode.normalError);

                } else {
                    body.put("errorMsg", "使用者建立成功");
                    body.put("errorCode", errorCode.noError);
                    userRespository.save(new UserInfo(request.getName(), passwordEncoder.encode( request.getPassword()), request.getAuthority()));
                }
                break;
            case "update":
                if (alreadyExist) {
                    body.put("errorMsg", "使用者更新成功");
                    userRespository.save(new UserInfo(old_id, request.getName(), passwordEncoder.encode( request.getPassword()), request.getAuthority()));
                
                } else {
                    body.put("errorMsg", "使用者不存在");
                    body.put("errorCode", errorCode.normalError);
                }
                break;
        }

        return body;
    }

    /*
     * 刪除使用者
     */
    public JSONObject deleteUserInfo(HashMap request) {
        JSONObject body = new JSONObject();
        ArrayList ids = (ArrayList) request.get("id");
        System.out.println("#######################################33 " + request);
        if (request.containsKey("id")) {
            for (int i=0; i<ids.size(); i++) {
                userRespository.deleteById(Long.valueOf((int) ids.get(i)));
            }
            body.put("errorCode", errorCode.noError);

        } else {
            body.put("errorMsg", "need required value");
            body.put("errorCode", errorCode.normalError);
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

    /*
     * 修改排程時間
     */
    public JSONObject updateSchedule(ScheduleVo scheduleVo) {
        JSONObject body = new JSONObject();
        ScheduleInfo[] scheduleInfo = scheduleInfoResponsitory.findAllSchedule();
        Long scheduleId = (long) 0;

        for (int i=0; i<scheduleInfo.length; i++) {
            if (scheduleInfo[i].getName().equals(scheduleVo.getScheduleName())) {
                scheduleId = scheduleInfo[i].getId();
            }
        }

        if ((long) 0 != scheduleId) {
            scheduleInfoResponsitory.save(new ScheduleInfo(scheduleId, scheduleVo.getScheduleName(), scheduleVo.getCronString()));
            dailyScheduleService.changeSchedule(scheduleVo.getScheduleName(), scheduleVo.getCronString());
            body.put("errorCode", errorCode.noError);
            body.put("errorMsg", "排程修改成功");
        
        } else {
            body.put("errorCode", errorCode.normalError);
            body.put("errorMsg", "排程不存在");
        }
        
        return body;
    }

    /*
     * 查詢排程
     */
    public JSONObject getScheduleInfo(HashMap request) {
        JSONObject body = new JSONObject();

        ScheduleInfo[] scheduleInfo = scheduleInfoResponsitory.findAllSchedule();
        body.put("errorCode", errorCode.noError);
        body.put("content", scheduleInfo);

        return body;
    }

    public JSONObject getRoleInfo(HashMap request) {
        JSONObject body = new JSONObject();

        RoleInfo[] roleInfo = roleInfoResponsitory.findAllRole();
        body.put("errorCode", errorCode.noError);
        body.put("content", roleInfo);

        return body;
    }

    public JSONObject getAuthorityInfo(HashMap request) {
        JSONObject body = new JSONObject();

        AuthorityInfo[] roleInfo = authorityInfoResponsitory.findAllAuthority();
        body.put("errorCode", errorCode.noError);
        body.put("content", roleInfo);

        return body;
    }
}
