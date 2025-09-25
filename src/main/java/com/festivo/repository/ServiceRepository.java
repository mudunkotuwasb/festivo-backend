package com.festivo.repository;

import com.festivo.entity.Service;
import com.festivo.entity.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Long> {
    
    List<Service> findByVendor(Vendor vendor);
    
    List<Service> findByVendorAndIsActive(Vendor vendor, Boolean isActive);
    
    @Query("SELECT s FROM Service s WHERE s.vendor = :vendor AND s.isActive = true")
    List<Service> findActiveServicesByVendor(@Param("vendor") Vendor vendor);
    
    @Query("SELECT s FROM Service s WHERE s.name LIKE %:name% AND s.isActive = true")
    List<Service> findByNameContaining(@Param("name") String name);
}
