package org.epita.spring.tpmovieapptest.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Map;

@Service
public class JwtFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;

    public JwtFilter(JwtService jwtService, UserDetailsService userDetailsService, ObjectMapper objectMapper) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwtToken = null;
            boolean isTokenExpired = true;
            String email = null;

            String bearer = request.getHeader("Authorization");

            if (bearer != null && bearer.startsWith("Bearer ") && bearer.length() > 7) {
                jwtToken = bearer.substring(7);
                isTokenExpired = jwtService.isTokenExpired(jwtToken);
                email = jwtService.extractUserName(jwtToken);
            }
            /* * La 3è condition vérifie si l'authentification n'est pas déjà gérée par ailleurs.
             * Elle permet d'accéder aux détails de l'authentification de l'utilisateur actuellement authentifié.
             * Si elle est à null, cela veut donc dire qu'il n'y a pas encore d'authentificataion.*/
            if (!isTokenExpired && email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = userDetailsService.loadUserByUsername(email);
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
//           ->  FINALITÉ : Mise en place de l'authentification
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
//        Passe au filtre suivant en transmettant la requête + la réponse
            filterChain.doFilter(request, response);
        } catch (ExpiredJwtException e) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "JWT Expired")));
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(Map.of("error", "Invalid JWT")));
        }
    }
}
