package com.exp.cl.config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.exp.cl.user.repository.UserRepository;
import com.exp.cl.utils.JWTUtils;

import lombok.AllArgsConstructor;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    
    private final JWTUtils jwtUtils;
    private final UserRepository userRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .csrf().disable()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            .authorizeRequests()
            .antMatchers(
                "/api/v1/login",
            "/api/v1/user",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v2/api-docs/**",
            "/webjars/**",
            "/swagger-resources/**",
            "/configuration/**").permitAll()
            .antMatchers(HttpMethod.POST, "/api/v1/user/").permitAll()    
            .anyRequest().authenticated()
            .and()
            .addFilterBefore(new JWTAuthenticationFilter(jwtUtils, userRepository), UsernamePasswordAuthenticationFilter.class);
    }
     

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        // No in-memory or JDBC users here; you use token auth only
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
}