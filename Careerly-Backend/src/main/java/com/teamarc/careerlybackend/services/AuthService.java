package com.teamarc.careerlybackend.services;


import com.teamarc.careerly.dto.SignupDto;
import com.teamarc.careerly.dto.UserDto;

public interface AuthService {
    String[] login(String email, String Password);

    UserDto signup(SignupDto signupDto);

    String refreshToken(String refreshToken);
}
