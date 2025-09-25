package com.festivo.service;

import com.festivo.entity.Message;
import com.festivo.entity.User;
import com.festivo.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MessageService {
    
    @Autowired
    private MessageRepository messageRepository;
    
    public Message sendMessage(Message message) {
        return messageRepository.save(message);
    }
    
    public List<Message> getConversationBetweenUsers(User user1, User user2) {
        return messageRepository.findConversationBetweenUsers(user1, user2);
    }
    
    public List<Message> getUnreadMessagesByUser(User user) {
        return messageRepository.findUnreadMessagesByReceiver(user);
    }
    
    public List<Message> getMessagesByUser(User user) {
        return messageRepository.findMessagesByUser(user);
    }
    
    public Message markAsRead(Long messageId) {
        Optional<Message> messageOpt = messageRepository.findById(messageId);
        if (messageOpt.isPresent()) {
            Message message = messageOpt.get();
            message.setIsRead(true);
            message.setReadAt(LocalDateTime.now());
            return messageRepository.save(message);
        }
        return null;
    }
    
    public void markAllAsRead(User user1, User user2) {
        List<Message> unreadMessages = messageRepository.findUnreadMessagesByReceiver(user1);
        for (Message message : unreadMessages) {
            if (message.getSender().equals(user2)) {
                message.setIsRead(true);
                message.setReadAt(LocalDateTime.now());
                messageRepository.save(message);
            }
        }
    }
    
    public void deleteMessage(Long messageId) {
        messageRepository.deleteById(messageId);
    }
}
