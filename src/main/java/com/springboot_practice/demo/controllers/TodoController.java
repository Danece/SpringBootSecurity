package com.springboot_practice.demo.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot_practice.demo.databaseObjects.UserInfo;
import com.springboot_practice.demo.repository.UserInfoRespository;
import com.springboot_practice.demo.token.JwtTokenUtils;
import com.springboot_practice.demo.vo.ErrorCode;
import com.springboot_practice.demo.vo.UserInfoVo;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class TodoController<ValidationFailureResponse>  {
    
    @Autowired
    private UserInfoRespository userRespository;

    private ErrorCode errorCode = new ErrorCode();

    /*
     * API Result Response 
     */
    private ResponseEntity responseResult(HttpStatus status, JSONObject body) {
        return ResponseEntity.status(status).body(body);
    }

    /*
     * 自己設定例外處理 - MethodArgumentNotValidException 
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<?> handleConstraintViolationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        JSONObject body = new JSONObject();
        body.put("errorCode", errorCode.exceptionError);
        body.put("errorMsg", ex.getFieldErrors().get(0).getDefaultMessage());
        return responseResult(HttpStatus.BAD_REQUEST, body);
	}

    /*
     * 取得 JWT Accesstoken
     */
    @PostMapping("/accessToken")
    public ResponseEntity login(@RequestBody HashMap<String, String> request) {
        JwtTokenUtils jwtToken = new JwtTokenUtils();
        // 取得 Token
        String token = jwtToken.generateToken(request);
        System.out.println("[ AccessToken ] " + token);
        JSONObject obj = new JSONObject();
        obj.put("UserName", request.get("userName"));
        obj.put("Token", token);
        return ResponseEntity.status(HttpStatus.OK).body(obj);
    }

    /*
     * 取得使用者資訊
     * 沒帶使用者名稱則取所有使用者資訊
     */
    @GetMapping("/userInfo")
    public ResponseEntity getUserInfo(@RequestParam HashMap request) {
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
        return responseResult(HttpStatus.OK, body);
    }

    /*
     * 建立新使用者
     */
    @PostMapping("/userInfo")
    public ResponseEntity createUserInfo (@Valid @RequestBody UserInfoVo request) {
        UserInfo[] userInfo = userRespository.findAllUser();
        boolean alreadyExist = false;
        JSONObject body = new JSONObject();

        for (int i=0; i<userInfo.length; i++) {
            if ((request.getName().equals(userInfo[i].getName().toString()))) {
                alreadyExist = true;
                break;
            }
        }
        
        if (!alreadyExist) {
            userRespository.save(new UserInfo(request.getName(), request.getAuthority()));
            body.put("errorCode", errorCode.noError);
            body.put("errorMsg", "使用者建立成功");

        } else {
            body.put("errorCode", errorCode.normalError);
            body.put("errorMsg", "使用者已存在");
        }
        return responseResult(HttpStatus.OK, body);
    }

    

}
