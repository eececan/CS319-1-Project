package com.project.btoproject.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JWTGenerator tokenGenerator;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Check if the SecurityContext already has an authenticated user
        if (SecurityContextHolder.getContext().getAuthentication() == null) {
            String token = getJWTFromRequest(request);

            if (StringUtils.hasText(token) && tokenGenerator.validateToken(token)) {
                String username = tokenGenerator.getUsernameFromJWT(token);

                // Load user details from CustomUserDetailsService
                UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

                // Create authentication token
                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                // Set details from the current HTTP request
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Populate SecurityContext with the new authentication
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Proceed with the filter chain
        filterChain.doFilter(request, response);
    }

    // Helper method to extract JWT from the Authorization header
    private String getJWTFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
