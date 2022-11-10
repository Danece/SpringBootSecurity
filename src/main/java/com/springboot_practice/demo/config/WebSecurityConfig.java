package com.springboot_practice.demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.thymeleaf.spring5.SpringTemplateEngine;
import org.thymeleaf.spring5.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring5.view.ThymeleafViewResolver;

import com.springboot_practice.demo.handle.AuthenticationFailureHandlerImpl;
import com.springboot_practice.demo.handle.AuthenticationSuccessHandlerImpl;
import com.springboot_practice.demo.handle.LoginAuthenticationFilter;
import com.springboot_practice.demo.handle.MyAccessDeniedHandler;
import com.springboot_practice.demo.handle.RestAuthenticationEntryPoint;


@Configuration
@EnableWebSecurity
// Spring boot security 實作
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
	private UserDetailsService userDetailsService;
    
    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        // 登入設定
            .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .accessDeniedHandler(new MyAccessDeniedHandler())
            .and()
        // Url 設定
            .addFilterAt(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests() // 定義哪些url需要被保護
            // .antMatchers("/test").hasRole("ADMIN") // 定義匹配到"/test"底下的需要有ADMIN的這個角色才能進去
			// .antMatchers("/welcome").hasAnyRole("ADMIN", "USER") // 其他指定任意角色都可以訪問
			// .antMatchers("/main").hasAnyAuthority("manager")
            .antMatchers("/loginpage").permitAll()
            .anyRequest().authenticated() // 其他尚未匹配到的url都需要身份驗證
            .and()
        // 頁面跳轉
            .formLogin()
			.loginPage("/loginpage") // 自定義登入頁面
            .successForwardUrl("/welcome") // 登入成功後進入頁面
            // .failureForwardUrl("/time") // 登入失敗跳轉頁面
            .and()
            .httpBasic()
            .and()
        // 登出
            .logout()
			.deleteCookies("JSESSIONID")
			// .logoutSuccessUrl("/loginpage")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // 可以使用任何的 HTTP 方法登出
            .and()
        // 異常處理
            .exceptionHandling()
            //.accessDeniedPage("/異常處理頁面");  // 請自行撰寫
            .accessDeniedHandler(myAccessDeniedHandler) // 針對 API，errorCode 403
            .and()
        // 記住我
            // .rememberMe()
			// // .userDetailsService(userDetailsService)
            // .key("rem-me-key")
            // .rememberMeCookieName("remenberlogin")
			// .tokenValiditySeconds(60) // 通常都會大於 session timeout 的時間
            // .and()
        // 用新的登錄踢掉舊的登錄
            .sessionManagement()
            .maximumSessions(1) // 可接受登入上限，超過則會踢掉前一個
            .expiredUrl("/login") // 被踢掉後跳轉頁面
            // .maxSessionsPreventsLogin(true) // 禁止新的登錄
            
            ;
            http.csrf().disable(); // 方便 Postman 測試 API
    }

    /*
     * 注意！規定！要建立密碼演算的實例
     * Spring Security 強制指定你必須決定使用者的密碼你要採取哪種編碼方式
     */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

    // Thymeleaf 跳轉 JSP 設定
    @Autowired
    WebApplicationContext webApplicationContext;

    @Bean
    public SpringResourceTemplateResolver thymeleafTemplateResolver(){
        SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
        templateResolver.setApplicationContext(webApplicationContext);
        templateResolver.setOrder(9);
        templateResolver.setPrefix("/WEB-INF/pages/");
        templateResolver.setSuffix("");
        return templateResolver;
    }

    @Bean
    public SpringTemplateEngine templateEngine() {
        SpringTemplateEngine springTemplateEngine= new SpringTemplateEngine();
        springTemplateEngine.setTemplateResolver(thymeleafTemplateResolver());
        springTemplateEngine.setEnableSpringELCompiler(true);
        return springTemplateEngine;
    }

    @Bean
    public ThymeleafViewResolver thymeleafViewResolver(){
        final ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
        viewResolver.setViewNames(new String[] {"*.html"});
        viewResolver.setExcludedViewNames(new String[] {"*.jsp"});
        viewResolver.setTemplateEngine(templateEngine());
        viewResolver.setCharacterEncoding("UTF-8");
        return viewResolver;
    }

    @Bean
    public InternalResourceViewResolver jspViewResolver(){
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
        viewResolver.setOrder(10);
        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/pages/");
        viewResolver.setSuffix("");
        viewResolver.setViewNames("*.jsp");
        return viewResolver;
    }

    // 針對[禁止新登入]，當用戶被阻擋登入，主動清除 Session
    @Bean 
    HttpSessionEventPublisher httpSessionEventPublisher() { 
        return new HttpSessionEventPublisher(); 
    }

    // 自定義登入
    @Bean
    LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandlerImpl());
        filter.setFilterProcessesUrl("/api/login");
        return filter;
    }

    // filter 需要有 AuthenticationManager 的設定，但是用原本 Spring Security 的即可
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
