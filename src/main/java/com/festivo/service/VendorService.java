package com.festivo.service;

import com.festivo.entity.Service;
import com.festivo.entity.TimeSlot;
import com.festivo.entity.Vendor;
import com.festivo.repository.ServiceRepository;
import com.festivo.repository.TimeSlotRepository;
import com.festivo.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@Transactional
public class VendorService {
    
    @Autowired
    private VendorRepository vendorRepository;
    
    @Autowired
    private ServiceRepository serviceRepository;
    
    @Autowired
    private TimeSlotRepository timeSlotRepository;
    
    public Vendor createVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }
    
    public Optional<Vendor> findVendorById(Long id) {
        return vendorRepository.findById(id);
    }
    
    public List<Vendor> findVendorsByType(Vendor.VendorType vendorType) {
        return vendorRepository.findByVendorType(vendorType);
    }
    
    public List<Vendor> findVendorsByCity(String city) {
        return vendorRepository.findByCity(city);
    }
    
    public List<Vendor> findVendorsByTypeAndCity(Vendor.VendorType vendorType, String city) {
        return vendorRepository.findByVendorTypeAndCity(vendorType, city);
    }
    
    public Page<Vendor> findVerifiedVendorsOrderByRating(Pageable pageable) {
        return vendorRepository.findVerifiedVendorsOrderByRating(pageable);
    }
    
    public Page<Vendor> findActiveVendorsOrderByRating(Pageable pageable) {
        return vendorRepository.findActiveVendorsOrderByRating(pageable);
    }
    
    public List<Vendor> findVendorsByMinRating(Double minRating) {
        return vendorRepository.findByMinRating(minRating);
    }
    
    public Vendor updateVendor(Vendor vendor) {
        return vendorRepository.save(vendor);
    }
    
    public void deleteVendor(Long id) {
        vendorRepository.deleteById(id);
    }
    
    // Service Management
    public Service createService(Service service) {
        return serviceRepository.save(service);
    }
    
    public List<Service> getServicesByVendor(Vendor vendor) {
        return serviceRepository.findByVendor(vendor);
    }
    
    public List<Service> getActiveServicesByVendor(Vendor vendor) {
        return serviceRepository.findActiveServicesByVendor(vendor);
    }
    
    public Service updateService(Service service) {
        return serviceRepository.save(service);
    }
    
    public void deleteService(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }
    
    // Time Slot Management
    public TimeSlot createTimeSlot(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }
    
    public List<TimeSlot> getTimeSlotsByVendor(Vendor vendor) {
        return timeSlotRepository.findByVendor(vendor);
    }
    
    public List<TimeSlot> getAvailableTimeSlotsByVendor(Vendor vendor) {
        return timeSlotRepository.findAvailableByVendor(vendor);
    }
    
    public List<TimeSlot> getAvailableTimeSlotsByVendorAndDate(Vendor vendor, LocalDateTime date) {
        return timeSlotRepository.findAvailableByVendorAndDate(vendor, date);
    }
    
    public List<TimeSlot> getTimeSlotsByVendorAndDateRange(Vendor vendor, LocalDateTime startDate, LocalDateTime endDate) {
        return timeSlotRepository.findByVendorAndDateRange(vendor, startDate, endDate);
    }
    
    public TimeSlot updateTimeSlot(TimeSlot timeSlot) {
        return timeSlotRepository.save(timeSlot);
    }
    
    public void deleteTimeSlot(Long timeSlotId) {
        timeSlotRepository.deleteById(timeSlotId);
    }
}
