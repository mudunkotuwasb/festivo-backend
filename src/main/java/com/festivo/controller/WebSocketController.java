package com.festivo.controller;

import com.festivo.entity.Message;
import com.festivo.entity.User;
import com.festivo.service.MessageService;
import com.festivo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;

@Controller
public class WebSocketController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;
    
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message) {
        message.setCreatedAt(LocalDateTime.now());
        return messageService.sendMessage(message);
    }
    
    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    public Message addUser(@Payload Message message, SimpMessageHeaderAccessor headerAccessor) {
        headerAccessor.getSessionAttributes().put("username", message.getSender().getName());
        return message;
    }
    
    @MessageMapping("/chat.private")
    public void sendPrivateMessage(@Payload PrivateMessageRequest request) {
        try {
            User sender = userService.findUserById(request.getSenderId()).orElse(null);
            User receiver = userService.findUserById(request.getReceiverId()).orElse(null);
            
            if (sender != null && receiver != null) {
                Message message = new Message();
                message.setContent(request.getContent());
                message.setSender(sender);
                message.setReceiver(receiver);
                message.setMessageType(Message.MessageType.TEXT);
                
                messageService.sendMessage(message);
            }
        } catch (Exception e) {
            // Handle error
        }
    }
    
    public static class PrivateMessageRequest {
        private Long senderId;
        private Long receiverId;
        private String content;
        
        // Getters and Setters
        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }
        public Long getReceiverId() { return receiverId; }
        public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
    }
}
