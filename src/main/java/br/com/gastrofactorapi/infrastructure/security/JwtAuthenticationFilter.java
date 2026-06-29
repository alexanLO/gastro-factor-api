package br.com.gastrofactorapi.infrastructure.security;

import java.io.IOException;
import java.util.List;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gastrofactorapi.infrastructure.security.auth.repository.JwtBlacklistRepository;
import br.com.gastrofactorapi.infrastructure.security.auth.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE)
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final JwtBlacklistRepository jwtBlacklistRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String path = request.getServletPath();

        // libera endpoints auth
        if (path.startsWith("/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");

        // sem token
        if (authHeader == null
                || authHeader.isBlank()
                || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);

            return;
        }

        String token = authHeader.substring(7);
        try {
            String email = jwtUtils.extractEmail(token);

            if (jwtBlacklistRepository.existsByToken(token)) {
            writeUnauthorized(response, "Token invalidado");
            return;
            }

            if (email != null &&
                !email.isBlank() &&
                SecurityContextHolder.getContext()
                    .getAuthentication() == null) {

            var user = userRepository.findByEmail(email)
                .orElse(null);

            if (user == null || !jwtUtils.isTokenValid(token, user.getEmail())) {
                writeUnauthorized(response, "Token invalido");
                return;
            }

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                user,
                null,
                List.of(
                    new SimpleGrantedAuthority(
                        "ROLE_" + user.getRole())));

            authToken.setDetails(
                new WebAuthenticationDetailsSource()
                    .buildDetails(request));

            SecurityContextHolder.getContext()
                .setAuthentication(authToken);
            }
        } catch (Exception ex) {
            log.warn("Falha ao validar token JWT: {}", ex.getMessage());
            writeUnauthorized(response, "Token invalido");
            return;
        }

        filterChain.doFilter(request, response);
    }

        private void writeUnauthorized(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
        }

}
