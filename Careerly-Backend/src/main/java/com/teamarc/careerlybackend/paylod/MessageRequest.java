package com.teamarc.careerlybackend.paylod;

import com.teamarc.careerlybackend.entity.User;
import lombok.*;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageRequest {

    public Long sender;
    public String messageContent;
    public Long roomId;
    private String messageTime;

}
