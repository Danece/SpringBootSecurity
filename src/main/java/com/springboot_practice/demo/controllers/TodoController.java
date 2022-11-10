package com.springboot_practice.demo.controllers;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot_practice.demo.token.JwtTokenUtils;

import net.minidev.json.JSONObject;

@RestController
@RequestMapping("/api")
public class TodoController  {
    
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
}
