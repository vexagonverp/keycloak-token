package io.github.vexagonverp.keycloaktoken;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;

import java.net.URI;

public class KeycloakToken {
    private static String ACCESS_TOKEN;
    private static String REFRESH_TOKEN;
    private final KeycloakRestTemplate restTemplate;
    private final String keycloakServerUrl;

    private final String adminUsername;
    private final String adminPassword;
    private final String adminClient;
    private final String adminGrant;
    private final String adminRealm;

    @Autowired
    public KeycloakToken(KeycloakRestTemplate restTemplate,
                         String keycloakServerUrl,
                         String adminUsername,
                         String adminPassword,
                         String adminClient,
                         String adminGrant,
                         String adminRealm) {
        this.restTemplate = restTemplate;
        this.keycloakServerUrl = keycloakServerUrl;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
        this.adminClient = adminClient;
        this.adminGrant = adminGrant;
        this.adminRealm = adminRealm;
    }

    public String getAccessToken() {
        if (testToken() == HttpStatus.UNAUTHORIZED) {
            refreshToken();
        }
        return ACCESS_TOKEN;
    }

    private HttpStatus testToken() {
        HttpStatus status;
        String token = "Bearer " + ACCESS_TOKEN;
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<AdminToken> responseAdminToken = restTemplate.exchange(URI.create(keycloakServerUrl + "/admin/realms/" + adminRealm),
                    HttpMethod.POST, entity, AdminToken.class);
            status = responseAdminToken.getStatusCode();
        } catch (HttpClientErrorException exception) {
            status = HttpStatus.UNAUTHORIZED;
        }
        return status;
    }

    private void generateNewToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> dataForm = new LinkedMultiValueMap<>();
        dataForm.add("client_id", adminClient);
        dataForm.add("username", adminUsername);
        dataForm.add("password", adminPassword);
        dataForm.add("grant_type", adminGrant);
        HttpEntity<MultiValueMap> entity = new HttpEntity<>(dataForm, headers);
        ResponseEntity<AdminToken> responseAdminToken = restTemplate.exchange(URI.create(keycloakServerUrl + "/realms/" + adminRealm + "/protocol/openid-connect/token"),
                HttpMethod.POST, entity, AdminToken.class);
        ACCESS_TOKEN = responseAdminToken.getBody().getAccessToken();
        REFRESH_TOKEN = responseAdminToken.getBody().getRefreshToken();

    }

    private void refreshToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE);
        MultiValueMap<String, String> dataForm = new LinkedMultiValueMap<>();
        dataForm.add("client_id", adminClient);
        dataForm.add("refresh_token", REFRESH_TOKEN);
        dataForm.add("grant_type", "refresh_token");
        HttpEntity<MultiValueMap> entity = new HttpEntity<>(dataForm, headers);
        try {
            ResponseEntity<AdminToken> responseAdminToken = restTemplate.exchange(URI.create(keycloakServerUrl + "/realms/" + adminRealm + "/protocol/openid-connect/token"),
                    HttpMethod.POST, entity, AdminToken.class);
            ACCESS_TOKEN = responseAdminToken.getBody().getAccessToken();
            REFRESH_TOKEN = responseAdminToken.getBody().getRefreshToken();
        } catch (HttpClientErrorException exception) {
            generateNewToken();
        }
    }

    private static class AdminToken {
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;

        public AdminToken() {
        }

        public AdminToken(String accessToken, String refresh_token) {
            this.accessToken = accessToken;
            this.refreshToken = refresh_token;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String access_token) {
            this.accessToken = access_token;
        }

        public String getRefreshToken() {
            return refreshToken;
        }

        public void setRefreshToken(String refresh_token) {
            this.refreshToken = refresh_token;
        }
    }
}
