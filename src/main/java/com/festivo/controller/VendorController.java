package com.festivo.controller;

import com.festivo.entity.Service;
import com.festivo.entity.TimeSlot;
import com.festivo.entity.Vendor;
import com.festivo.service.VendorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/vendors")
@CrossOrigin(origins = "*")
@Tag(name = "Vendor Management", description = "APIs for managing vendors, services, and time slots")
@SecurityRequirement(name = "Bearer Authentication")
public class VendorController {
    
    @Autowired
    private VendorService vendorService;
    
    @GetMapping
    @Operation(
        summary = "Get all vendors",
        description = "Retrieve a paginated list of vendors with optional filtering by city, vendor type, and minimum rating"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved vendors",
            content = @Content(
                mediaType = "application/json",
                schema = @Schema(implementation = Map.class),
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "success": true,
                        "vendors": [
                            {
                                "id": 1,
                                "businessName": "Elegant Catering",
                                "vendorType": "CATERING",
                                "city": "Colombo",
                                "rating": 4.8,
                                "totalReviews": 124,
                                "isVerified": true
                            }
                        ],
                        "totalElements": 25,
                        "totalPages": 3
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request parameters",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error Response",
                    value = """
                    {
                        "success": false,
                        "message": "Invalid vendor type"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> getAllVendors(
            @Parameter(description = "Filter vendors by city", example = "Colombo")
            @RequestParam(required = false) String city,
            
            @Parameter(description = "Filter vendors by type", example = "CATERING")
            @RequestParam(required = false) String vendorType,
            
            @Parameter(description = "Filter vendors by minimum rating", example = "4.0")
            @RequestParam(required = false) Double minRating,
            
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size) {
        
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Vendor> vendorList;
            Page<Vendor> vendors;
            
            if (vendorType != null && city != null) {
                vendorList = vendorService.findVendorsByTypeAndCity(
                    Vendor.VendorType.valueOf(vendorType.toUpperCase()), city);
                vendors = Page.empty();
            } else if (vendorType != null) {
                vendorList = vendorService.findVendorsByType(
                    Vendor.VendorType.valueOf(vendorType.toUpperCase()));
                vendors = Page.empty();
            } else if (city != null) {
                vendorList = vendorService.findVendorsByCity(city);
                vendors = Page.empty();
            } else if (minRating != null) {
                vendorList = vendorService.findVendorsByMinRating(minRating);
                vendors = Page.empty();
            } else {
                vendors = vendorService.findActiveVendorsOrderByRating(pageable);
                vendorList = vendors.getContent();
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("vendors", vendorList);
            response.put("totalElements", vendors.isEmpty() ? vendorList.size() : vendors.getTotalElements());
            response.put("totalPages", vendors.isEmpty() ? 1 : vendors.getTotalPages());
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch vendors: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get vendor by ID",
        description = "Retrieve detailed information about a specific vendor"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Successfully retrieved vendor",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "success": true,
                        "vendor": {
                            "id": 1,
                            "businessName": "Elegant Catering",
                            "vendorType": "CATERING",
                            "city": "Colombo",
                            "rating": 4.8,
                            "totalReviews": 124,
                            "description": "Professional catering services",
                            "isVerified": true
                        }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Vendor not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error Response",
                    value = """
                    {
                        "success": false,
                        "message": "Vendor not found"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> getVendorById(
            @Parameter(description = "Vendor ID", example = "1", required = true)
            @PathVariable Long id) {
        try {
            Optional<Vendor> vendor = vendorService.findVendorById(id);
            if (vendor.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("vendor", vendor.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch vendor: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}/services")
    public ResponseEntity<?> getVendorServices(@PathVariable Long id) {
        try {
            Optional<Vendor> vendor = vendorService.findVendorById(id);
            if (vendor.isPresent()) {
                List<Service> services = vendorService.getActiveServicesByVendor(vendor.get());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("services", services);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch services: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}/time-slots")
    public ResponseEntity<?> getVendorTimeSlots(
            @PathVariable Long id,
            @RequestParam(required = false) String date) {
        try {
            Optional<Vendor> vendor = vendorService.findVendorById(id);
            if (vendor.isPresent()) {
                List<TimeSlot> timeSlots;
                if (date != null) {
                    LocalDateTime dateTime = LocalDateTime.parse(date);
                    timeSlots = vendorService.getAvailableTimeSlotsByVendorAndDate(vendor.get(), dateTime);
                } else {
                    timeSlots = vendorService.getAvailableTimeSlotsByVendor(vendor.get());
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("timeSlots", timeSlots);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch time slots: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/services")
    public ResponseEntity<?> createService(@PathVariable Long id, @Valid @RequestBody Service service) {
        try {
            Optional<Vendor> vendor = vendorService.findVendorById(id);
            if (vendor.isPresent()) {
                service.setVendor(vendor.get());
                Service savedService = vendorService.createService(service);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Service created successfully");
                response.put("service", savedService);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to create service: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/time-slots")
    public ResponseEntity<?> createTimeSlot(@PathVariable Long id, @Valid @RequestBody TimeSlot timeSlot) {
        try {
            Optional<Vendor> vendor = vendorService.findVendorById(id);
            if (vendor.isPresent()) {
                timeSlot.setVendor(vendor.get());
                TimeSlot savedTimeSlot = vendorService.createTimeSlot(timeSlot);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Time slot created successfully");
                response.put("timeSlot", savedTimeSlot);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to create time slot: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateVendor(@PathVariable Long id, @Valid @RequestBody Vendor vendor) {
        try {
            Optional<Vendor> existingVendor = vendorService.findVendorById(id);
            if (existingVendor.isPresent()) {
                vendor.setId(id);
                Vendor updatedVendor = vendorService.updateVendor(vendor);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Vendor updated successfully");
                response.put("vendor", updatedVendor);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to update vendor: " + e.getMessage()));
        }
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
}
