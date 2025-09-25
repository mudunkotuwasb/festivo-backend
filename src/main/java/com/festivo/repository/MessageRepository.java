package com.festivo.repository;

import com.festivo.entity.Message;
import com.festivo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {
    
    @Query("SELECT m FROM Message m WHERE (m.sender = :user1 AND m.receiver = :user2) OR (m.sender = :user2 AND m.receiver = :user1) ORDER BY m.createdAt ASC")
    List<Message> findConversationBetweenUsers(@Param("user1") User user1, @Param("user2") User user2);
    
    @Query("SELECT m FROM Message m WHERE m.receiver = :user AND m.isRead = false")
    List<Message> findUnreadMessagesByReceiver(@Param("user") User user);
    
    @Query("SELECT m FROM Message m WHERE m.sender = :user OR m.receiver = :user ORDER BY m.createdAt DESC")
    List<Message> findMessagesByUser(@Param("user") User user);
}
