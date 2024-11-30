package com.iteletric.iteletricapi.config.security.userauthentication;

import com.iteletric.iteletricapi.config.security.JwtTokenService;
import com.iteletric.iteletricapi.config.security.SecurityConfiguration;
import com.iteletric.iteletricapi.models.User;
import com.iteletric.iteletricapi.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class UserAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    UserAuthenticationFilter(JwtTokenService jwtTokenService, UserRepository userRepository) {
        this.jwtTokenService = jwtTokenService;
        this.userRepository = userRepository;
    }

    private final JwtTokenService jwtTokenService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (checkIfEndpointIsNotPublic(request)) authenticate(request, response);
            filterChain.doFilter(request, response);
        } catch (RuntimeException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write(e.getMessage());
        }
    }

    private void authenticate(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String token = recoveryToken(request);

        if (token == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token ausente ou inválido.");
            return;
        }

        Long userId = jwtTokenService.getUserIdFromToken(token);
        User user = userRepository.findById(userId)
                                  .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));;

        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsImpl, null, userDetailsImpl.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private String recoveryToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null) {
            return authorizationHeader.replace("Bearer ", "");
        }
        return null;
    }

    private boolean checkIfEndpointIsNotPublic(HttpServletRequest request) {
        String requestURI = request.getRequestURI();

        for (String allowedEndpoint : SecurityConfiguration.ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED) {
            if (requestURI.matches(allowedEndpoint.replace("*", ".*"))) {
                return false;
            }
        }

        for (String swaggerEndpoint : SecurityConfiguration.ENDPOINT_SWAGGER) {
            if (requestURI.matches(swaggerEndpoint.replace("**", ".*"))) {
                return false;
            }
        }

        return true;
    }

}
