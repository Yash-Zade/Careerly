package com.teamarc.careerlybackend.controller;


import com.teamarc.careerlybackend.dto.PrivateMessageDTO;
import com.teamarc.careerlybackend.entity.Message;
import com.teamarc.careerlybackend.paylod.MessageRequest;
import com.teamarc.careerlybackend.paylod.PrivateMessageRequest;
import com.teamarc.careerlybackend.services.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
@CrossOrigin("*")
public class ChatController {

    private final ChatService chatService;


    @MessageMapping("/sendMessage/{roomId}")
    @SendTo("/topic/room/{roomId}")
    public Message sendMessage(
            @RequestBody MessageRequest request,
            @DestinationVariable Long roomId) {

        return chatService.sendMessage(request, roomId);
    }

    @MessageMapping("/privateMessage")
    @SendTo("/private")
    public PrivateMessageDTO sendPrivateMessage(@RequestBody PrivateMessageRequest request) {
        return chatService.sendPrivateMessage(request);
    }

}
