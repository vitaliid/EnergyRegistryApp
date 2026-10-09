package org.example.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Keycloak Admin API client used to create and maintain user accounts (see KeycloakUserService).
 */
@Configuration
@EnableConfigurationProperties(KeycloakAdminConfig.KeycloakProperties.class)
public class KeycloakAdminConfig {

    @ConfigurationProperties(prefix = "app.keycloak")
    public record KeycloakProperties(
            String serverUrl,
            String realm,
            String adminClientId,
            String adminClientSecret,
            String initialPassword
    ) {
    }

    @Bean(destroyMethod = "close")
    public Keycloak keycloak(KeycloakProperties properties) {
        return KeycloakBuilder.builder()
                .serverUrl(properties.serverUrl())
                .realm(properties.realm())
                .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                .clientId(properties.adminClientId())
                .clientSecret(properties.adminClientSecret())
                .build();
    }

    @Bean
    public RealmResource registryRealm(Keycloak keycloak, KeycloakProperties properties) {
        return keycloak.realm(properties.realm());
    }
}
