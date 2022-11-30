package com.springboot_practice.demo.handle;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

/*
 * For 當使用者透過 API 訪問工具去訪問後端 API，但實際上使用者尚未登入，所以後端直接回覆 json 格式，阻擋使用者進行未登入的操作，而非跳回傳統 Spring Security 頁面
 * 
 * 設定使用者的權限進入點，建立 login 的 Api 路由
 * 不採用傳統 Spring Security 的 Login 頁面，透過 Override commerce 這個 method，去做設定，讓它可以直接回覆 json 格式
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        // ObjectMapper mapper = new ObjectMapper();
        // Map<String, String> map = Map.of("error", "請先登入才可進行操作");
        // String error = mapper.writeValueAsString(map);
        // response.setContentType("application/json;  charset=UTF-8");
        // response.setCharacterEncoding("UTF-8");
        // response.setStatus(response.SC_UNAUTHORIZED);
        // PrintWriter writer = response.getWriter();
        // writer.write(error);
        // writer.flush();
        // writer.close();
        request.setAttribute("status", "entryPintError");
        request.getRequestDispatcher("/loginpage").forward(request, response);
    }
    
}
