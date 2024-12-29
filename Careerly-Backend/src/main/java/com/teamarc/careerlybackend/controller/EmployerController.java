package com.teamarc.careerlybackend.controller;


import com.teamarc.careerlybackend.dto.EmployerDTO;
import com.teamarc.careerlybackend.services.EmployerService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(path="/employer")
@RequiredArgsConstructor
public class EmployerController {

    private final EmployerService employerService;





}
