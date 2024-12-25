package com.teamarc.careerlybackend.services;


import com.teamarc.careerlybackend.dto.SignupDTO;
import com.teamarc.careerlybackend.dto.UserDTO;

public interface AuthService {
    String[] login(String email, String Password);

    UserDTO signup(SignupDTO signupDto);

    String refreshToken(String refreshToken);
}
