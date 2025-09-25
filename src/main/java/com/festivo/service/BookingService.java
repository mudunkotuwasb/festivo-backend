package com.festivo.service;

import com.festivo.entity.Booking;
import com.festivo.entity.Customer;
import com.festivo.entity.Payment;
import com.festivo.entity.TimeSlot;
import com.festivo.entity.Vendor;
import com.festivo.repository.BookingRepository;
import com.festivo.repository.PaymentRepository;
import com.festivo.repository.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class BookingService {
    
    @Autowired
    private BookingRepository bookingRepository;
    
    @Autowired
    private PaymentRepository paymentRepository;
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    public Booking createBooking(Booking booking) {
        // Mark the time slot as booked
        if (booking.getTimeSlot() != null) {
            TimeSlot timeSlot = booking.getTimeSlot();
            timeSlot.setStatus(TimeSlot.TimeSlotStatus.BOOKED);
            timeSlotRepository.save(timeSlot);
        }
        
        return bookingRepository.save(booking);
    }
    
    public Optional<Booking> findBookingById(Long id) {
        return bookingRepository.findById(id);
    }
    
    public List<Booking> getBookingsByCustomer(Customer customer) {
        return bookingRepository.findByCustomer(customer);
    }
    
    public List<Booking> getBookingsByVendor(Vendor vendor) {
        return bookingRepository.findByVendor(vendor);
    }
    
    public List<Booking> getBookingsByServiceDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return bookingRepository.findByServiceDateBetween(startDate, endDate);
    }
    
    public List<Booking> getBookingsByStatus(Booking.BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }
    
    public List<Booking> getBookingsByPaymentStatus(Booking.PaymentStatus paymentStatus) {
        return bookingRepository.findByPaymentStatus(paymentStatus);
    }
    
    public Booking updateBooking(Booking booking) {
        return bookingRepository.save(booking);
    }
    
    public Booking cancelBooking(Long bookingId, String cancellationReason) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            booking.setStatus(Booking.BookingStatus.CANCELLED);
            booking.setCancellationReason(cancellationReason);
            booking.setCancelledAt(LocalDateTime.now());
            
            // Free up the time slot
            if (booking.getTimeSlot() != null) {
                TimeSlot timeSlot = booking.getTimeSlot();
                timeSlot.setStatus(TimeSlot.TimeSlotStatus.AVAILABLE);
                timeSlotRepository.save(timeSlot);
            }
            
            return bookingRepository.save(booking);
        }
        return null;
    }
    
    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
    
    // Payment Management
    public Payment createPayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    public Optional<Payment> findPaymentById(Long id) {
        return paymentRepository.findById(id);
    }
    
    public Optional<Payment> findPaymentByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentId(paymentId);
    }
    
    public List<Payment> getPaymentsByStatus(Payment.PaymentStatus status) {
        return paymentRepository.findByStatus(status);
    }
    
    public Payment updatePayment(Payment payment) {
        return paymentRepository.save(payment);
    }
    
    public Booking processAdvancePayment(Long bookingId, BigDecimal advanceAmount, String paymentId) {
        Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
        if (bookingOpt.isPresent()) {
            Booking booking = bookingOpt.get();
            
            // Create payment record
            Payment payment = new Payment();
            payment.setPaymentId(paymentId);
            payment.setAmount(advanceAmount);
            payment.setPaymentMethod(Payment.PaymentMethod.PAYHERE);
            payment.setPaymentType(Payment.PaymentType.ADVANCE_PAYMENT);
            payment.setStatus(Payment.PaymentStatus.COMPLETED);
            payment.setBooking(booking);
            payment.setProcessedAt(LocalDateTime.now());
            
            paymentRepository.save(payment);
            
            // Update booking
            booking.setAdvancePayment(advanceAmount);
            booking.setRemainingAmount(booking.getTotalAmount().subtract(advanceAmount));
            booking.setPaymentStatus(Booking.PaymentStatus.PARTIAL);
            
            return bookingRepository.save(booking);
        }
        return null;
    }
}
