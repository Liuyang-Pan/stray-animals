package cn.basicPLY.animals.config;

import cn.basicPLY.animals.entity.CertificationUserDetails;
import cn.basicPLY.animals.service.impl.StrayAnimalsUserDetailsServiceImpl;
import cn.basicPLY.animals.utils.AjaxResult;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.*;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.PrintWriter;

/**
 * purpose:
 *
 * @author Pan Liuyang
 * 2022/4/5 22:39
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private StrayAnimalsUserDetailsServiceImpl strayAnimalsUserDetailsService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 登录授权配置
     * 使用自定义UserDetailsService获取用户
     *
     * @param auth 认证
     * @throws Exception 异常
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(strayAnimalsUserDetailsService);
    }

    /**
     * 定义角色继承关系
     *
     * @return RoleHierarchyImpl角色关系继承实现
     */
    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_admin > ROLE_user");
        return hierarchy;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/js/**", "/css/**", "/images/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/*/admin/**").hasRole("admin")
                .antMatchers("/*/user/**").hasRole("user")
                .antMatchers("/certification/**", "/file/**", "/swagger-ui/**",
                        "/swagger-resources/**", "/webjars/**", "/v2/**", "/api/**", "/v3/**")
                .permitAll() //permitAll() /certification/**所以请求均可访问
                .anyRequest().authenticated() //authenticated()需要认证访问
                .and()
                .formLogin()
                .loginPage("/login.html").loginProcessingUrl("/doLogin")
                .usernameParameter("name").passwordParameter("password")
                //登录成功逻辑
                .successHandler((request, response, authentication) -> {
                    //获取用户 设置密码为空返回
                    CertificationUserDetails principal = (CertificationUserDetails) authentication.getPrincipal();
                    principal.setPassword(null);
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write(new ObjectMapper().writeValueAsString(AjaxResult.success("登录成功", principal)));
                    out.flush();
                    out.close();
                })
                //登录失败逻辑
                .failureHandler((request, response, exception) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    AjaxResult ajaxResult = null;
                    if (exception instanceof LockedException) {
                        ajaxResult = AjaxResult.error("账户被锁定，请联系管理员!");
                    } else if (exception instanceof CredentialsExpiredException) {
                        ajaxResult = AjaxResult.error("密码过期，请联系管理员!");
                    } else if (exception instanceof AccountExpiredException) {
                        ajaxResult = AjaxResult.error("密码过期，请联系管理员!");
                    } else if (exception instanceof DisabledException) {
                        ajaxResult = AjaxResult.error("账户被禁用，请联系管理员!");
                    } else if (exception instanceof BadCredentialsException) {
                        ajaxResult = AjaxResult.error("用户名或者密码输入错误，请重新输入!");
                    }
                    out.write(new ObjectMapper().writeValueAsString(ajaxResult));
                    out.flush();
                    out.close();
                })
                .and()
                .logout()
                .logoutUrl("/logout")
                .logoutSuccessHandler((request, response, authentication) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write("注销成功");
                    out.flush();
                    out.close();
                })
                .permitAll()
                .and()
                .csrf().disable()
                .exceptionHandling()
                //未授权的返回信息
                .authenticationEntryPoint((request, response, authException) -> {
                    response.setContentType("application/json;charset=utf-8");
                    PrintWriter out = response.getWriter();
                    out.write("尚未登录，请先登录");
                    out.flush();
                    out.close();
                });
    }
}
