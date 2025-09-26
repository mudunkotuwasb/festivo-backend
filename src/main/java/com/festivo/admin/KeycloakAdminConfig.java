package com.festivo.admin;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KeycloakAdminConfig {

	@Bean
	public Keycloak keycloakAdmin(
			@Value("${keycloak.url}") String url,
			@Value("${keycloak.realm}") String realm,
			@Value("${keycloak.admin-client-id}") String clientId,
			@Value("${keycloak.admin-client-secret}") String clientSecret
	) {
		return KeycloakBuilder.builder()
				.serverUrl(url)
				.realm(realm)
				.clientId(clientId)
				.clientSecret(clientSecret)
				.grantType("client_credentials")
				.build();
	}
}



