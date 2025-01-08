package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.ChatRoom;
import lombok.Data;

@Data
public class MessageDTO {

    private Long messageId;

    private Long sender;

//    private UserDTO receiver;

    private String messageContent;

    private Long chatRoom;

    private String timestamp;
}
