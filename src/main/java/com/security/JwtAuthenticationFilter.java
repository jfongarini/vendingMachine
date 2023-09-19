package com.security;

import com.dao.UserDao;
import com.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String INVALID_TOKEN = "Invalid connection TOKEN";
    private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDao userDao;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String token = getToken(request);

        if (token == null){
            filterChain.doFilter(request,response);
            LOGGER.info(INVALID_TOKEN);
            return;
        }
        String userId = jwtService.getUserNameFromToken(token);

        if (Objects.nonNull(userId) && Objects.isNull(SecurityContextHolder.getContext().getAuthentication())){
            Optional<User> user = userDao.findById(Integer.parseInt(userId));
            if (user.isEmpty()){
                LOGGER.info(INVALID_TOKEN);
                return;
            }
            if (jwtService.isTokenValid(token,user.get())){
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user,null,user.get().getAuthorities());
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                LOGGER.info("Validated connection TOKEN");
            }
        }

        filterChain.doFilter(request,response);
    }

    private String getToken(HttpServletRequest request){

        LOGGER.info("Validating connection TOKEN");

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")){
            return authHeader.substring(7);
        }
        return null;
    }
}
