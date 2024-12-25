package com.teamarc.careerlybackend.services.impl;

import com.teamarc.careerlybackend.dto.SignupDTO;
import com.teamarc.careerlybackend.dto.UserDTO;
import com.teamarc.careerlybackend.entity.User;
import com.teamarc.careerlybackend.exceptions.RuntimeConflictException;
import com.teamarc.careerlybackend.repository.UserRepository;
import com.teamarc.careerlybackend.security.JWTService;
import com.teamarc.careerlybackend.services.AuthService;
import com.teamarc.careerlybackend.services.UserService;
import com.teamarc.careerlybackend.services.WalletService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Set;

import static com.teamarc.careerlybackend.entity.enums.Roles.APPLICANT;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final UserService userService;
    private final WalletService walletService;

    @Override
    public String[] login(String email, String password) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        User user = (User) authentication.getPrincipal();
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return new String[]{accessToken, refreshToken};
    }

    @Override
    @Transactional
    public UserDTO signup(SignupDTO signupDto) {
        User user = userRepository.findByEmail(signupDto.getEmail())
                .orElse(null);
        if (user != null)
            throw new RuntimeConflictException("The user already exist with email id: " + signupDto.getEmail());

        User mappedUser = modelMapper.map(signupDto, User.class);
        mappedUser.setPassword(passwordEncoder.encode(mappedUser.getPassword()));
        mappedUser.setRoles(Set.of(APPLICANT));
        User savedUser = userRepository.save(mappedUser);
        walletService.cerateNewWallet(savedUser);

        return modelMapper.map(savedUser, UserDTO.class);
    }

    @Override
    public String refreshToken(String refreshToken) {

        Long userId = jwtService.getUserIdFromToken(refreshToken);
        User user = userService.getUserById(userId);
        return jwtService.generateAccessToken(user);
    }
}
