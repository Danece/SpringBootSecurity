package com.springboot_practice.demo.handle;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.NullRememberMeServices;
import org.springframework.security.web.authentication.RememberMeServices;

/*
 * For 處理當登入成功後，可以採取什麼動作
 * 
 * session 去存放 account、authority，以便之後如何判斷過來的 request
 */
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {
    
    private final String LOGGED_IN = "logged_in";
    private final String USER_TYPE = "user_type";

    private RememberMeServices rememberMeServices = new NullRememberMeServices();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String account = authentication.getName();
        Collection collection = authentication.getAuthorities();
        String authority = collection.iterator().next().toString();
        HttpSession session = request.getSession();
        session.setAttribute(LOGGED_IN, account);
        session.setAttribute(USER_TYPE, authority);
        // Map<String, String> result = new HashMap<>();
        // result.put("authority", authority);
        // response.setContentType("application/json; charset=UTF-8");
        // PrintWriter out = response.getWriter();
        // response.setStatus(200);
        // ObjectMapper om = new ObjectMapper();
        // out.write(om.writeValueAsString(result));
        // out.flush();
        // out.close();

        //登錄成功後，調用RememberMeServices保存Token相關信息
        rememberMeServices.loginSuccess(request, response, authentication);

        request.getRequestDispatcher("/welcome").forward(request, response);
		return;
    }
}
