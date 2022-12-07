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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.springboot_practice.demo.databaseObjects.AuthorityInfo;
import com.springboot_practice.demo.databaseObjects.RoleInfo;
import com.springboot_practice.demo.repository.AuthorityInfoResponsitory;
import com.springboot_practice.demo.repository.RoleInfoResponsitory;
import com.springboot_practice.demo.repository.ScheduleInfoResponsitory;

import net.minidev.json.JSONObject;

@Controller // 表示這個 class 是控制器，不要用 Restcontroller
@RequestMapping("/") // 表示路由從 / 開始

public class MainController {
    
    @Autowired
    private ScheduleInfoResponsitory scheduleInfoResponsitory;

    @Autowired
    private RoleInfoResponsitory roleInfoResponsitory;

    @Autowired
    private AuthorityInfoResponsitory authorityInfoResponsitory;

    /*
     * For i18N
     */
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        
        messageSource.setBasename("classpath:i18N/messages");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }
    
    Locale locale = LocaleContextHolder.getLocale();
    MessageSource messageSource = messageSource();

    @GetMapping("/lang")
    public ResponseEntity getUserInfo(@RequestParam HashMap request) {
        JSONObject body = new JSONObject();
        body.put("lang", this.locale.toString());
        return ResponseEntity.status(HttpStatus.OK).body(body);
    }

    @PostMapping("/lang") // 表示路由改成 /cnm/wc 開始
    public String changeLang (@RequestBody HashMap request) {
        switch(request.get("lang").toString()) {
            case "zh_TW":
                this.locale = Locale.TAIWAN;
                break;
            case "en_US":
                this.locale = Locale.US;
                break;
            default:
                this.locale = Locale.TAIWAN;
        }
        return "loginpage.jsp";
    }


    // 跳轉頁面作法一
    @RequestMapping(value="/time") // 表示路由改成 /cnm/wc 開始
    public String goShowTime (Model m) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd hh:mm");
        m.addAttribute("now", sdf.format(new Date()));
        return "test.jsp"; // 將 View 定向到 test.jsp，非字串
    }

    @RequestMapping("/loginpage")
    public String goLogin (Model m) {
        m = setLabels(m);
        return "loginpage.jsp";
    }

    // 跳轉頁面作法二
    @RequestMapping("/welcome")
    public String goMainPage (@RequestParam(value = "name", defaultValue = "Danece") String name, Model m) {
        // UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        m = setLabels(m);
        return "welcome.jsp";
    }

    @RequestMapping("/scheduleList")
    public String goScheduleList (Model m) {
        m = setLabels(m);

        return "schedule/scheduleList.jsp";
    }

    @RequestMapping("/userList")
    public String goUserList (Model m) {
        m = setLabels(m);
        // Role
        RoleInfo[] roles = roleInfoResponsitory.findAllRole();
        ArrayList roleValueList = new ArrayList<>();
        ArrayList roleNameList = new ArrayList<>();
        for (int i=0; i<roles.length; i++) {
            roleValueList.add(roles[i].getName());
            switch(roles[i].getName()) {
                case "manager":
                    roleNameList.add(messageSource.getMessage("role.manager", null, locale));
                    break;
                case "employee":
                    roleNameList.add(messageSource.getMessage("role.employee", null, locale));
                    break;
            }
        }
        // Authority
        AuthorityInfo[] auths = authorityInfoResponsitory.findAllAuthority();
        ArrayList authValueList = new ArrayList<>();
        ArrayList authNameList = new ArrayList<>();
        for (int j=0; j<auths.length; j++) {
            authValueList.add(auths[j].getName());
            switch(auths[j].getName()) {
                case "admin":
                    authNameList.add(messageSource.getMessage("authority.admin", null, locale));
                    break;
                case "normal":
                    authNameList.add(messageSource.getMessage("authority.normal", null, locale));
                    break;
            }
        }
        m.addAttribute("roleValueList", roleValueList);
        m.addAttribute("roleNameList", roleNameList);
        m.addAttribute("authValueList", authValueList);
        m.addAttribute("authNameList", authNameList);
        // m.addAttribute("authorityList", authorityInfoResponsitory.findAllAuthority());
        return "user/userList.jsp";
    }

    private Model setLabels (Model m) {
        
        m.addAttribute("LANG_TW", messageSource.getMessage( "lang.option.tw",  null, locale));
        m.addAttribute("LANG_EN", messageSource.getMessage( "lang.option.en",  null, locale));
        
        m.addAttribute("LOGIN_TITLE", messageSource.getMessage( "loginpage.title",  null, locale));
        m.addAttribute("WELCOME_TITLE", messageSource.getMessage( "welcomepage.title",  null, locale));
        m.addAttribute("USERLIST_TITLE", messageSource.getMessage( "user.list.title",  null, locale));
        m.addAttribute("SCHEDULE_TITLE", messageSource.getMessage( "schedule.list.title",  null, locale));
        
        m.addAttribute("PLACEHOLDER_ACCOUNT", messageSource.getMessage( "loginpage.placeholder.account",  null, locale));
        m.addAttribute("PLACEHOLDER_PASSWORD", messageSource.getMessage( "loginpage.placeholder.password",  null, locale));
        
        m.addAttribute("REMEMBER_ME", messageSource.getMessage( "loginpage.remember_me",  null, locale));
        m.addAttribute("ACCOUNT", messageSource.getMessage( "str.account",  null, locale));
        m.addAttribute("PASSWORD", messageSource.getMessage( "str.password",  null, locale));
        m.addAttribute("ROLE_MANAGER", messageSource.getMessage("role.manager", null, locale));
        m.addAttribute("ROLE_EMPLOYEE", messageSource.getMessage("role.employee", null, locale));
        m.addAttribute("LANGUAGE", messageSource.getMessage("str.language", null, locale));
        m.addAttribute("AUTHORITY", messageSource.getMessage("str.authority", null, locale));
        m.addAttribute("ROLE", messageSource.getMessage("str.role", null, locale));
        m.addAttribute("AUTHORITY_ADMIN", messageSource.getMessage("authority.admin", null, locale));
        m.addAttribute("AUTHORITY_NORMAL", messageSource.getMessage("authority.normal", null, locale));
        m.addAttribute("SUCCESS", messageSource.getMessage("str.success", null, locale));
        m.addAttribute("FAILED", messageSource.getMessage("str.failed", null, locale));
       
        m.addAttribute("WELCOME_ROW_ACCOUNT", messageSource.getMessage( "welcomepage.row.account",  null, locale));
        m.addAttribute("WELCOME_ROW_USERNAME", messageSource.getMessage( "welcomepage.row.user_name",  null, locale));
        m.addAttribute("WELCOME_ROW_CREDENTIAL", messageSource.getMessage( "welcomepage.row.credentials",  null, locale));
        m.addAttribute("WELCOME_ROW_AUTHRITIES", messageSource.getMessage( "welcomepage.row.authorities",  null, locale));
        m.addAttribute("WELCOME_ROW_REMOTEADDRESS", messageSource.getMessage( "welcomepage.row.remote_address",  null, locale));
        m.addAttribute("WELCOME_ROW_SESSIONID", messageSource.getMessage( "welcomepage.row.session_id",  null, locale));
        m.addAttribute("WELCOME_ROW_JWTACCESSTOKEN", messageSource.getMessage( "welcomepage.row.jwt_access_token",  null, locale));
        m.addAttribute("USERLIST_ROW_USER_NAME", messageSource.getMessage( "user.list.name",  null, locale));
        m.addAttribute("USERLIST_ROW_AUTHORITY", messageSource.getMessage( "user.list.autority",  null, locale));
        m.addAttribute("SCHEDULELIST_ROW_SCHEDULE_NAME", messageSource.getMessage( "schedule.list.schedule_name",  null, locale));
        m.addAttribute("SCHEDULELIST_ROW_EXECUTION_TIME", messageSource.getMessage( "schedule.list.execution_time",  null, locale));
        
        m.addAttribute("BTN_LOGIN", messageSource.getMessage( "str.login",  null, locale));
        m.addAttribute("BTN_LOGOUT", messageSource.getMessage("str.logout", null, locale));
        m.addAttribute("BTN_EXPORT_JWT", messageSource.getMessage("str.export_jwt", null, locale));
        m.addAttribute("BTN_SCHEDULE_MANAGER", messageSource.getMessage("str.schedule_manager", null, locale));
        m.addAttribute("BTN_USER_MANAGER", messageSource.getMessage("str.user_manager", null, locale));
        m.addAttribute("BTN_TEST", messageSource.getMessage("str.test", null, locale));
        m.addAttribute("BTN_SEARCH", messageSource.getMessage("str.search", null, locale));
        m.addAttribute("BTN_RETURN", messageSource.getMessage("str.return", null, locale));
        m.addAttribute("BTN_SAVE", messageSource.getMessage("str.save", null, locale));
        m.addAttribute("BTN_CREATE", messageSource.getMessage("str.create", null, locale));
        m.addAttribute("BTN_CLOSE", messageSource.getMessage("str.close", null, locale));
        m.addAttribute("BTN_DELETE", messageSource.getMessage("str.delete", null, locale));
        m.addAttribute("BTN_CANCEL", messageSource.getMessage("str.cancel", null, locale));
        m.addAttribute("BTN_SURE", messageSource.getMessage("str.sure", null, locale));
        
        m.addAttribute("MSG_LOGINIDENTITY", messageSource.getMessage("str.login_identity", null, locale));
        m.addAttribute("MSG_HELLO", messageSource.getMessage( "welcomepage.msg.hello",  null, locale));
        m.addAttribute("MSG_REQUIREDVALUE", messageSource.getMessage("errorMsg.required_value.can_cot_be_empty", null, locale));
        m.addAttribute("MSG_FORMATERROR", messageSource.getMessage("errorMsg.required_value.format_error", null, locale));
        m.addAttribute("MSG_LOGIN_FAILED", messageSource.getMessage("errorMsg.login.failed", null, locale));
        m.addAttribute("MSG_NEDD_LOGIN", messageSource.getMessage("errorMsg.login.need_login", null, locale));
        
        m.addAttribute("MODAL_CREATE_USER", messageSource.getMessage("modal.create_user", null, locale));
        m.addAttribute("MODAL_DELETE_USER", messageSource.getMessage("modal.delete_user", null, locale));
        m.addAttribute("MODAL_DELETE_USER_MSG", messageSource.getMessage("modal.delete_user.msg", null, locale));
        
        return m;
    }
}
