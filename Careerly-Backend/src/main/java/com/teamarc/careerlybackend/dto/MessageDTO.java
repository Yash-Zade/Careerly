package com.teamarc.careerlybackend.dto;

import lombok.Data;

@Data
public class MessageDTO {

    private Long messageId;

    private UserDTO sender;

//    private UserDTO receiver;

    private String messageContent;

    private String timestamp;
}
