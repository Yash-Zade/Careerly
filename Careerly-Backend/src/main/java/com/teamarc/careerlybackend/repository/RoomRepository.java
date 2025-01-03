package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<ChatRoom, Long> {
}
