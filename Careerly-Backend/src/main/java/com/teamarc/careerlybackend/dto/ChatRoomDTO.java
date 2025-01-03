package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomDTO {

    private Long roomId;
    private String roomName;
    private List<Message> message;
}
