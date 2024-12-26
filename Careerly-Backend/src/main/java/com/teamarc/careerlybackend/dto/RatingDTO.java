package com.teamarc.careerlybackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class RatingDTO {

    private Long ratingId;

    @NotNull(message = "Rating value cannot be null")
    private Double ratingValue;

    private String comment;
}
