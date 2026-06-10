package org.example;

import de.bund.bva.isyfact.logging.IsyLogger;
import de.bund.bva.isyfact.logging.IsyLoggerFactory;
import de.bund.bva.isyfact.logging.LogKategorie;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;


@Component
@RequiredArgsConstructor
public class StartupLoggingRunner implements ApplicationRunner {

    private static final IsyLogger log =
            IsyLoggerFactory.getLogger(StartupLoggingRunner.class);

    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) {

        String port = environment.getProperty("local.server.port", "8080");
        String contextPath = environment.getProperty("server.servlet.context-path", "");

        String host = "http://localhost:" + port + contextPath;

        log.info(LogKategorie.JOURNAL, "EAPP00001", "========================================");
        log.info(LogKategorie.JOURNAL, "EAPP00001", "🚀 Application started successfully");
        log.info(LogKategorie.JOURNAL, "EAPP00001", "🌍 Application URL: {}", host);
        log.info(LogKategorie.JOURNAL, "EAPP00001", "📄 Swagger UI: {}/swagger-ui/index.html", host);
        log.info(LogKategorie.JOURNAL, "EAPP00001", "📄 OpenAPI Docs: {}/v3/api-docs", host);
        log.info(LogKategorie.JOURNAL, "EAPP00001", "========================================");
    }
}