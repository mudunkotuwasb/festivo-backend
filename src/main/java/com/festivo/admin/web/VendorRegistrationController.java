package com.festivo.admin.web;

import com.festivo.admin.dto.VendorSignupRequest;
import com.festivo.admin.service.VendorRegistrationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class VendorRegistrationController {

	private final VendorRegistrationService service;

	public VendorRegistrationController(VendorRegistrationService service) {
		this.service = service;
	}

	@PostMapping("/register-vendor")
	public ResponseEntity<?> registerVendor(@RequestBody VendorSignupRequest req) {
		String userId = service.registerVendor(req.email(), req.firstName(), req.lastName());
		return ResponseEntity.ok().body("{\"userId\":\"" + userId + "\"}");
	}
}



