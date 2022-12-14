package com.springboot_practice.demo.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.rememberme.JdbcTokenRepositoryImpl;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
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
import com.springboot_practice.demo.service.DailyScheduleService;
import com.springboot_practice.demo.service.UserDetailsServiceImpl;;

@Configuration
@EnableWebSecurity
// Spring boot security ??????
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private DataSource dataSource; //?????????

    @Autowired
    private DailyScheduleService dailyScheduleService;
    
    // For Remember me
    @Bean
    public PersistentTokenRepository tokenRepository() {
        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
        tokenRepository.setDataSource(dataSource); //???????????????
        // tokenRepository.setCreateTableOnStartup(true); //??????????????????????????????????????????
        return tokenRepository;
    }

    @Autowired
    private MyAccessDeniedHandler myAccessDeniedHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
        // ????????????
            .exceptionHandling()
            .authenticationEntryPoint(new RestAuthenticationEntryPoint())
            .accessDeniedHandler(new MyAccessDeniedHandler())
            .and()
        // Url ??????
            .addFilterAt(loginAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests() // ????????????url???????????????
            // .antMatchers("/test").hasRole("ADMIN") // ???????????????"/test"??????????????????ADMIN???????????????????????????
			// .antMatchers("/welcome").hasAnyRole("ADMIN", "USER") // ???????????????????????????????????????
			// .antMatchers("/main").hasAnyAuthority("manager")
            .antMatchers("/loginpage").permitAll()
            .antMatchers("/lang").permitAll()
            .antMatchers("/static/global.css").permitAll()
            .antMatchers("/scripts/global_function.js").permitAll()
            .anyRequest().authenticated() // ????????????????????????url?????????????????????
            .and()
        // ????????????
            .formLogin()
			.loginPage("/loginpage") // ?????????????????????
            .successForwardUrl("/welcome") // ???????????????????????????
            // .failureForwardUrl("/time") // ????????????????????????
            .and()
            .httpBasic()
            .and()
        // ??????
            .logout()
			.deleteCookies("JSESSIONID")
			.logoutSuccessUrl("/loginpage")
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout")) // ????????????????????? HTTP ????????????
            .and()
        // ????????????
            .exceptionHandling()
            //.accessDeniedPage("/??????????????????");  // ???????????????
            .accessDeniedHandler(myAccessDeniedHandler) // ?????? API???errorCode 403
            .and()
        // ?????????
            .rememberMe()
			// .userDetailsService(userDetailsService) //??????userDetailsService 
            .tokenRepository(tokenRepository())  //?????????????????????
            .tokenValiditySeconds(60 * 60)  //??????????????????(???) 
            .rememberMeServices(beanRememberMeServices())
            .and()
        // ?????????????????????????????????
            .sessionManagement()
            .maximumSessions(1) // ???????????????????????????????????????????????????
            .expiredUrl("/login") // ????????????????????????
            // .maxSessionsPreventsLogin(true) // ??????????????????
            
            ;
        http.csrf().disable(); // ?????? Postman ?????? API

        dailyScheduleService.setSchedule();
    }

    /*
     * ????????????????????????????????????????????????
     * Spring Security ???????????????????????????????????????????????????????????????????????????
     */
	@Bean
	public PasswordEncoder getPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

    // Thymeleaf ?????? JSP ??????
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

    // ??????[???????????????]?????????????????????????????????????????? Session
    @Bean 
    HttpSessionEventPublisher httpSessionEventPublisher() { 
        return new HttpSessionEventPublisher(); 
    }

    // For Remember me
    @Bean
    public RememberMeServices beanRememberMeServices() {
        // ???????????? key ??? ???????????????????????????
        TokenBasedRememberMeServices service = new TokenBasedRememberMeServices("remember-me", userDetailsServiceImpl);
        service.setParameter("remember-me");
        service.setTokenValiditySeconds(60*60); // ?????? : ?????????

        return service;
    }

    // ???????????????
    @Bean
    LoginAuthenticationFilter loginAuthenticationFilter() throws Exception {
        LoginAuthenticationFilter filter = new LoginAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManagerBean());
        filter.setAuthenticationSuccessHandler(new AuthenticationSuccessHandlerImpl());
        filter.setAuthenticationFailureHandler(new AuthenticationFailureHandlerImpl());
        filter.setRememberMeServices(beanRememberMeServices());
        filter.setFilterProcessesUrl("/api/login");
        return filter;
    }

    // filter ????????? AuthenticationManager ??????????????????????????? Spring Security ?????????
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}
