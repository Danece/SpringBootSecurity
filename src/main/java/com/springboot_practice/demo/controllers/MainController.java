package com.springboot_practice.demo.controllers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String goMainPage (@RequestParam(value = "name", defaultValue = "Danece") String name, Model m) {
        // UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Locale locale = LocaleContextHolder.getLocale();
        MessageSource messageSource = messageSource();
        m.addAttribute("title", messageSource.getMessage( "welcomepage.title",  null, locale));
        m.addAttribute("msg_hello", messageSource.getMessage( "welcomepage.msg.hello",  null, locale));
        m.addAttribute("row_account", messageSource.getMessage( "welcomepage.row.account",  null, locale));
        m.addAttribute("row_userName", messageSource.getMessage( "welcomepage.row.user_name",  null, locale));
        m.addAttribute("row_credwntials", messageSource.getMessage( "welcomepage.row.credentials",  null, locale));
        m.addAttribute("row_authorities", messageSource.getMessage( "welcomepage.row.authorities",  null, locale));
        m.addAttribute("row_remoteAddress", messageSource.getMessage( "welcomepage.row.remote_address",  null, locale));
        m.addAttribute("row_sessionId", messageSource.getMessage( "welcomepage.row.session_id",  null, locale));
        m.addAttribute("row_jwtAccessToken", messageSource.getMessage( "welcomepage.row.jwt_access_token",  null, locale));
        m.addAttribute("btn_logout", messageSource.getMessage("str.logout", null, locale));
        m.addAttribute("export_jwt", messageSource.getMessage("str.export_jwt", null, locale));
        m.addAttribute("schedule_manager", messageSource.getMessage("str.schedule_manager", null, locale));
        m.addAttribute("user_manager", messageSource.getMessage("str.user_manager", null, locale));
        m.addAttribute("test", messageSource.getMessage("str.test", null, locale));
        m.addAttribute("msg_loginIdentity", messageSource.getMessage("str.login_identity", null, locale));
        m.addAttribute("role_manager", messageSource.getMessage("role.manager", null, locale));
        m.addAttribute("role_employee", messageSource.getMessage("role.employee", null, locale));
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

    @RequestMapping("/userList")
    public String goUserList (Model m) {
        Locale locale = LocaleContextHolder.getLocale();
        MessageSource messageSource = messageSource();
        m.addAttribute("title", messageSource.getMessage( "user.list.title",  null, locale));
        m.addAttribute("user_name", messageSource.getMessage( "user.list.name",  null, locale));
        m.addAttribute("authority", messageSource.getMessage( "user.list.autority",  null, locale));
        m.addAttribute("btn_search", messageSource.getMessage("str.search", null, locale));
        m.addAttribute("btn_return", messageSource.getMessage("str.return", null, locale));
        
        return "user/userList.jsp";
    }
}
