package com.springboot_practice.demo.controllers;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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
import com.springboot_practice.demo.service.APIService;
import com.springboot_practice.demo.token.JwtTokenUtils;
import com.springboot_practice.demo.vo.ErrorCode;
import com.springboot_practice.demo.vo.UserInfoVo;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class TodoController<ValidationFailureResponse>  {
    

    private ErrorCode errorCode = new ErrorCode();

    @Autowired
    private APIService apiService = new APIService();

    /*
     * API Result Response 
     */
    private ResponseEntity responseResult(HttpStatus status, JSONObject body) {
        return ResponseEntity.status(status).body(body);
    }

    /*
     * 自己設定例外處理 - MethodArgumentNotValidException
     * 次例外問題會在打進來時就先檢核，並不是往下送到 apiService.java 才做檢查 
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<?> handleConstraintViolationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        JSONObject body = new JSONObject();
        body.put("errorCode", errorCode.exceptionError);
        body.put("errorMsg", ex.getFieldErrors().get(0).getDefaultMessage());
        return responseResult(HttpStatus.BAD_REQUEST, body);
	}

    /*
     * 取得 JWT 加密結果
     * 參數:使用者名稱
     */
    @PostMapping("/jwtResult")
    public ResponseEntity getJwtResult(@RequestBody HashMap<String, String> request) {
        
        return ResponseEntity.status(HttpStatus.OK).body(apiService.getJwtResult(request));
    }

    /*
     * 取得使用者資訊
     * 參數:名稱
     * (沒帶使用者名稱則取所有使用者資訊)
     */
    @GetMapping("/userInfo")
    public ResponseEntity getUserInfo(@RequestParam HashMap request) {
        
        return responseResult(HttpStatus.OK, apiService.getUserInfo(request));
    }

    /*
     * 建立新使用者
     * 參數:名稱、密碼、權限
     */
    @PostMapping("/userInfo")
    public ResponseEntity createOrUpdateUserInfo (@Valid @RequestBody UserInfoVo request) {
        
        return responseResult(HttpStatus.OK, apiService.createOrUpdateUserInfo(request));
    }

    

}
