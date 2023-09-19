package com.security;

import com.util.enums.UserEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class WebSecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        return http
                .csrf().disable()
                .authorizeHttpRequests(ar -> ar.requestMatchers(
                        "/api/operations",
                                "/api/vending-machine/**/login",
                                "/swagger-ui/**",
                                "/swagger/**",
                                "/v3/api-docs/**",
                                "/swagger.json",
                                "/swagger-resources/**",
                                "/webjars/**",
                                "/configuration/**").permitAll()
                        .requestMatchers("/api/operation/**").hasRole(UserEnum.USER.name())
                        .requestMatchers("/api/vending-machine/**").hasRole(UserEnum.ADMIN.name())
                        .requestMatchers("/api/coin/**").hasRole(UserEnum.ADMIN.name())
                        .requestMatchers("/api/product/**").hasRole(UserEnum.ADMIN.name())
                        .anyRequest().authenticated())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();

    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

}
