package com.teamarc.careerly.services.impl;


import com.teamarc.careerly.dto.SignupDto;
import com.teamarc.careerly.dto.UserDto;
import com.teamarc.careerly.entities.User;
import com.teamarc.careerly.entities.enums.Roles;
import com.teamarc.careerly.exceptions.RuntimeConflictException;
import com.teamarc.careerly.repository.UserRepository;
import com.teamarc.careerly.security.JWTService;
import com.teamarc.careerly.services.AuthService;
import com.teamarc.careerly.services.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;

    @Override
    public String[] login(String email, String password) {
        Authentication authentication= authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
        User user= (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new String[]{accessToken, refreshToken};
    }

    @Override
    @Transactional
    public UserDto signup(SignupDto signupDto) {
        User user=userRepository.findByEmail(signupDto.getEmail())
                .orElse(null);
        if(user != null)
            throw new RuntimeConflictException("The user already exist with email id: "+signupDto.getEmail());

        User mappedUser=modelMapper.map(signupDto,User.class);
        mappedUser.setRoles(Set.of(Roles.EMPLOYEE));
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        User savedUser = userRepository.save(mappedUser);

        return modelMapper.map(savedUser, UserDto.class);
}

    @Override
    public String refreshToken(String refreshToken) {

        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);
        return jwtService.generateAccessToken(user);
    }
}
