package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.dto.PrivateMessageDTO;
import com.teamarc.careerlybackend.entity.ChatRoom;
import com.teamarc.careerlybackend.entity.Message;
import com.teamarc.careerlybackend.entity.PrivateMessage;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.exceptions.ResourceNotFoundException;
import com.teamarc.careerlybackend.paylod.MessageRequest;
import com.teamarc.careerlybackend.paylod.PrivateMessageRequest;
import com.teamarc.careerlybackend.repository.PrivateMessageRepository;
import com.teamarc.careerlybackend.repository.RoomRepository;
import com.teamarc.careerlybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {


    private final RoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PrivateMessageRepository privateMessageRepository;

    public Message sendMessage(MessageRequest request, Long roomId) {
        ChatRoom room = chatRoomRepository.findById(roomId).orElseThrow(() -> new ResourceNotFoundException("Room not found"));
        User user = userRepository.findById(request.getSender()).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Message message = modelMapper.map(request, Message.class);
        message.setMessageContent(request.getMessageContent());
        message.setSender(user);
        message.setTimestamp(String.valueOf(LocalDateTime.now()));


        room.getMessage().add(message);
        chatRoomRepository.save(room);


        return message;
    }

    public PrivateMessageDTO sendPrivateMessage(PrivateMessageRequest request) {
        User sender = userRepository.findById(request.getSender()).orElseThrow(() -> new ResourceNotFoundException("Sender not found"));
        User receiver = userRepository.findById(request.getReceiver()).orElseThrow(() -> new ResourceNotFoundException("Receiver not found"));

        PrivateMessage privateMessage = modelMapper.map(request, PrivateMessage.class);
        privateMessage.setSender(sender);
        privateMessage.setReceiver(receiver);
        privateMessage.setTimestamp(LocalDateTime.now());

        PrivateMessage savedMessage = privateMessageRepository.save(privateMessage);

        PrivateMessageDTO responseDTO = modelMapper.map(savedMessage, PrivateMessageDTO.class);

        return responseDTO;
    }

    public List<PrivateMessageDTO> getPrivateMessages(Long sender, long receiverId) {
        List<PrivateMessage> privateMessages = privateMessageRepository.findBySenderIdAndReceiverId(sender, receiverId);
        return privateMessages.stream().map(privateMessage -> modelMapper.map(privateMessage, PrivateMessageDTO.class)).collect(Collectors.toList());
    }

}
