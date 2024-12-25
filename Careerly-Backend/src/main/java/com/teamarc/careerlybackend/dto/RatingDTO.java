package com.teamarc.careerlybackend.dto;

import com.teamarc.careerlybackend.entity.enums.RatingValue;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingDTO {

    private Long ratingId;

    @NotNull(message = "Rating value cannot be null")
    private RatingValue ratingValue;

    private String comment;
}
