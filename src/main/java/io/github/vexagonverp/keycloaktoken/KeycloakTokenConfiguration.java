package io.github.vexagonverp.keycloaktoken;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
@ComponentScan
@ConditionalOnProperty(value = "io.github.vexagonverp.keycloaktoken.config.enabled", havingValue = "true", matchIfMissing = true)
public class KeycloakTokenConfiguration {

    private String keycloakServerUrl="http://${KEYCLOAK_HOST:localhost}:${KEYCLOAK_PORT:8080}/auth";

    private String adminUsername="admin";
    private String adminPassword="admin";
    private String adminClient="admin-cli";
    private String adminGrant="password";
    private String adminRealm="master";

    private RestTemplate restTemplate;

    public void setKeycloakServerUrl(String keycloakServerUrl) {
        this.keycloakServerUrl = keycloakServerUrl;
    }

    public void setAdminUsername(String adminUsername) {
        this.adminUsername = adminUsername;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public void setAdminClient(String adminClient) {
        this.adminClient = adminClient;
    }

    public void setAdminGrant(String adminGrant) {
        this.adminGrant = adminGrant;
    }

    public void setAdminRealm(String adminRealm) {
        this.adminRealm = adminRealm;
    }


    // Create your Bean definitions here
    @Bean
    public KeycloakToken myBean() {
        return new KeycloakToken(restTemplate,keycloakServerUrl,
                adminUsername,adminPassword,
                adminClient,adminGrant,
                adminRealm);
    }
}
