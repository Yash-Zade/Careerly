package com.teamarc.careerlybackend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Getter
@Setter
public class Admin extends User {

    @OneToMany(mappedBy = "admin")
    private List<Payment> payment;

    // Getters and Setters
}

