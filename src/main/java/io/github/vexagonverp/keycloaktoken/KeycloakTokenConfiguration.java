package io.github.vexagonverp.keycloaktoken;

import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan
@ConditionalOnProperty(value = "io.github.vexagonverp.keycloaktoken.config.enabled", havingValue = "true", matchIfMissing = true)
public class KeycloakTokenConfiguration {

    private static String keycloakServerUrl = "http://localhost:8080/auth";

    private static String adminUsername = "admin";
    private static String adminPassword = "admin";
    private static String adminClient = "admin-cli";
    private static String adminGrant = "password";
    private static String adminRealm = "master";
    @Autowired
    private KeycloakRestTemplate keycloakRestTemplate;

    public void setKeycloakServerUrl(String keycloakServerUrl) {
        KeycloakTokenConfiguration.keycloakServerUrl = keycloakServerUrl;
    }

    public void setAdminUsername(String adminUsername) {
        KeycloakTokenConfiguration.adminUsername = adminUsername;
    }

    public void setAdminPassword(String adminPassword) {
        KeycloakTokenConfiguration.adminPassword = adminPassword;
    }

    public void setAdminClient(String adminClient) {
        KeycloakTokenConfiguration.adminClient = adminClient;
    }

    public void setAdminGrant(String adminGrant) {
        KeycloakTokenConfiguration.adminGrant = adminGrant;
    }

    public void setAdminRealm(String adminRealm) {
        KeycloakTokenConfiguration.adminRealm = adminRealm;
    }

    // Create your Bean definitions here
    @Bean
    public KeycloakToken keycloakToken() {
        return new KeycloakToken(keycloakRestTemplate, keycloakServerUrl,
                adminUsername, adminPassword,
                adminClient, adminGrant,
                adminRealm);
    }
}
