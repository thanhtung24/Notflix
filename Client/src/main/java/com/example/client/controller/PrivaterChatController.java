package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.PrivaterChatEndpoint;
import com.example.client.model.AktuellerNutzer;
import com.example.client.model.ChatNachricht;
import com.example.client.model.ChosenNutzer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.io.IOException;
import java.net.URL;
import java.util.stream.Collectors;


@Configuration
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@EnableScheduling
@EnableAsync
public class PrivaterChatController implements Initializable{

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private PrivaterChatEndpoint privaterChatEndpoint;

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;


    @FXML
    private TextArea textarea_eingabe;

    @FXML
    private Button button_senden;

    @FXML
    private TextField text_DuVorname;

    @FXML
    private TextField text_DuNachname;

    @FXML
    private TextField text_PartnerVorname;

    @FXML
    private TextField text_PartnerNachname;

    @FXML
    private TextArea textArea_chat;

    private ChatNachricht vonMirGesendet;
    private StompSession session;
    private Long aktuellerNutzerID;
    private Long ausgewaehlterNutzerID;
    private List<ChatNachricht> gesendeteNachrichtenWebSocket = new ArrayList<>();
    private List<ChatNachricht> empfangeneNachrichtenWebSocket = new ArrayList<>();
    private List<ChatNachricht> alleBisHerGesendeteNachrichten = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.textArea_chat.setEditable(false);

        if(AktuellerNutzer.aktuellerNutzer !=null && ChosenNutzer.nutzer != null) {
            //DU:
            text_DuVorname.setText(AktuellerNutzer.aktuellerNutzer.getVorname());
            text_DuNachname.setText(AktuellerNutzer.aktuellerNutzer.getNachname());

            //Partner:
            text_PartnerVorname.setText(ChosenNutzer.nutzer.getVorname());
            text_PartnerNachname.setText(ChosenNutzer.nutzer.getNachname());
        }

        if(AktuellerNutzer.aktuellerNutzer != null && ChosenNutzer.nutzer != null){
            aktuellerNutzerID = AktuellerNutzer.aktuellerNutzer.getId();
            ausgewaehlterNutzerID = ChosenNutzer.nutzer.getId();
        }


        chatVerlauf(aktuellerNutzerID,ausgewaehlterNutzerID);

        List<Long> zeitStempelNachrichten = new ArrayList<>();

        gesendeteNachrichtenWebSocket.stream()
                .map(ChatNachricht::getZeitStempel)
                .collect(Collectors.toCollection(() -> zeitStempelNachrichten));

        empfangeneNachrichtenWebSocket.stream()
                .map(ChatNachricht::getZeitStempel)
                .collect(Collectors.toCollection(() -> zeitStempelNachrichten));


        Collections.sort(alleBisHerGesendeteNachrichten, Comparator.comparing(ChatNachricht::getZeitStempel));

        //System.out.println("Anzahl der Zeitstempel: " + zeitStempelNachrichten.size());

        //System.out.println("Anzahl der Nachrichten: " + alleBisHerGesendeteNachrichten.size());


        //Liste alleBisHerGesendeteNachrichten ist anhand der TimeStamps sortiert d.h die Reihenfolge der
        //ausgetauschten Nachrichten ist fertig
        //ToDo: Du oder Partner vor die Nachricht im ChatVerlauf setzen, anhand der Id rausbekommen


