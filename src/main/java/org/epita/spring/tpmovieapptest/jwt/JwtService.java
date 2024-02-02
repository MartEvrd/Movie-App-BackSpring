package org.epita.spring.tpmovieapptest.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {
    @Value("${JWT_SECRET}")
    private String secret;

    private SecretKey key;
    private final UserDetailsService userDetailsService;

    public JwtService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @PostConstruct
    private void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        System.out.println("key = "+key);
    }

    public Map<String, String> generateToken(String email) {
        UserDetails user = userDetailsService.loadUserByUsername(email);
        return this.generateJwt(user);
    }

    public Map<String, String> generateJwt(UserDetails user){
        final long currentTime = System.currentTimeMillis();
        final Long expirationTime = currentTime + 30 * 60 * 1000;

        Map<String, Object> claims = Map.of(
                "email", user.getUsername(),
                Claims.EXPIRATION, new Date(expirationTime),
                Claims.SUBJECT, user.getUsername()
        );
        System.out.println(claims);

        final String token = Jwts.builder()
                .issuedAt(new Date(currentTime))
                .expiration(new Date(expirationTime))
                .subject(user.getUsername())
                .claims(claims)
                .signWith(key)
                .compact();

        return Map.of("bearer", token);
    }

    public String extractUserName(String jwtToken){
        return getPayLoad(jwtToken).getSubject();
    }

    public boolean isTokenExpired(String jwtToken) {
        Date expirationTime = new Date();
        return expirationTime.after(getPayLoad(jwtToken).getExpiration());
    }

    private Claims getPayLoad(String jwtToken){
        return (Claims) Jwts.parser().verifyWith(key).build().parse(jwtToken).getPayload();
    }

    public void newSession(String jwtToken) {

        String email = this.extractUserName(jwtToken);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails user = userDetailsService.loadUserByUsername(email);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                    user, null, user.getAuthorities()
            );
            SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
        }
    }
}
