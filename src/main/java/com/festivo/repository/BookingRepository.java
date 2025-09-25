package com.festivo.repository;

import com.festivo.entity.Booking;
import com.festivo.entity.Customer;
import com.festivo.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    
    List<Booking> findByCustomer(Customer customer);
    
    List<Booking> findByVendor(Vendor vendor);
    
    @Query("SELECT b FROM Booking b WHERE b.customer = :customer ORDER BY b.serviceDate DESC")
    List<Booking> findByCustomerOrderByServiceDateDesc(@Param("customer") Customer customer);
    
    @Query("SELECT b FROM Booking b WHERE b.vendor = :vendor ORDER BY b.serviceDate DESC")
    List<Booking> findByVendorOrderByServiceDateDesc(@Param("vendor") Vendor vendor);
    
    @Query("SELECT b FROM Booking b WHERE b.serviceDate BETWEEN :startDate AND :endDate")
    List<Booking> findByServiceDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT b FROM Booking b WHERE b.status = :status")
    List<Booking> findByStatus(@Param("status") Booking.BookingStatus status);
    
    @Query("SELECT b FROM Booking b WHERE b.paymentStatus = :paymentStatus")
    List<Booking> findByPaymentStatus(@Param("paymentStatus") Booking.PaymentStatus paymentStatus);
}
