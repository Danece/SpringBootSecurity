package com.springboot_practice.demo.controllers;

import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;

// import org.apache.http.client.methods.CloseableHttpResponse;
// import org.apache.http.client.methods.HttpPost;
// import org.apache.http.entity.StringEntity;
// import org.apache.http.impl.client.CloseableHttpClient;
// import org.apache.http.impl.client.HttpClients;
// import org.apache.http.util.EntityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

import java.util.Locale;

@Controller // 表示這個 class 是控制器，不要用 Restcontroller
@RequestMapping("/") // 表示路由從 / 開始

public class MainController {
    
    /*
     * For i18N
     */
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        
        messageSource.setBasename("classpath:i18N/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    // 跳轉頁面作法一
    @RequestMapping(value="/time") // 表示路由改成 /cnm/wc 開始
    public String goShowTime (Model m) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        m.addAttribute("now", sdf.format(new Date()));
        return "test.jsp"; // 將 View 定向到 test.jsp，非字串
    }

    // 跳轉頁面作法二
    @RequestMapping("/welcome")
    public String goMainPage (@RequestParam(value = "name", defaultValue = "Danece") String name) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        // m.addAttribute("accessToken", getStubResponse);
        // =============================================================================
        /* 
         * 目的 : 在進到登入後頁面時，執行API取得AccessToken
         * 狀況 : 卡在 {"error":"請先登入才可進行操作"} 
        */
        // String result = "";
        // HttpPost post = new HttpPost("http://localhost:8080/api/accessToken");
        // String valueToEncode = userDetails.getUsername() + ":admin";
        // StringBuilder json = new StringBuilder();
        // json.append("{");
        // json.append("\"userName\":\"" + userDetails.getUsername() + "\",");
        // json.append("}");
        // try {
        //     post.setEntity(new StringEntity(json.toString()));
        //     post.addHeader("basic", Base64.getEncoder().encodeToString(valueToEncode.getBytes()));
        //     CloseableHttpClient httpClient = HttpClients.createDefault() ;
        //     CloseableHttpResponse response = httpClient.execute(post);
        //     System.out.println("[goMainPage-post] " + Base64.getEncoder().encodeToString(valueToEncode.getBytes()).toString());
        //     result = EntityUtils.toString(response.getEntity());
        // } catch (Exception e) {
        //     e.printStackTrace();
        // }
        // System.out.println("[goMainPage-result] " + result);
        // =============================================================================
        System.out.println("[goMainPage] " + userDetails.getUsername());
        return "welcome.jsp";
    }

    @RequestMapping("/loginpage")
    public String goLogin (Model m) {
        Locale locale = LocaleContextHolder.getLocale();
        MessageSource messageSource = messageSource();
        m.addAttribute("title", messageSource.getMessage( "loginpage.title",  null, locale));
        m.addAttribute("placeholder_account", messageSource.getMessage( "loginpage.placeholder.account",  null, locale));
        m.addAttribute("placeholder_password", messageSource.getMessage( "loginpage.placeholder.password",  null, locale));
        m.addAttribute("btn_login", messageSource.getMessage( "str.login",  null, locale));
        return "loginpage.jsp";
    }

    private static final String getBasicAuthenticationHeader(String username, String password) {
        String valueToEncode = username + ":" + password;
        return "Basic " + Base64.getEncoder().encodeToString(valueToEncode.getBytes());
    }

}
