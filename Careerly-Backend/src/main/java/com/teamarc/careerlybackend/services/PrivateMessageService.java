package com.teamarc.careerlybackend.services;

import com.teamarc.careerlybackend.dto.PrivateMessageDTO;
import com.teamarc.careerlybackend.entity.PrivateMessage;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.repository.PrivateMessageRepository;
import com.teamarc.careerlybackend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateMessageService {

    private final UserRepository userRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final ModelMapper modelMapper;

    public ResponseEntity<?> sendMessage(PrivateMessageDTO privateMessageDTO) {

        if (privateMessageDTO.getSender() == null) {
            return ResponseEntity.badRequest().body("Sender cannot be null");
        }

        if (privateMessageDTO.getReceiver() == null) {
            return ResponseEntity.badRequest().body("Receiver cannot be null");
        }

        User sender = userRepository.findById(privateMessageDTO.getSender()).orElseThrow(() -> new RuntimeException("Sender not found"));
        User receiver = userRepository.findById(privateMessageDTO.getReceiver()).orElseThrow(() -> new RuntimeException("Receiver not found"));

        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setSender(sender);
        privateMessage.setReceiver(receiver);
        privateMessage.setMessageContent(privateMessageDTO.getMessageContent());
        privateMessage.setTimestamp(LocalDateTime.now());

        PrivateMessage savedMessage = privateMessageRepository.save(privateMessage);

        PrivateMessageDTO responseDTO = new PrivateMessageDTO();
        responseDTO.setMessageId(savedMessage.getPrivateMessageId());
        responseDTO.setSender(savedMessage.getSender().getId());
        responseDTO.setReceiver(savedMessage.getReceiver().getId());
        responseDTO.setMessageContent(savedMessage.getMessageContent());
        responseDTO.setTimestamp(savedMessage.getTimestamp());

        return ResponseEntity.ok(responseDTO);

    }

    public ResponseEntity<?> getPrivateMessage(Long senderId, Long receiverId) {
        List<PrivateMessage> privateMessages = privateMessageRepository.findBySenderIdAndReceiverId(senderId, receiverId);
        List<PrivateMessageDTO> privateMessageDTOs = privateMessages.stream().map(privateMessage -> {
            PrivateMessageDTO dto = new PrivateMessageDTO();
            dto.setMessageId(privateMessage.getPrivateMessageId());
            dto.setSender(privateMessage.getSender().getId());
            dto.setReceiver(privateMessage.getReceiver().getId());
            dto.setMessageContent(privateMessage.getMessageContent());
            dto.setTimestamp(privateMessage.getTimestamp());
            return dto;
        }).collect(Collectors.toList());
        return ResponseEntity.ok(privateMessageDTOs);
    }

}