        for(ChatNachricht nachricht: alleBisHerGesendeteNachrichten){
            //Fall wenn der AktuelleNutzer ("DU") die Nachricht gesendet hast
            if(nachricht.getPersonSenderId().equals(aktuellerNutzerID)){
                textArea_chat.appendText("Du: " + nachricht.getInhalt() + "\n");
            }
            else if(nachricht.getPersonSenderId().equals(ausgewaehlterNutzerID)){
                textArea_chat.appendText(ChosenNutzer.nutzer.getVorname() + ": " + nachricht.getInhalt() + "\n");
            }

        }
        try {
            webSocketVerbindungHerstellen();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    public List<ChatNachricht> chatVerlauf(Long id, Long id2) {
        List<ChatNachricht> verlaufVonMir = null;
        List<ChatNachricht> verlaufVonPartner = null;
        try {
            verlaufVonMir = privaterChatEndpoint.meinChatVerlauf(id, id2).execute().body();
            verlaufVonPartner = privaterChatEndpoint.meinChatVerlauf(id2, id).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

        gesendeteNachrichtenWebSocket.addAll(verlaufVonPartner);

        empfangeneNachrichtenWebSocket.addAll(verlaufVonMir);

        alleBisHerGesendeteNachrichten.addAll(verlaufVonMir);

        alleBisHerGesendeteNachrichten.addAll(verlaufVonPartner);

        return verlaufVonMir;
    }
    public void nachrichtenSenden(ActionEvent event) {
        button_senden.setOnMouseClicked(actionEvent -> {


            //System.out.println(textarea_eingabe.getText());

            ChatNachricht chatNachricht = new ChatNachricht();
            chatNachricht.setPersonEmpfaengerId(ausgewaehlterNutzerID);
            chatNachricht.setPersonSenderId(aktuellerNutzerID);
            chatNachricht.setInhalt(textarea_eingabe.getText());

            chatNachricht.setZeitStempel(System.currentTimeMillis());

            this.session.send("/app/privaterChat", chatNachricht);


            try {
                privaterChatEndpoint.nachrichteSenden(chatNachricht).execute();
                System.out.println("Nachricht an den Server geschickt");
            } catch (IOException e) {
                e.printStackTrace();
            }


            gesendeteNachrichtenWebSocket.add(chatNachricht);

            textArea_chat.appendText("Du: " + gesendeteNachrichtenWebSocket.get(gesendeteNachrichtenWebSocket.size() - 1).getInhalt() + "\n");

            textarea_eingabe.clear();

            //zuletzt Gesendete Nachricht
            this.vonMirGesendet = chatNachricht;

        });
    }

    @Scheduled(fixedRate = 2000)
    public void empfangeneNachrichten(){

        ChatNachricht zuLetztEmpfangen = SessionHandler.chatNachricht;

        ChatNachricht zuLetztGesendet = this.vonMirGesendet;

        if(zuLetztEmpfangen != null){
            // zuLetztEmpfangen.equals(zuLetztGesendet) funktioniert
            if(zuLetztEmpfangen.equals(zuLetztGesendet)){
                System.out.println("Du hast keine neue Nachricht die letzt Nachricht kommt von dir");
            } else {
                if(zuLetztEmpfangen.getPersonEmpfaengerId().equals(aktuellerNutzerID) && zuLetztEmpfangen.getPersonSenderId().equals(ausgewaehlterNutzerID)){
                    System.out.println("Du hast eine ungelesene Nachricht");
                    empfangeneNachrichtenWebSocket.add(zuLetztEmpfangen);

                    textArea_chat.appendText(ChosenNutzer.nutzer.getVorname() + ": " + empfangeneNachrichtenWebSocket.get(empfangeneNachrichtenWebSocket.size() - 1).getInhalt() + "\n");

                    SessionHandler.chatNachricht = null;
                } else {
                    System.out.println("Die Nachricht war nicht an dich!");
                    System.out.println("Die Nachricht war an die Person mit der ID: " + zuLetztEmpfangen.getPersonEmpfaengerId());
                    System.out.println("Dein ID ist: " + aktuellerNutzerID);
                    System.out.println("Der Sender der Nachricht ist: " + zuLetztEmpfangen.getPersonSenderId());
                    System.out.println("Dein Chatpartner von dem die Nachricht kommt hat die ID: " + ausgewaehlterNutzerID);
                }

            }
        } else {
            System.out.println("Es wurden keine Nachrichten verschickt");
        }
        SessionHandler.chatNachricht = null;

    }

    public void zurueckButton(){
        try {
            postProcessor.postProcessBeforeDestruction(this, "stopTask");
            System.out.println("Scheudling wurde beendet!");
            fxmlLoader.setRoot("Nutzersuche");

            //Nutzer IDs zurueck setzen
            aktuellerNutzerID = null;
            ausgewaehlterNutzerID = null;

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public StompSession webSocketVerbindungHerstellen() throws InterruptedException, ExecutionException{
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        SessionHandler sessionHandler = new SessionHandler();
        ListenableFuture<StompSession> sessionAsync =
                stompClient.connect("ws://localhost:8080/chat", sessionHandler);
        StompSession session = sessionAsync.get();
        session.subscribe("/topic/privaterChat", sessionHandler);
        this.session = session;
        return session;
    }
}

class SessionHandler extends StompSessionHandlerAdapter {
    static ChatNachricht chatNachricht;
    @Override
    public Type getPayloadType(StompHeaders headers) {
        return ChatNachricht.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        //System.out.println("In handle Frame Methode");
        ChatNachricht empfangen = (ChatNachricht) payload;
        //System.out.println("Nachricht vom Kanal : " + empfangen.getInhalt());
        //zuletzt empfangeneNachricht
        chatNachricht = empfangen;
    }
}
