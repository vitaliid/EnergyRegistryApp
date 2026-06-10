package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.service.CacheTestService;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {

    private final CacheTestService cacheTestService;
    private final EntityManagerFactory emf;

    @Operation(summary = "Health check")
    @GetMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @Operation(summary = "Test cache")
    @GetMapping("/cache/test")
    public String testCache() {
        cacheTestService.loadFirst(1);
        cacheTestService.loadSecond(1);

        SessionFactory sf = emf.unwrap(SessionFactory.class);
        Statistics stats = sf.getStatistics();
        log.info("Second level cache hits: {}", stats.getSecondLevelCacheHitCount());
        log.info("Second level cache misses: {}", stats.getSecondLevelCacheMissCount());
        log.info("Second level cache puts: {}", stats.getSecondLevelCachePutCount());
        return """
                Hits=%d
                Misses=%d
                Puts=%d
                """
                .formatted(
                        stats.getSecondLevelCacheHitCount(),
                        stats.getSecondLevelCacheMissCount(),
                        stats.getSecondLevelCachePutCount());
    }
}
