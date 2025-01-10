package com.teamarc.careerlybackend.controller;

import com.teamarc.careerlybackend.dto.PrivateMessageDTO;
import com.teamarc.careerlybackend.services.PrivateMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/privateMessage")
public class PrivateMessageController {

    private final PrivateMessageService privateMessageService;

    @PostMapping("/send")
    public ResponseEntity<?> sendPrivateMessage(@RequestBody PrivateMessageDTO privateMessageDTO) {
        return ResponseEntity.ok(privateMessageService.sendMessage(privateMessageDTO));
    }

    @GetMapping("/get/{sender}/{receiver}")
    public ResponseEntity<?> getPrivateMessage(@PathVariable Long sender, @PathVariable Long receiver) {
        return ResponseEntity.ok(privateMessageService.getPrivateMessage(sender, receiver));
    }

}
