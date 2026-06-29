package br.com.gastrofactorapi.infrastructure.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.security.rate-limit.login.attempts:5}")
    private int numberOfattempts;

    @Value("${app.security.rate-limit.login.minutes:1}")
    private int minutes;

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
                response.setHeader("Retry-After", String.valueOf(minutes * 60));
                response.setHeader("X-RateLimit-Limit", String.valueOf(numberOfattempts));
                response.setHeader("X-RateLimit-Window-Minutes", String.valueOf(minutes));

                response.getWriter().write("Muitas tentativas. Tente novamente em " + minutes + " minuto(s).");

                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
