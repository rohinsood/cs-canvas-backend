package com.nighthawk.spring_portfolio.security;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()  // Cross-Site Request Forgery disable for JS Fetch URIs
            .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/api/grade/predict").permitAll()
                .antMatchers(HttpMethod.GET, "/api/grade/predict").permitAll()  // Allow GET requests
                .anyRequest().authenticated();
    }
}
