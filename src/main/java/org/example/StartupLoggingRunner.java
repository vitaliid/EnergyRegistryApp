package org.example;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class StartupLoggingRunner implements ApplicationRunner {

    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) {

        String port = environment.getProperty("local.server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");

        String host = "http://localhost:" + port + contextPath;

        log.info("========================================");
        log.info("🚀 Application started successfully");
        log.info("🌍 Application URL: {}", host);
        log.info("📄 Swagger UI: {}/swagger-ui/index.html", host);
        log.info("📄 OpenAPI Docs: {}/v3/api-docs", host);
        log.info("========================================");
    }
}