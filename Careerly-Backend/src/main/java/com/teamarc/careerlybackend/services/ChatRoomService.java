package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.ChatRoomDTO;
import com.teamarc.careerlybackend.entity.ChatRoom;
import com.teamarc.careerlybackend.entity.Message;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.RoomRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RoomRepository roomRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> createRoom(ChatRoomDTO chatRoomDTO) {
        if(roomRepository.findById(chatRoomDTO.getRoomId()).isPresent()) {
            return ResponseEntity.badRequest().body("Room already exists");
        }

        ChatRoom chatRoom = modelMapper.map(chatRoomDTO, ChatRoom.class);
        chatRoom.setRoomId(chatRoomDTO.getRoomId());
        chatRoom.setRoomName(chatRoomDTO.getRoomName());
        chatRoom.setMessage(chatRoomDTO.getMessage());
        ChatRoom savedRoom = roomRepository.save(chatRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(savedRoom, ChatRoomDTO.class));
    }

    public ResponseEntity<?> getRoom(Long roomId) {
        if(roomRepository.findById(roomId).isEmpty()) {
            return ResponseEntity.badRequest().body("Room does not exist");
        }

        ChatRoom chatRoom =roomRepository.findById(roomId).orElseThrow(()-> new ResourceNotFoundException("Room not found"));
        return ResponseEntity.ok(modelMapper.map(chatRoom, ChatRoomDTO.class));
    }

    public ResponseEntity<List<Message>> getMessage(Long roomId) {
        ChatRoom chatRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return ResponseEntity.ok(chatRoom.getMessage());
    }
}
