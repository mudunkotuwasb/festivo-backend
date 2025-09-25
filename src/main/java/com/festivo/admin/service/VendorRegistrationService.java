package com.festivo.admin.service;

import jakarta.ws.rs.core.Response;
import java.util.List;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class VendorRegistrationService {

	private final Keycloak kc;
	private final String realm;
	private final String vendorsGroupId;
	private final String frontendClientId;

	public VendorRegistrationService(
			Keycloak kc,
			@Value("${keycloak.realm}") String realm,
			@Value("${keycloak.vendors-group-id}") String vendorsGroupId,
			@Value("${keycloak.frontend-client-id}") String frontendClientId
	) {
		this.kc = kc;
		this.realm = realm;
		this.vendorsGroupId = vendorsGroupId;
		this.frontendClientId = frontendClientId;
	}

	public String registerVendor(String email, String firstName, String lastName) {
		UserRepresentation user = new UserRepresentation();
		user.setUsername(email);
		user.setEmail(email);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setEnabled(true);
		user.setEmailVerified(false);

		Response resp = kc.realm(realm).users().create(user);
		if (resp.getStatus() != 201) {
			throw new RuntimeException("Create user failed: " + resp.getStatusInfo());
		}

		String userId = kc.realm(realm).users()
				.search(email, true).stream()
				.filter(u -> email.equalsIgnoreCase(u.getEmail()))
				.findFirst()
				.orElseThrow(() -> new RuntimeException("User not found after creation"))
				.getId();

		GroupRepresentation group = kc.realm(realm).groups().group(vendorsGroupId).toRepresentation();
		kc.realm(realm).users().get(userId).joinGroup(group.getId());


		kc.realm(realm).users().get(userId)
				.executeActionsEmail(frontendClientId, null, null, List.of("VERIFY_EMAIL"));

		return userId;
	}
}


