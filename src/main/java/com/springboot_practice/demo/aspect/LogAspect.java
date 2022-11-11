package com.springboot_practice.demo.aspect;

import java.util.Arrays;
import java.util.Objects;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@Aspect
public class LogAspect {

    // 切入點
    @Pointcut("execution(* com.springboot_practice.demo.controllers..*(..))")
    public void pointcut () {
        // 基本上不用特別實作
    }

    // 宣告時間戳記使用的變數，紀錄 Controller 的執行起訖
    Long longTimeStampStart, longTimeStampEnd;

    // @Before 表示在 Controller 之前運作
    @Before("pointcut()")
    public void before (JoinPoint joinPoint) {
        // 紀錄 Controller 啟動時間
        longTimeStampStart = System.currentTimeMillis();
    }

    // 帶入 JoinPoint 表示被執行的那個 Pointcut 程式類別
    // @AfterReturning 表示在 Controller 正常完成之後運作
    @AfterReturning("pointcut()")
    public void AfterReturningOfPointcut (JoinPoint joinPoint) {
        // 在日誌輸出 Controller 正常執行完成的資訊
        Logger(LoggerFactory.getLogger(joinPoint.getTarget().getClass()), "API Finished  Normally.");
    }

    // @AfterThrowing 表示 Controller 發生錯誤時要做的事情
    @AfterThrowing(value = "pointcut()", throwing = "exception")
    public void AfterReturningOfPointcut (JoinPoint joinPoint, Exception exception) {
        Logger myLogger = LoggerFactory.getLogger(joinPoint.getTarget().getClass());

        // 在日誌輸出 Exception 資訊
        myLogger.error("API Finished by Exception: {}", exception.getMessage());
    }

    // @After 表示 Controller 無論有沒有正常結束都要做的事情
    @After("pointcut()")
    public void AfterPointcut(JoinPoint joinPoint) {
        // 紀錄 Controller 結束時間
        longTimeStampEnd = System.currentTimeMillis();

        // 起訖時間相減取得該 Controller 的執行時間
        Long longTimeDiff = longTimeStampEnd - longTimeStampStart;

        // 在日誌輸出 Controller 執行時間
        Logger(LoggerFactory.getLogger(joinPoint.getTarget().getClass()), "Time used : " + Objects.toString(longTimeDiff) + " ms");

        // 在日誌輸出 Request 相關資訊
        Logger(LoggerFactory.getLogger(joinPoint.getTarget().getClass()), joinPoint);
    }

    // 注入 HttpServletRequest 以便取得 Request 資訊
    @Autowired
    private HttpServletRequest request;

    // 注入 HttpServletResponse 以便取得 Response 資訊
    @Autowired
    private HttpServletResponse response;

    private void Logger(Logger myLogger, JoinPoint joinPoint) {
        String protocal = Objects.toString(request.getProtocol(), "");
        String method = request.getMethod();
        String scheme = request.getScheme();
        String uri = request.getRequestURI();
        String agent = request.getHeader("user-agent");
        String remoteaddr = request.getRemoteAddr();
        int remoteport = request.getRemotePort();
        String localaddr = request.getLocalAddr();
        int localport = request.getLocalPort();

        String sessionid = request.getSession().getId();

        String classname = joinPoint.getTarget().getClass().getName();
        String classMethod = joinPoint.getSignature().getName();

        String strMessage = "\r\nController Logging Message:\r\n";
        strMessage += String.format("%s\r\n", "------------------------------");
        strMessage += String.format("   Session ID : %s\r\n", sessionid);
        strMessage += String.format("     Protocal : %s\r\n", protocal);
        strMessage += String.format("       Method : %s\r\n", method);
        strMessage += String.format("          Url : %s://%s%d%s\r\n", scheme, localaddr, localport, uri);
        strMessage += String.format("        Agent : %s\r\n", agent);
        strMessage += String.format("       Remote : %s:%d\r\n", remoteaddr, remoteport);
        strMessage += String.format("   Class Name : %s\r\n", classname);
        strMessage += String.format(" Class Method : %s", classMethod);

        myLogger.info(strMessage);
    }

    private void Logger(Logger myLogger, String message) {
        myLogger.info(message);
    }
}
