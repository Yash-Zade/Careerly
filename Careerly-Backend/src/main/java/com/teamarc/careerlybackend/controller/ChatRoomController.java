package com.teamarc.careerlybackend.controller;


import com.teamarc.careerlybackend.dto.ChatRoomDTO;
import com.teamarc.careerlybackend.entity.Message;
import com.teamarc.careerlybackend.repository.RoomRepository;
import com.teamarc.careerlybackend.services.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path ="/api/chat/room")
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
    public ResponseEntity<List<Message>> getMessage(@PathVariable Long roomId) {
        return chatRoomService.getMessage(roomId);
    }
}
