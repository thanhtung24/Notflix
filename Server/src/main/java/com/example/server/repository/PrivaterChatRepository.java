package com.example.server.repository;

import com.example.server.entities.ChatNachricht;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivaterChatRepository extends JpaRepository<ChatNachricht, Long> {

    List<ChatNachricht> getChatNachrichtsByPersonEmpfaengerId(Long id);

    List<ChatNachricht> getChatNachrichtsByPersonEmpfaengerIdAndPersonSenderId(Long id1, Long id2);
}
