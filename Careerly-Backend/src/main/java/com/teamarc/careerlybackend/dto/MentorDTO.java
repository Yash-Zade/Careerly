package com.teamarc.careerlybackend.dto;

import lombok.Data;

import java.util.List;

@Data
public class MentorDTO {

    private Long mentorId;

    private List<String> expertise;

    private UserDTO user;

    private Long totalSessions;

    private Double averageRating;

    private String bio;

    private Integer experience;
}
