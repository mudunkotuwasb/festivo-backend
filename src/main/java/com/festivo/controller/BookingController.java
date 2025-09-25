package com.festivo.controller;

import com.festivo.entity.Booking;
import com.festivo.entity.Customer;
import com.festivo.entity.Payment;
import com.festivo.entity.Vendor;
import com.festivo.service.BookingService;
import com.festivo.service.UserService;
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
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
@CrossOrigin(origins = "*")
@Tag(name = "Booking Management", description = "APIs for managing service bookings and payments")
@SecurityRequirement(name = "Bearer Authentication")
public class BookingController {
    
    @Autowired
    private BookingService bookingService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @Operation(
        summary = "Create a new booking",
        description = "Create a new service booking between a customer and vendor"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Booking created successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "success": true,
                        "message": "Booking created successfully",
                        "data": {
                            "id": 1,
                            "bookingDate": "2024-01-15T10:30:00",
                            "serviceDate": "2024-02-15T18:00:00",
                            "totalAmount": 1200.00,
                            "advancePayment": 360.00,
                            "remainingAmount": 840.00,
                            "status": "PENDING",
                            "paymentStatus": "PENDING",
                            "customer": {
                                "id": 1,
                                "name": "John Doe"
                            },
                            "vendor": {
                                "id": 2,
                                "businessName": "Elegant Catering"
                            }
                        }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or user not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error Response",
                    value = """
                    {
                        "success": false,
                        "message": "Customer or vendor not found"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> createBooking(
            @Parameter(description = "Booking request object", required = true)
            @Valid @RequestBody BookingRequest request) {
        try {
            Optional<Customer> customer = userService.findCustomerById(request.getCustomerId());
            Optional<Vendor> vendor = userService.findVendorById(request.getVendorId());
            
            if (customer.isEmpty() || vendor.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Customer or vendor not found"));
            }
            
            Booking booking = new Booking();
            booking.setBookingDate(LocalDateTime.now());
            booking.setServiceDate(request.getServiceDate());
            booking.setTotalAmount(request.getTotalAmount());
            booking.setAdvancePayment(request.getAdvancePayment());
            booking.setRemainingAmount(request.getTotalAmount().subtract(request.getAdvancePayment()));
            booking.setSpecialRequests(request.getSpecialRequests());
            booking.setCustomer(customer.get());
            booking.setVendor(vendor.get());
            booking.setService(request.getService());
            booking.setEvent(request.getEvent());
            
            Booking savedBooking = bookingService.createBooking(booking);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Booking created successfully");
            response.put("booking", savedBooking);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to create booking: " + e.getMessage()));
        }
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<?> getBookingsByCustomer(@PathVariable Long customerId) {
        try {
            Optional<Customer> customer = userService.findCustomerById(customerId);
            if (customer.isPresent()) {
                List<Booking> bookings = bookingService.getBookingsByCustomer(customer.get());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("bookings", bookings);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch bookings: " + e.getMessage()));
        }
    }
    
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getBookingsByVendor(@PathVariable Long vendorId) {
        try {
            Optional<Vendor> vendor = userService.findVendorById(vendorId);
            if (vendor.isPresent()) {
                List<Booking> bookings = bookingService.getBookingsByVendor(vendor.get());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("bookings", bookings);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch bookings: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable Long id) {
        try {
            Optional<Booking> booking = bookingService.findBookingById(id);
            if (booking.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("booking", booking.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch booking: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateBookingStatus(@PathVariable Long id, @RequestBody StatusUpdateRequest request) {
        try {
            Optional<Booking> booking = bookingService.findBookingById(id);
            if (booking.isPresent()) {
                Booking existingBooking = booking.get();
                existingBooking.setStatus(Booking.BookingStatus.valueOf(request.getStatus()));
                Booking updatedBooking = bookingService.updateBooking(existingBooking);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Booking status updated successfully");
                response.put("booking", updatedBooking);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to update booking status: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/cancel")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, @RequestBody CancellationRequest request) {
        try {
            Booking cancelledBooking = bookingService.cancelBooking(id, request.getReason());
            if (cancelledBooking != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Booking cancelled successfully");
                response.put("booking", cancelledBooking);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to cancel booking: " + e.getMessage()));
        }
    }
    
    @PostMapping("/{id}/payment")
    public ResponseEntity<?> processAdvancePayment(@PathVariable Long id, @RequestBody PaymentRequest request) {
        try {
            Booking booking = bookingService.processAdvancePayment(id, request.getAmount(), request.getPaymentId());
            if (booking != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Advance payment processed successfully");
                response.put("booking", booking);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to process payment: " + e.getMessage()));
        }
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    // DTOs
    public static class BookingRequest {
        private Long customerId;
        private Long vendorId;
        private LocalDateTime serviceDate;
        private BigDecimal totalAmount;
        private BigDecimal advancePayment;
        private String specialRequests;
        private com.festivo.entity.Service service;
        private com.festivo.entity.Event event;
        
        // Getters and Setters
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public Long getVendorId() { return vendorId; }
        public void setVendorId(Long vendorId) { this.vendorId = vendorId; }
        public LocalDateTime getServiceDate() { return serviceDate; }
        public void setServiceDate(LocalDateTime serviceDate) { this.serviceDate = serviceDate; }
        public BigDecimal getTotalAmount() { return totalAmount; }
        public void setTotalAmount(BigDecimal totalAmount) { this.totalAmount = totalAmount; }
        public BigDecimal getAdvancePayment() { return advancePayment; }
        public void setAdvancePayment(BigDecimal advancePayment) { this.advancePayment = advancePayment; }
        public String getSpecialRequests() { return specialRequests; }
        public void setSpecialRequests(String specialRequests) { this.specialRequests = specialRequests; }
        public com.festivo.entity.Service getService() { return service; }
        public void setService(com.festivo.entity.Service service) { this.service = service; }
        public com.festivo.entity.Event getEvent() { return event; }
        public void setEvent(com.festivo.entity.Event event) { this.event = event; }
    }
    
    public static class StatusUpdateRequest {
        private String status;
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
    }
    
    public static class CancellationRequest {
        private String reason;
        
        public String getReason() { return reason; }
        public void setReason(String reason) { this.reason = reason; }
    }
    
    public static class PaymentRequest {
        private BigDecimal amount;
        private String paymentId;
        
        public BigDecimal getAmount() { return amount; }
        public void setAmount(BigDecimal amount) { this.amount = amount; }
        public String getPaymentId() { return paymentId; }
        public void setPaymentId(String paymentId) { this.paymentId = paymentId; }
    }
}
