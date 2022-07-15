package com.example.server.service;

import com.example.server.entities.ChatNachricht;
import com.example.server.repository.PrivaterChatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PrivaterChatService {
    @Autowired
    private PrivaterChatRepository privaterChatRepository;

    public List<ChatNachricht> chatVerlauf(Long id1, Long id2){
        return privaterChatRepository.getChatNachrichtsByPersonEmpfaengerIdAndPersonSenderId(id1,id2);
    }

    public ChatNachricht nachrichtSpeichern(ChatNachricht nachrichtZuSpeichern){
        privaterChatRepository.save(nachrichtZuSpeichern);
        return nachrichtZuSpeichern;
    }
}

