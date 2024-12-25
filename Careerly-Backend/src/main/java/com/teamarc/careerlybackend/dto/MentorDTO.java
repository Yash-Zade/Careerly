package com.teamarc.careerlybackend.dto;

import lombok.Data;
import java.util.List;

@Data
public class MentorDTO extends UserDTO {

    private List<String> expertise;

    private List<SessionDTO> sessions;
}
