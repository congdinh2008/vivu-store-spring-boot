package com.congdinh.vivustore.services;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Service
public class TokenServiceImpl implements TokenService {
    @Value("${app.security.access-token-secret-key}")
    private String secretKey;

    @Value("${app.security.access-token-expired-in-second}")
    private Integer expireTime;

    public String generateAccessToken(Authentication authentication) {
        String roles = authentication.getAuthorities().stream().map(Object::toString).collect(Collectors.joining(","));

        return generateAccessTokenByRoles(authentication.getName(), roles);
    }

    private String generateAccessTokenByRoles(String name, String roles) {
        LocalDateTime expiredAt = LocalDateTime.now().plusSeconds(expireTime);

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        Date expiration = Date.from(expiredAt.atZone(ZoneId.systemDefault()).toInstant());

        return Jwts.builder()
                .subject(name)
                .claim("roles", roles)
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    private Authentication parseAccessToken(String accessToken) {
        if (accessToken == null) {
            return null;
        }

        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));

        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(accessToken)
                    .getPayload();

            String roles = claims.get("roles").toString();

            Set<GrantedAuthority> grantedAuthorities = Set.of(roles.split(","))
                    .stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toSet());

            User principal = new User(claims.getSubject(), "", grantedAuthorities);

            return new UsernamePasswordAuthenticationToken(principal, null, grantedAuthorities);
        } catch (Exception e) {
            return null;
        }
    }

    public Authentication getAuthentication(String jwtToken) {
        return parseAccessToken(jwtToken);
    }
}
