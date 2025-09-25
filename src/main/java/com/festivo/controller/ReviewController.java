package com.festivo.controller;

import com.festivo.entity.Review;
import com.festivo.entity.Vendor;
import com.festivo.service.ReviewService;
import com.festivo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/reviews")
@CrossOrigin(origins = "*")
public class ReviewController {
    
    @Autowired
    private ReviewService reviewService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    public ResponseEntity<?> createReview(@Valid @RequestBody ReviewRequest request) {
        try {
            Optional<com.festivo.entity.Customer> customer = userService.findCustomerById(request.getCustomerId());
            Optional<Vendor> vendor = userService.findVendorById(request.getVendorId());
            
            if (customer.isEmpty() || vendor.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Customer or vendor not found"));
            }
            
            Review review = new Review();
            review.setRating(request.getRating());
            review.setComment(request.getComment());
            review.setCustomer(customer.get());
            review.setVendor(vendor.get());
            review.setBooking(request.getBooking());
            
            Review savedReview = reviewService.createReview(review);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Review created successfully");
            response.put("review", savedReview);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to create review: " + e.getMessage()));
        }
    }
    
    @GetMapping("/vendor/{vendorId}")
    public ResponseEntity<?> getReviewsByVendor(@PathVariable Long vendorId) {
        try {
            Optional<Vendor> vendor = userService.findVendorById(vendorId);
            if (vendor.isPresent()) {
                List<Review> reviews = reviewService.getReviewsByVendor(vendor.get());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("reviews", reviews);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch reviews: " + e.getMessage()));
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<?> getReviewById(@PathVariable Long id) {
        try {
            Optional<Review> review = reviewService.findReviewById(id);
            if (review.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("review", review.get());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch review: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @Valid @RequestBody ReviewRequest request) {
        try {
            Optional<Review> existingReview = reviewService.findReviewById(id);
            if (existingReview.isPresent()) {
                Review review = existingReview.get();
                review.setRating(request.getRating());
                review.setComment(request.getComment());
                
                Review updatedReview = reviewService.updateReview(review);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Review updated successfully");
                response.put("review", updatedReview);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to update review: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Review deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to delete review: " + e.getMessage()));
        }
    }
    
    @GetMapping("/rating/{minRating}")
    public ResponseEntity<?> getReviewsByMinRating(@PathVariable Integer minRating) {
        try {
            List<Review> reviews = reviewService.getReviewsByMinRating(minRating);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("reviews", reviews);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch reviews: " + e.getMessage()));
        }
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    // DTO
    public static class ReviewRequest {
        private Long customerId;
        private Long vendorId;
        private Integer rating;
        private String comment;
        private com.festivo.entity.Booking booking;
        
        // Getters and Setters
        public Long getCustomerId() { return customerId; }
        public void setCustomerId(Long customerId) { this.customerId = customerId; }
        public Long getVendorId() { return vendorId; }
        public void setVendorId(Long vendorId) { this.vendorId = vendorId; }
        public Integer getRating() { return rating; }
        public void setRating(Integer rating) { this.rating = rating; }
        public String getComment() { return comment; }
        public void setComment(String comment) { this.comment = comment; }
        public com.festivo.entity.Booking getBooking() { return booking; }
        public void setBooking(com.festivo.entity.Booking booking) { this.booking = booking; }
    }
}
