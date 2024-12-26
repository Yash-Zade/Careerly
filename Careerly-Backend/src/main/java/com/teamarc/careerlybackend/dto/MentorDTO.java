package com.teamarc.careerlybackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class MentorDTO {

    private Long mentorId;

    private List<String> expertise;

    private UserDTO user;

    private List<SessionDTO> sessions;

    private List<RatingDTO> ratings;
}
