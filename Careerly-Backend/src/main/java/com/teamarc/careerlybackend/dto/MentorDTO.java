package com.teamarc.careerlybackend.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class MentorDTO extends UserDTO {

    private List<String> expertise;

    private List<SessionDTO> sessions;
}
