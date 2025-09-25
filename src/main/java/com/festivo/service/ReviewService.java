package com.festivo.service;

import com.festivo.entity.Review;
import com.festivo.entity.Vendor;
import com.festivo.repository.ReviewRepository;
import com.festivo.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ReviewService {
    
    @Autowired
    private ReviewRepository reviewRepository;
    
    @Autowired
    private VendorRepository vendorRepository;
    
    public Review createReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        
        // Update vendor rating
        updateVendorRating(review.getVendor());
        
        return savedReview;
    }
    
    public Optional<Review> findReviewById(Long id) {
        return reviewRepository.findById(id);
    }
    
    public List<Review> getReviewsByVendor(Vendor vendor) {
        return reviewRepository.findByVendor(vendor);
    }
    
    public List<Review> getReviewsByMinRating(Integer minRating) {
        return reviewRepository.findByMinRating(minRating);
    }
    
    public Review updateReview(Review review) {
        Review savedReview = reviewRepository.save(review);
        
        // Update vendor rating
        updateVendorRating(review.getVendor());
        
        return savedReview;
    }
    
    public void deleteReview(Long reviewId) {
        Optional<Review> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            Review review = reviewOpt.get();
            Vendor vendor = review.getVendor();
            
            reviewRepository.deleteById(reviewId);
            
            // Update vendor rating
            updateVendorRating(vendor);
        }
    }
    
    private void updateVendorRating(Vendor vendor) {
        Double averageRating = reviewRepository.findAverageRatingByVendor(vendor);
        Long totalReviews = reviewRepository.countByVendor(vendor);
        
        vendor.setRating(averageRating != null ? averageRating : 0.0);
        vendor.setTotalReviews(totalReviews.intValue());
        
        vendorRepository.save(vendor);
    }
}
