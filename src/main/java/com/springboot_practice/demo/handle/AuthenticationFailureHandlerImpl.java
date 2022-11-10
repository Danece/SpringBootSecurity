package com.springboot_practice.demo.handle;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.fasterxml.jackson.databind.ObjectMapper;

/*
 * For 處理當登入失敗後，可以採取什麼動作
 */
public class AuthenticationFailureHandlerImpl implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // response.setContentType("application/json; charset=UTF-8");
        // PrintWriter out = response.getWriter();
        // response.setStatus(404);
        // Map<String, String> result = Map.of("message", "登入失敗");
        // ObjectMapper om = new ObjectMapper();
        // out.write(om.writeValueAsString(result));
        // out.flush();
        // out.close();
        request.setAttribute("status", "loginError");
        request.getRequestDispatcher("/loginpage").forward(request, response);
    }
    
}
