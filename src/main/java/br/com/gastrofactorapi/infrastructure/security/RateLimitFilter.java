package br.com.gastrofactorapi.infrastructure.security;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import br.com.gastrofactorapi.infrastructure.config.RateLimitConfig;
import io.github.bucket4j.Bucket;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private final RateLimitConfig rateLimitConfig;

    private final int numberOfattempts = 5;
    private final int minutes = 1;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws IOException, ServletException {

        String path = request.getServletPath();
        if (path.equals("/v1/auth/login")) {
            String ip = request.getRemoteAddr();

            Bucket bucket = rateLimitConfig.resolveBucket(ip + ":login", numberOfattempts, minutes);

            if (!bucket.tryConsume(1)) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());

                response.getWriter().write("Muitas tentativas. Tente novamente em 5 minuto.");

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
