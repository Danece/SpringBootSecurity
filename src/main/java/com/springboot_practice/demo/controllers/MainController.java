package com.springboot_practice.demo.controllers;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot_practice.demo.databaseObjects.ScheduleInfo;
import com.springboot_practice.demo.repository.ScheduleInfoResponsitory;

@Controller // 表示這個 class 是控制器，不要用 Restcontroller
@RequestMapping("/") // 表示路由從 / 開始

public class MainController {
    
    @Autowired
    private ScheduleInfoResponsitory scheduleInfoResponsitory;

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
        m.addAttribute("remember_me", messageSource.getMessage( "loginpage.remember_me",  null, locale));
        m.addAttribute("btn_login", messageSource.getMessage( "str.login",  null, locale));
        return "loginpage.jsp";
    }

    @RequestMapping("/scheduleList")
    public String goScheduleList (Model m) {
        Locale locale = LocaleContextHolder.getLocale();
        MessageSource messageSource = messageSource();
        m.addAttribute("title", messageSource.getMessage( "schedule.list.title",  null, locale));
        m.addAttribute("schedule_name", messageSource.getMessage( "schedule.list.schedule_name",  null, locale));
        m.addAttribute("execution_time", messageSource.getMessage( "schedule.list.execution_time",  null, locale));
        m.addAttribute("btn_return", messageSource.getMessage("str.return", null, locale));
        m.addAttribute("btn_save", messageSource.getMessage("str.save", null, locale));
        m.addAttribute("msg_requiredValue", messageSource.getMessage("errorMsg.required_value.can_cot_be_empty", null, locale));
        m.addAttribute("msg_formatError", messageSource.getMessage("errorMsg.required_value.format_error", null, locale));
        m.addAttribute("tableContants", scheduleInfoResponsitory.findAllSchedule());

        return "schedule/scheduleList.jsp";
    }

}
