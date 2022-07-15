package com.example.server.restcontroller;

import com.example.server.entities.ChatNachricht;
import com.example.server.repository.PrivaterChatRepository;
import com.example.server.service.PrivaterChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/chat")
public class PrivaterChatRestController {

    @Autowired
    private PrivaterChatService privaterChatService;

    @PostMapping("/sendeNachricht")
    public ResponseEntity<ChatNachricht> nachrichtSenden(@RequestBody ChatNachricht nachricht){
        ChatNachricht gespeicherteNachricht = privaterChatService.nachrichtSpeichern(nachricht);
        return new ResponseEntity<>(gespeicherteNachricht, HttpStatus.OK);
    }

    @GetMapping("/chatVerlauf/{id1}/{id2}")
    public ResponseEntity<List<ChatNachricht>> meinChatVerlauf(@PathVariable(value = "id1") Long id1, @PathVariable(value = "id2") Long id2){
        return new ResponseEntity<>(privaterChatService.chatVerlauf(id1,id2), HttpStatus.OK);
    }
}
