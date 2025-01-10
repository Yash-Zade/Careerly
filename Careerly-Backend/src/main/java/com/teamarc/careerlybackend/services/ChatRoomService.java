package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.ChatRoomDTO;
import com.teamarc.careerlybackend.dto.MessageDTO;
import com.teamarc.careerlybackend.entity.ChatRoom;
import com.teamarc.careerlybackend.entity.Message;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.repository.MessageRepository;
import com.teamarc.careerlybackend.repository.RoomRepository;
import com.teamarc.careerlybackend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public ResponseEntity<?> createRoom(ChatRoomDTO chatRoomDTO) {
        if (roomRepository.findById(chatRoomDTO.getRoomId()).isPresent()) {
            return ResponseEntity.badRequest().body("Room already exists");
        }

        ChatRoom chatRoom = modelMapper.map(chatRoomDTO, ChatRoom.class);
        chatRoom.setRoomId(chatRoomDTO.getRoomId());
        chatRoom.setRoomName(chatRoomDTO.getRoomName());
        chatRoom.setMessage(chatRoomDTO.getMessage().stream()
                .map(messageDTO -> modelMapper.map(messageDTO, Message.class))
                .collect(Collectors.toList()));
        ChatRoom savedRoom = roomRepository.save(chatRoom);
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(savedRoom, ChatRoomDTO.class));
    }

    public ResponseEntity<?> getRoom(Long roomId) {
        ChatRoom chatRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        List<Message> messages = messageRepository.findAllMessagesByRoomId(roomId);
        ChatRoomDTO chatRoomDTO = new ChatRoomDTO();
        chatRoomDTO.setRoomId(chatRoom.getRoomId());
        chatRoomDTO.setRoomName(chatRoom.getRoomName());
        chatRoomDTO.setMessage(chatRoom.getMessage().stream().map(message -> {
            MessageDTO messageDTO = new MessageDTO();
            messageDTO.setMessageId(message.getMessageId());
            messageDTO.setSender(message.getSender().getId());
            messageDTO.setMessageContent(message.getMessageContent());
            messageDTO.setChatRoom(message.getChatRoom().getRoomId());
            messageDTO.setTimestamp(message.getTimestamp());
            return messageDTO;
        }).collect(Collectors.toList()));
        return ResponseEntity.ok(chatRoomDTO);
    }

    public List<MessageDTO> getAllMessages(Long roomId) {
        ChatRoom chatRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return chatRoom.getMessage().stream()
                .map(message -> modelMapper.map(message, MessageDTO.class))
                .collect(Collectors.toList());
    }


    public List<Message> getMessage(Long roomId) {
        ChatRoom chatRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        return chatRoom.getMessage();
    }

    @Transactional
    public MessageDTO addMessage(Long roomId, MessageDTO messageDTO) {
        ChatRoom chatRoom = roomRepository.findById(roomId)
                .orElseThrow(() -> new ResourceNotFoundException("Room not found"));

        User sender = userRepository.findById(messageDTO.getSender())
                .orElseThrow(() -> new ResourceNotFoundException("Sender not found"));


        Message message = new Message();
        message.setMessageContent(messageDTO.getMessageContent());
        message.setSender(sender);
        message.setChatRoom(chatRoom);
        message.setTimestamp(messageDTO.getTimestamp());

        Message savedMessage = messageRepository.save(message);

        MessageDTO responseDTO = new MessageDTO();
        responseDTO.setMessageId(savedMessage.getMessageId());
        responseDTO.setSender(savedMessage.getSender().getId());
        responseDTO.setMessageContent(savedMessage.getMessageContent());
        responseDTO.setChatRoom(savedMessage.getChatRoom().getRoomId());
        responseDTO.setTimestamp(savedMessage.getTimestamp());

        return responseDTO;
    }


}
