package com.example.u5w3d5.security;

import com.example.u5w3d5.entities.Utente;
import com.example.u5w3d5.exceptions.UnauthorizedException;
import com.example.u5w3d5.services.UtentiService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final TokenTools tokenTools;
    private final UtentiService utentiService;
    public TokenFilter(TokenTools tokenTools, UtentiService utentiService) {
        this.tokenTools = tokenTools;
        this.utentiService = utentiService;
    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new UnauthorizedException("Inserire il token nell'authorization header nel formato corretto");
        }

        String accessToken = authHeader.replace("Bearer ", "");

        tokenTools.verifyToken(accessToken);

        UUID utenteId = tokenTools.extractIdFromToken(accessToken);

        Utente authenticatedUser = utentiService.findById(utenteId);

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                authenticatedUser,
                null,
                authenticatedUser.getAuthorities()
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
