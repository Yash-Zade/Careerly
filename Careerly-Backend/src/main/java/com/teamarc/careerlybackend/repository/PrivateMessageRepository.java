package com.teamarc.careerlybackend.repository;

import com.teamarc.careerlybackend.entity.PrivateMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PrivateMessageRepository extends JpaRepository<PrivateMessage, Long> {
    List<PrivateMessage> findBySenderIdAndReceiverId(Long senderId, Long receiverId);
}
