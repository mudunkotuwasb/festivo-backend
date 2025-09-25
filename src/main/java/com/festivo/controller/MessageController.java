package com.festivo.controller;

import com.festivo.entity.Message;
import com.festivo.entity.User;
import com.festivo.service.MessageService;
import com.festivo.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/messages")
@CrossOrigin(origins = "*")
@Tag(name = "Message Management", description = "APIs for real-time messaging between customers and vendors")
@SecurityRequirement(name = "Bearer Authentication")
public class MessageController {
    
    @Autowired
    private MessageService messageService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping
    @Operation(
        summary = "Send a message",
        description = "Send a message between users (customers and vendors)"
    )
    @ApiResponses(value = {
        @ApiResponse(
            responseCode = "200",
            description = "Message sent successfully",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Success Response",
                    value = """
                    {
                        "success": true,
                        "message": "Message sent successfully",
                        "data": {
                            "id": 1,
                            "content": "Hello, I'm interested in your catering services",
                            "sender": {
                                "id": 1,
                                "name": "John Doe"
                            },
                            "receiver": {
                                "id": 2,
                                "name": "Elegant Catering"
                            },
                            "isRead": false,
                            "createdAt": "2024-01-15T10:30:00"
                        }
                    }
                    """
                )
            )
        ),
        @ApiResponse(
            responseCode = "400",
            description = "Invalid request or user not found",
            content = @Content(
                mediaType = "application/json",
                examples = @ExampleObject(
                    name = "Error Response",
                    value = """
                    {
                        "success": false,
                        "message": "Sender or receiver not found"
                    }
                    """
                )
            )
        )
    })
    public ResponseEntity<?> sendMessage(
            @Parameter(description = "Message request object", required = true)
            @Valid @RequestBody MessageRequest request) {
        try {
            Optional<User> sender = userService.findUserById(request.getSenderId());
            Optional<User> receiver = userService.findUserById(request.getReceiverId());
            
            if (sender.isEmpty() || receiver.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Sender or receiver not found"));
            }
            
            Message message = new Message();
            message.setContent(request.getContent());
            message.setSender(sender.get());
            message.setReceiver(receiver.get());
            message.setMessageType(Message.MessageType.valueOf(request.getMessageType().toUpperCase()));
            message.setAttachmentUrl(request.getAttachmentUrl());
            
            Message savedMessage = messageService.sendMessage(message);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Message sent successfully");
            response.put("data", savedMessage);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to send message: " + e.getMessage()));
        }
    }
    
    @GetMapping("/conversation/{userId1}/{userId2}")
    public ResponseEntity<?> getConversation(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            Optional<User> user1 = userService.findUserById(userId1);
            Optional<User> user2 = userService.findUserById(userId2);
            
            if (user1.isEmpty() || user2.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Users not found"));
            }
            
            List<Message> messages = messageService.getConversationBetweenUsers(user1.get(), user2.get());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("messages", messages);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch conversation: " + e.getMessage()));
        }
    }
    
    @GetMapping("/unread/{userId}")
    public ResponseEntity<?> getUnreadMessages(@PathVariable Long userId) {
        try {
            Optional<User> user = userService.findUserById(userId);
            if (user.isPresent()) {
                List<Message> messages = messageService.getUnreadMessagesByUser(user.get());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("messages", messages);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch unread messages: " + e.getMessage()));
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getUserMessages(@PathVariable Long userId) {
        try {
            Optional<User> user = userService.findUserById(userId);
            if (user.isPresent()) {
                List<Message> messages = messageService.getMessagesByUser(user.get());
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("messages", messages);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to fetch messages: " + e.getMessage()));
        }
    }
    
    @PutMapping("/{messageId}/read")
    public ResponseEntity<?> markAsRead(@PathVariable Long messageId) {
        try {
            Message message = messageService.markAsRead(messageId);
            if (message != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Message marked as read");
                response.put("data", message);
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to mark message as read: " + e.getMessage()));
        }
    }
    
    @PutMapping("/conversation/{userId1}/{userId2}/read")
    public ResponseEntity<?> markConversationAsRead(@PathVariable Long userId1, @PathVariable Long userId2) {
        try {
            Optional<User> user1 = userService.findUserById(userId1);
            Optional<User> user2 = userService.findUserById(userId2);
            
            if (user1.isEmpty() || user2.isEmpty()) {
                return ResponseEntity.badRequest().body(createErrorResponse("Users not found"));
            }
            
            messageService.markAllAsRead(user1.get(), user2.get());
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Conversation marked as read");
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to mark conversation as read: " + e.getMessage()));
        }
    }
    
    @DeleteMapping("/{messageId}")
    public ResponseEntity<?> deleteMessage(@PathVariable Long messageId) {
        try {
            messageService.deleteMessage(messageId);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Message deleted successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(createErrorResponse("Failed to delete message: " + e.getMessage()));
        }
    }
    
    private Map<String, Object> createErrorResponse(String message) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("message", message);
        return response;
    }
    
    // DTO
    public static class MessageRequest {
        private Long senderId;
        private Long receiverId;
        private String content;
        private String messageType = "TEXT";
        private String attachmentUrl;
        
        // Getters and Setters
        public Long getSenderId() { return senderId; }
        public void setSenderId(Long senderId) { this.senderId = senderId; }
        public Long getReceiverId() { return receiverId; }
        public void setReceiverId(Long receiverId) { this.receiverId = receiverId; }
        public String getContent() { return content; }
        public void setContent(String content) { this.content = content; }
        public String getMessageType() { return messageType; }
        public void setMessageType(String messageType) { this.messageType = messageType; }
        public String getAttachmentUrl() { return attachmentUrl; }
        public void setAttachmentUrl(String attachmentUrl) { this.attachmentUrl = attachmentUrl; }
    }
}
