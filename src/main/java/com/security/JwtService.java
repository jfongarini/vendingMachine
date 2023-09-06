package com.security;

import com.model.Operation;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;


@Service
public class JwtService {

    private static final String SECRET_KEY="586E3272357538782F413F4428472B4B6250655368566B597033733676397924";
    public String getToken(Operation operation){
        return getToken(new HashMap<>(),operation);
    }

    private String getToken(HashMap<String, Object> extraClaims,Operation operation) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(String.valueOf(operation.getOperationId()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(operation.getUser().getExpirationDate())
                .signWith(getKey())
                .compact();
    }

    private Key getKey(){
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public String getUserNameFromToken(String token){
        token = token.replace("Bearer ", "");
        return getClaims(token, Claims::getSubject);
    }

    public boolean isTokenValid(String token, Operation operation){
        final String username = getUserNameFromToken(token);
        return (username.equals(String.valueOf(operation.getOperationId())) && !isTokenExpired(token));
    }

    private Claims getAllClaims(String token){
        return Jwts
                .parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T getClaims(String token, Function<Claims,T> claimsResolver){
        final Claims claims = getAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Date getExpirationDate(String token){
        return getClaims(token, Claims::getExpiration);
    }

    private boolean isTokenExpired(String token){
        return getExpirationDate(token).before(new Date());
    }
}
