package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.enums.CallStatus;
import com.teamarc.careerlybackend.entity.enums.CallType;
import lombok.Data;

import java.util.Date;

@Data
public class CallDTO {

    private Long callId;

    private UserDTO caller;

    private UserDTO receiver;

    private CallType callType;

    private Date callStartTime;

    private Date callEndTime;

    private String googleMeetLink;

    private CallStatus status;
}
