package com.teamarc.careerlybackend.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PrivateMessageDTO {


    private Long messageId;
    private Long sender;
    private Long receiver;
    private String messageContent;
    private LocalDateTime timestamp;

}
