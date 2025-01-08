package com.teamarc.careerlybackend.paylod;

import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PrivateMessageRequest {
    public Long sender;
    public Long receiver;
    public String messageContent;
    public Long roomId;
    private String messageTime;
}
