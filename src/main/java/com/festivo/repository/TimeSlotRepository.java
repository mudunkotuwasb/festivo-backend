package com.festivo.repository;

import com.festivo.entity.TimeSlot;
import com.festivo.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {
    
    List<TimeSlot> findByVendor(Vendor vendor);
    
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.vendor = :vendor AND ts.status = 'AVAILABLE'")
    List<TimeSlot> findAvailableByVendor(@Param("vendor") Vendor vendor);
    
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.vendor = :vendor AND ts.date = :date AND ts.status = 'AVAILABLE'")
    List<TimeSlot> findAvailableByVendorAndDate(@Param("vendor") Vendor vendor, @Param("date") LocalDateTime date);
    
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.vendor = :vendor AND ts.date BETWEEN :startDate AND :endDate")
    List<TimeSlot> findByVendorAndDateRange(@Param("vendor") Vendor vendor, 
                                           @Param("startDate") LocalDateTime startDate, 
                                           @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT ts FROM TimeSlot ts WHERE ts.vendor = :vendor AND ts.date >= :date AND ts.status = 'AVAILABLE'")
    List<TimeSlot> findAvailableByVendorFromDate(@Param("vendor") Vendor vendor, @Param("date") LocalDateTime date);
}
