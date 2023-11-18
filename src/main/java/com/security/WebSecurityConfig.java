package com.security;

import com.util.enums.UserEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(ar -> ar.requestMatchers(
                                "/*.html",
                                "/js/*",
                                "/css/*",
                                "/api/operations",
                                "/api/vending-machines/login/**",
                                "/swagger-ui/**",
                                "/swagger/**",
                                "/v3/api-docs/**",
                                "/swagger.json",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/configuration/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/vending-machines").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/vending-machines").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/coins").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/products").permitAll()
                        .requestMatchers("/api/operations/**").hasRole(UserEnum.USER.name())
                        .requestMatchers("/api/vending-machines/**").hasRole(UserEnum.ADMIN.name())
                        .requestMatchers("/api/coins/**").hasRole(UserEnum.ADMIN.name())
                        .requestMatchers("/api/products/**").hasRole(UserEnum.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

}
