package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.entity.ChatRoom;
import com.teamarc.careerlybackend.entity.Message;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.paylod.MessageRequest;
import com.teamarc.careerlybackend.repository.RoomRepository;
import com.teamarc.careerlybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ChatService {


    private final RoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    public Message sendMessage(MessageRequest request, Long roomId) {
        ChatRoom room = chatRoomRepository.findById(request.getRoomId()).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        User user = userRepository.findById(request.getSender()).orElseThrow(()-> new ResourceNotFoundException("User not found"));
        Message message = modelMapper.map(request, Message.class);
        message.setMessageContent(request.getMessageContent());
        message.setSender(user);
        message.setTimestamp(String.valueOf(LocalDateTime.now()));


        room.getMessage().add(message);
        chatRoomRepository.save(room);


        return message;
    }


}
