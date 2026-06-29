package br.com.gastrofactorapi.infrastructure.config;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Configuration;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;

@Configuration
public class RateLimitConfig {

    private final Map<String, Bucket> buckets = new ConcurrentHashMap<>();

    public Bucket resolveBucket(String key, int capacity, int minutes) {

        return buckets.computeIfAbsent(key, k -> createBucket(capacity, minutes));
    }

    private Bucket createBucket(int capacity, int minutes) {

        Bandwidth limit = Bandwidth.builder()
                .capacity(capacity)
                .refillIntervally(minutes, Duration.ofMinutes(minutes))
                .build();

        return Bucket.builder()
                .addLimit(limit)
                .build();
    }
}
