package com.teamarc.careerlybackend.controller;


import com.teamarc.careerlybackend.dto.ChatRoomDTO;
import com.teamarc.careerlybackend.dto.MessageDTO;
import com.teamarc.careerlybackend.entity.Message;
import com.teamarc.careerlybackend.services.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/chatroom")
public class ChatRoomController {


    private final ChatRoomService chatRoomService;

    public ChatRoomController(ChatRoomService chatRoomService) {
        this.chatRoomService = chatRoomService;
    }
    // Create Room

    @PostMapping("/create")
    public ResponseEntity<?> createRoom(@RequestBody ChatRoomDTO chatRoomDTO) {
        return chatRoomService.createRoom(chatRoomDTO);

    }

    @GetMapping("/get/{roomId}")
    public ResponseEntity<?> getRoom(@PathVariable Long roomId) {
        return chatRoomService.getRoom(roomId);
    }

    @GetMapping("{roomId}/messages")
    public List<Message> getMessage(@PathVariable Long roomId) {
        return chatRoomService.getMessage(roomId);
    }

    @PostMapping("/{roomId}/messages")
    public ResponseEntity<?> addMessage(@PathVariable Long roomId, @RequestBody MessageDTO messageDTO) {
        return ResponseEntity.ok(chatRoomService.addMessage(roomId, messageDTO));
    }

    @GetMapping("/{roomId}/messages/all")
    public List<MessageDTO> getAllMessages(@PathVariable Long roomId) {
        return chatRoomService.getAllMessages(roomId);
    }

}
