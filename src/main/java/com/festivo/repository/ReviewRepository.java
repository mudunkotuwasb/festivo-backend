package com.festivo.repository;

import com.festivo.entity.Review;
import com.festivo.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    List<Review> findByVendor(Vendor vendor);
    
    @Query("SELECT r FROM Review r WHERE r.vendor = :vendor ORDER BY r.createdAt DESC")
    List<Review> findByVendorOrderByCreatedAtDesc(@Param("vendor") Vendor vendor);
    
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.vendor = :vendor")
    Double findAverageRatingByVendor(@Param("vendor") Vendor vendor);
    
    @Query("SELECT COUNT(r) FROM Review r WHERE r.vendor = :vendor")
    Long countByVendor(@Param("vendor") Vendor vendor);
    
    @Query("SELECT r FROM Review r WHERE r.rating >= :minRating")
    List<Review> findByMinRating(@Param("minRating") Integer minRating);
}
