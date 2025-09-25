package com.festivo.repository;

import com.festivo.entity.Customer;
import com.festivo.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {
    
    List<Event> findByCustomer(Customer customer);
    
    @Query("SELECT e FROM Event e WHERE e.customer = :customer ORDER BY e.eventDate DESC")
    List<Event> findByCustomerOrderByEventDateDesc(@Param("customer") Customer customer);
    
    @Query("SELECT e FROM Event e WHERE e.eventDate BETWEEN :startDate AND :endDate")
    List<Event> findByEventDateBetween(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    @Query("SELECT e FROM Event e WHERE e.eventType = :eventType")
    List<Event> findByEventType(@Param("eventType") Event.EventType eventType);
}
