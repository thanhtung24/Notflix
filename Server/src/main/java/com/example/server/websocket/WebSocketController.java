package com.example.server.websocket;


import com.example.server.entities.ChatNachricht;
import com.example.server.entities.DiskussionsGruppeChatNachrichtEntity;
import org.hibernate.annotations.common.util.impl.LoggerFactory;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketController {

    private Logger logger = LoggerFactory.logger(WebSocketController.class);
    @Autowired
    private SimpMessageSendingOperations messagingTemplate;

    @MessageMapping("/gruppenChat/{gruppenID}")
    public DiskussionsGruppeChatNachrichtEntity send(@DestinationVariable String gruppenID, DiskussionsGruppeChatNachrichtEntity nachricht){
        messagingTemplate.convertAndSend("/topic/gruppenChat/"+ gruppenID, nachricht);
        logger.info("Send Methode im WebSocketController wurde aufgerufen");
        return nachricht;
    }

    @MessageMapping("/privaterChat")
    public ChatNachricht privateChatNachrichten(ChatNachricht chatNachricht){
        messagingTemplate.convertAndSend("/topic/privaterChat", chatNachricht);
        logger.info("Private Chatnachricht verschickt");
        return chatNachricht;
    }
}

