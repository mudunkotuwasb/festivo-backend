package com.festivo.repository;

import com.festivo.entity.Vendor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {
    
    Optional<Vendor> findByEmail(String email);
    
    Optional<Vendor> findByBusinessRegistrationNumber(String businessRegistrationNumber);
    
    @Query("SELECT v FROM Vendor v WHERE v.vendorType = :vendorType AND v.isActive = true")
    List<Vendor> findByVendorType(@Param("vendorType") Vendor.VendorType vendorType);
    
    @Query("SELECT v FROM Vendor v WHERE v.city = :city AND v.isActive = true")
    List<Vendor> findByCity(@Param("city") String city);
    
    @Query("SELECT v FROM Vendor v WHERE v.vendorType = :vendorType AND v.city = :city AND v.isActive = true")
    List<Vendor> findByVendorTypeAndCity(@Param("vendorType") Vendor.VendorType vendorType, @Param("city") String city);
    
    @Query("SELECT v FROM Vendor v WHERE v.isVerified = true AND v.isActive = true ORDER BY v.rating DESC")
    Page<Vendor> findVerifiedVendorsOrderByRating(Pageable pageable);
    
    @Query("SELECT v FROM Vendor v WHERE v.isActive = true ORDER BY v.rating DESC")
    Page<Vendor> findActiveVendorsOrderByRating(Pageable pageable);
    
    @Query("SELECT v FROM Vendor v WHERE v.isActive = true AND v.rating >= :minRating")
    List<Vendor> findByMinRating(@Param("minRating") Double minRating);
}
