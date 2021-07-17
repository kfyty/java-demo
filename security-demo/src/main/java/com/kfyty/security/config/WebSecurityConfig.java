package com.kfyty.security.config;

import com.kfyty.security.config.security.LogoutRedisHandler;
import com.kfyty.security.config.security.PermissionDecisionManager;
import com.kfyty.security.config.security.TokenAuthenticationFilter;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.LinkedList;
import java.util.List;

@Configuration
@ConfigurationProperties(prefix = "spring.security")
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Getter @Setter
    private List<String> anonymous = new LinkedList<>();

    @Autowired
    private LogoutRedisHandler logoutRedisHandler;

    @Autowired
    private TokenAuthenticationFilter tokenAuthenticationFilter;

    @Autowired
    private PermissionDecisionManager permissionDecisionManager;

    /**
     * 授权失败：匿名用户
     */
    @Bean
    public AuthenticationEntryPoint loginUrlAuthenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint("/login.html");
    }

    /**
     * 授权失败：已登录用户
     */
    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        AccessDeniedHandlerImpl accessDeniedHandler = new AccessDeniedHandlerImpl();
        accessDeniedHandler.setErrorPage("/refuse.html");
        return accessDeniedHandler;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .formLogin().loginPage("/login.html").permitAll()
                .and()
                .logout().logoutUrl("/passport/logout").addLogoutHandler(logoutRedisHandler).permitAll()
                .and()
                .authorizeRequests()
                .antMatchers(this.anonymous.toArray(new String[0])).permitAll()
                .anyRequest().authenticated()
                .accessDecisionManager(permissionDecisionManager)
                .and()
                .addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .accessDeniedHandler(accessDeniedHandler());
    }
}
