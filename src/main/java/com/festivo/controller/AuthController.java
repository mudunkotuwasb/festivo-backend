package com.festivo.controller;

import com.festivo.entity.Customer;
import com.festivo.entity.Vendor;
import com.festivo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
@Tag(name = "Authentication", description = "APIs for user registration and authentication")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/register/customer")
    @Operation(
        summary = "Register a new customer",
        description = "Register a new customer account with basic information"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Customer registered successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "success": true,
                        "message": "Customer registered successfully",
                        "data": {
                            "id": 1,
                            "name": "John Doe",
                            "email": "john@example.com",
                            "phoneNumber": "+94 77 123 4567",
                            "userType": "CUSTOMER",
                            "isActive": true
                        }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or email already exists",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error Response",
                    value = """
                    {
                        "success": false,
                        "message": "Email already exists"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> registerCustomer(
            @Parameter(description = "Customer registration request", required = true)
            @Valid @RequestBody CustomerRegistrationRequest request) {
        try {
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email already exists"));
            }
            
            Customer customer = new Customer();
            customer.setName(request.getName());
            customer.setEmail(request.getEmail());
            customer.setPhoneNumber(request.getPhoneNumber());
            customer.setAddress(request.getAddress());
            customer.setCity(request.getCity());
            customer.setPostalCode(request.getPostalCode());
            
            Customer savedCustomer = userService.createCustomer(customer);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Customer registered successfully");
            response.put("user", savedCustomer);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/register/vendor")
    public ResponseEntity<?> registerVendor(@Valid @RequestBody VendorRegistrationRequest request) {
        try {
            if (userService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body(createErrorResponse("Email already exists"));
            }
            
            Vendor vendor = new Vendor();
            vendor.setName(request.getName());
            vendor.setEmail(request.getEmail());
            vendor.setPhoneNumber(request.getPhoneNumber());
            vendor.setBusinessName(request.getBusinessName());
            vendor.setBusinessRegistrationNumber(request.getBusinessRegistrationNumber());
            vendor.setAddress(request.getAddress());
            vendor.setCity(request.getCity());
            vendor.setPostalCode(request.getPostalCode());
            vendor.setDescription(request.getDescription());
            vendor.setWebsite(request.getWebsite());
            vendor.setVendorType(request.getVendorType());
            
            Vendor savedVendor = userService.createVendor(vendor);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Vendor registered successfully");
            response.put("user", savedVendor);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Registration failed: " + e.getMessage()));
        }
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Basic login (temporary)", description = "Temporary login that looks up user by email only and returns role and id. Replace with Keycloak.")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        return userService.findUserByEmail(request.getEmail())
                .map(user -> {
                    Map<String, Object> response = new HashMap<>();
                    response.put("success", true);
                    response.put("userId", user.getId());
                    response.put("role", user.getUserType().name().toLowerCase());
                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.badRequest().body(createErrorResponse("Invalid credentials")));
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    // DTOs
    public static class CustomerRegistrationRequest {
        private String name;
        private String email;
        private String phoneNumber;
        private String address;
        private String city;
        private String postalCode;
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    }
    
    public static class VendorRegistrationRequest {
        private String name;
        private String email;
        private String phoneNumber;
        private String businessName;
        private String businessRegistrationNumber;
        private String address;
        private String city;
        private String postalCode;
        private String description;
        private String website;
        private Vendor.VendorType vendorType;
        
        // Getters and Setters
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhoneNumber() { return phoneNumber; }
        public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
        public String getBusinessName() { return businessName; }
        public void setBusinessName(String businessName) { this.businessName = businessName; }
        public String getBusinessRegistrationNumber() { return businessRegistrationNumber; }
        public void setBusinessRegistrationNumber(String businessRegistrationNumber) { this.businessRegistrationNumber = businessRegistrationNumber; }
        public String getAddress() { return address; }
        public void setAddress(String address) { this.address = address; }
        public String getCity() { return city; }
        public void setCity(String city) { this.city = city; }
        public String getPostalCode() { return postalCode; }
        public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getWebsite() { return website; }
        public void setWebsite(String website) { this.website = website; }
        public Vendor.VendorType getVendorType() { return vendorType; }
        public void setVendorType(Vendor.VendorType vendorType) { this.vendorType = vendorType; }
    }

    public static class LoginRequest {
        private String email;
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
    }
}
