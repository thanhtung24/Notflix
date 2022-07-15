package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.DiskussionsGruppeEndpoint;
import com.example.client.endpoints.FreundschaftEndpoint;
import com.example.client.endpoints.NutzerEndpoint;
import com.example.client.model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Configuration
@Component
//@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@EnableScheduling
@EnableAsync
public class DiskussionsGruppenChatController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private NutzerEndpoint nutzerEndpoint;

    @Autowired
    private DiskussionsGruppeEndpoint diskussionsGruppeEndpoint;

    @FXML
    private TextArea textarea_chat;

    @FXML
    private Label label_gruppenName;

    @FXML
    private TextField textfield_eingabe;

    @FXML
    private Button button_nachrichtSenden;

    @FXML
    private Button button_beitreten;

    @FXML
    private Button button_verlassen;

    @FXML
    private Text text_beitretenStatus;

    @FXML
    private Label label_adminName;
    @FXML
    private Button button_privacyEinstellungen;

    @FXML
    private Label label_aenderungsStatus;

    @FXML
    private Label label_sendenError;

    private Long aktuelleGruppeID;

    private Long aktuellerNutzerID;

    private String gruppenName;

    private StompSession session;

    private Long gruppenErstellerId;

    private Nutzer gruppenErstellerNutzer;

    private List<DiskussionsGruppeChatNachricht> chatVerlauf = new ArrayList<>();

    private boolean gruppeIstPrivat;

    private final String beigetreten = "Beigetreten";

    private List<Nutzer> nutzerDerGruppe = new ArrayList<>();

    private boolean mitglied;

    private boolean nutzerIstMitAdminBefreundet;

    private boolean flagBeitreten;

    @Autowired
    private FreundschaftEndpoint freundschaftEndpoint;

    private List<Nutzer> freundesListeVonAktuellemNutzer = new ArrayList<>();
    public void zurueck() throws IOException {
        fxmlLoader.setRoot("Diskussionsgruppen");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.aktuelleGruppeID = AusgewaehlteGruppe.diskussionsGruppe.getId();
        this.aktuellerNutzerID = AktuellerNutzer.aktuellerNutzer.getId();
        this.gruppenName = AusgewaehlteGruppe.gruppenName;
        this.gruppenErstellerId = AusgewaehlteGruppe.gruppenErstellerId;





        try {
            this.gruppenErstellerNutzer = nutzerEndpoint.aktuellerNutzerAnhandId(this.gruppenErstellerId).execute().body();
            this.nutzerDerGruppe = this.diskussionsGruppeEndpoint.alleGruppenMitglieder(this.aktuelleGruppeID).execute().body();
            this.freundesListeVonAktuellemNutzer = this.freundschaftEndpoint.meineFreunde(this.aktuellerNutzerID).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }





        this.label_adminName.setText("Admin: " + this.gruppenErstellerNutzer.getVorname() + " " + this.gruppenErstellerNutzer.getNachname());

        textarea_chat.setEditable(false);

        textarea_chat.setVisible(false);

        label_gruppenName.setText("Chat der Gruppe: " + gruppenName);

        String id = this.aktuelleGruppeID.toString();
        System.out.println("Ausgewaehlte Gruppen ID: " + id);

        if(!flagBeitreten){
            gruppenChatVerlauf(this.aktuelleGruppeID);
            if(chatVerlauf != null) {
                Collections.sort(chatVerlauf, Comparator.comparing(DiskussionsGruppeChatNachricht::getZeitStempel));

                for(DiskussionsGruppeChatNachricht nachricht : chatVerlauf){
                    String nutzerName = "";
                    try {
                        nutzerName = nutzerEndpoint.aktuellerNutzerAnhandId(nachricht.getPersonSenderId()).execute().body().getVorname();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    textarea_chat.appendText(nutzerName+ ": " + nachricht.getInhalt() + "\n");
                }
            }
        }
        this.flagBeitreten = false;



        try {
            webSocketVerbindungHerstellen();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        this.mitglied = mitgliedStatuscheck(aktuellerNutzerID, aktuelleGruppeID);
        if(mitglied){
            textarea_chat.setVisible(true);
            button_beitreten.setText(beigetreten);
            button_beitreten.setDisable(true);
        } else {
            textarea_chat.setVisible(false);
            textfield_eingabe.setVisible(false);
            button_nachrichtSenden.setVisible(false);
            button_verlassen.setDisable(true);
        }
        adminPrivacyButton();

    }

    public void adminPrivacyButton(){
        //ToDo: Schauen ob der aktuelle Nutzer Admin ist, wenn ja dann soll er einen Button sehen wo er die privacy einstellung der gruppe ändern kann
        boolean adminStatus = ueberpruefenObAktuellerNutzerGruppenAdminIst(this.aktuellerNutzerID);

        if(adminStatus){
            button_privacyEinstellungen.setVisible(true);
            //Admins können nicht die Gruppe verlassen
            button_verlassen.setVisible(false);
            //ToDo: aktuelle Privay einstellung finden und den text des buttons anpassen
            try {
                gruppeIstPrivat = Boolean.TRUE.equals(this.diskussionsGruppeEndpoint.gruppenPrivacyEinstellung(this.aktuelleGruppeID).execute().body());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(gruppeIstPrivat){
                button_privacyEinstellungen.setText("Gruppe Öffentlich setzen");
            } else {
                button_privacyEinstellungen.setText("Gruppe Privat setzen");
            }

        } else {
            button_privacyEinstellungen.setVisible(false);
        }
    }

    public void bearbeitenDerPrivacyEinstellung(ActionEvent actionEvent){
        this.button_privacyEinstellungen.setOnMouseClicked(mouseEvent -> {
            boolean isSuccesful;
            try {
                isSuccesful = this.diskussionsGruppeEndpoint.gruppeBearbeiten(this.aktuelleGruppeID, !this.gruppeIstPrivat).execute().isSuccessful();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(isSuccesful){
                label_aenderungsStatus.setText("Status geändert");
                label_aenderungsStatus.setTextFill(Color.GREEN);
                //erneutes aufrufen
                adminPrivacyButton();

            } else {
                label_aenderungsStatus.setText("Fehler beim ändern");
                label_aenderungsStatus.setTextFill(Color.RED);
            }

        });
    }


    public List<DiskussionsGruppeChatNachricht> gruppenChatVerlauf(Long gruppenID){
        try {
            chatVerlauf = diskussionsGruppeEndpoint.chatVerlaufVonGruppe(gruppenID).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return chatVerlauf;
    }

    public void nachrichtenSenden(ActionEvent event) {
        this.button_nachrichtSenden.setOnMouseClicked(actionEvent -> {
            String gruppenID = this.aktuelleGruppeID.toString();
            String eingabe = textfield_eingabe.getText();

            if(eingabe.trim().isEmpty()){
                label_sendenError.setText("Bitte keine leere Nachrichten verschicken");
                label_sendenError.setTextFill(Color.RED);
                textfield_eingabe.clear();
                return;
            }

            DiskussionsGruppeChatNachricht diskussionsGruppeChatNachricht = DiskussionsGruppeChatNachricht.builder()
                    .gruppenId(this.aktuelleGruppeID)
                    .personSenderId(aktuellerNutzerID)
                    .inhalt(eingabe)
                    .zeitStempel(System.currentTimeMillis())
                    .build();

            //String destination = "/topic/gruppenChat/" + gruppenID;

            String destination = "/app/gruppenChat/" + gruppenID;


            this.session.send(destination, diskussionsGruppeChatNachricht);

            System.out.println("Nachricht geschickt");

            try {
                diskussionsGruppeEndpoint.nachrichtSpeichern(diskussionsGruppeChatNachricht).execute();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Nachricht in der DB gespeichert");

            textarea_chat.appendText("Du: " + diskussionsGruppeChatNachricht.getInhalt() + "\n");

            textfield_eingabe.clear();

            label_sendenError.setText("");


        });
    }

    public boolean ueberpruefenObAktuellerNutzerGruppenAdminIst(Long aktuellerNutzerID){
        if(aktuellerNutzerID.equals(this.gruppenErstellerId)){
            return true;
        } else {
            return false;
        }
    }



    @Scheduled(fixedRate = 1000)
    public void nachrichtenEmpfangen(){

        DiskussionsGruppeChatNachricht zuletztempfangen = SessionHandlerGruppenChat.diskussionsGruppeChatNachricht;

        boolean gruppeIstPrivat = false;

        for(Nutzer meineFreunde : this.freundesListeVonAktuellemNutzer) {
            if (meineFreunde.getId().equals(gruppenErstellerId)) {
                //ToDo: Nutzer und Admin sind befreundet -> Gruppe anzeigen
                this.nutzerIstMitAdminBefreundet = true;
            }
        }
        try {
            if(aktuelleGruppeID != null){
                gruppeIstPrivat = this.diskussionsGruppeEndpoint.gruppenPrivacyEinstellung(this.aktuelleGruppeID).execute().body();
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //System.out.println("GruppenStatus: " + gruppeIstPrivat);

        //System.out.println("Nutzer is mit Admin befreundet: " + AusgewaehlteGruppe.nutzerIstMitAdminBefreundet);

        //System.out.println("Nutzer ist Admin: " + this.aktuellerNutzerID.equals(this.gruppenErstellerId));
        if(mitglied){
            if(!gruppeIstPrivat || (nutzerIstMitAdminBefreundet ||this.aktuellerNutzerID.equals(this.gruppenErstellerId))){
                if(this.textarea_chat != null && this.textfield_eingabe != null){
                    this.textarea_chat.setVisible(true);
                    this.textfield_eingabe.setVisible(true);
                    this.button_nachrichtSenden.setVisible(true);
                }
                if(zuletztempfangen != null){

                    //System.out.println("DEBUG: MITGLIED SOLLTE ALLES SEHEN KÖNNEN");

                    String inhaltDerNachricht = zuletztempfangen.getInhalt();
                    Long senderId = zuletztempfangen.getPersonSenderId();
                    if(zuletztempfangen.getPersonSenderId().equals(aktuellerNutzerID)){
                        System.out.println("Keine neue Gruppennachricht");
                    } else {

                        String nutzerName = "";

                        for(Nutzer nutzer: this.nutzerDerGruppe){
                            if(nutzer.getId().equals(senderId)){
                                //Nachricht wurde von dieser Person geschickt
                                nutzerName = nutzer.getVorname();
                            }
                        }

                        if(nutzerName.isEmpty()){
                            //Wenn der Nutzer nicht in der liste aus der initialien List dabei ist. z.b erst im verlauf dazugetreten
                            try {
                                Nutzer neuerNutzer = nutzerEndpoint.aktuellerNutzerAnhandId(senderId).execute().body();
                                nutzerName = neuerNutzer.getVorname();
                                //Neuer Nutzer wird in die liste mit aufgenommen
                                this.nutzerDerGruppe.add(neuerNutzer);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }


                        textarea_chat.appendText(nutzerName + ": " + inhaltDerNachricht + "\n");
                        System.out.println("Nachricht in den Gruppenchat geschrieben");
                    }
                    SessionHandlerGruppenChat.diskussionsGruppeChatNachricht = null;
                } else {
                    //System.out.println("ZuletzteEmpfangen ist null");
                }
            } else {
                if(this.textarea_chat != null && this.textfield_eingabe != null){
                    this.textarea_chat.setVisible(false);
                    this.textfield_eingabe.setVisible(false);
                    this.button_nachrichtSenden.setVisible(false);
                }

            }
        }



    }


    public void gruppeBeitreten(ActionEvent actionEvent){
        button_beitreten.setOnMouseClicked(mouseEvent -> {
            DiskussionsGruppenMitglied diskussionsGruppenMitglied = DiskussionsGruppenMitglied.builder()
                    .gruppenId(aktuelleGruppeID)
                    .nutzerId(aktuellerNutzerID)
                    .build();
            boolean response;

            try {
                response = diskussionsGruppeEndpoint.gruppenMitgliedHinzufuegen(diskussionsGruppenMitglied).execute().isSuccessful();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(response) {
                text_beitretenStatus.setText("Erfolgreich beigetreten");
                textarea_chat.setVisible(true);
                textfield_eingabe.setVisible(true);
                button_nachrichtSenden.setVisible(true);
                button_beitreten.setText(beigetreten);
                button_beitreten.setDisable(true);
                button_verlassen.setDisable(false);
            }
            else
                text_beitretenStatus.setText("Fehler beim beitreten");

            this.flagBeitreten = true;

            initialize(null,null);
        });
    }

    public boolean mitgliedStatuscheck(Long nutzerID, Long gruppeID){
        boolean erfolgreich;
        try {
            erfolgreich = diskussionsGruppeEndpoint.mitgliedStatus(nutzerID, gruppeID).execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        //wenn true dann mitglied sonst kein Mitglied
        return erfolgreich;

    }

    public void gruppeVerlassen(ActionEvent actionEvent){
        button_verlassen.setOnMouseClicked(mouseEvent -> {
            try {
                this.diskussionsGruppeEndpoint.gruppeVerlassen(this.aktuelleGruppeID, this.aktuellerNutzerID).execute();
                //Simmuliere Delay
                Thread.sleep(1000);
                fxmlLoader.setRoot("Diskussionsgruppen");
            } catch (IOException | InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }





    public StompSession webSocketVerbindungHerstellen() throws InterruptedException, ExecutionException {
        WebSocketClient client = new StandardWebSocketClient();

        WebSocketStompClient stompClient = new WebSocketStompClient(client);
        stompClient.setMessageConverter(new MappingJackson2MessageConverter());

        SessionHandlerGruppenChat sessionHandlerGruppenChat = new SessionHandlerGruppenChat();
        ListenableFuture<StompSession> sessionAsync =
                stompClient.connect("ws://localhost:8080/chat", sessionHandlerGruppenChat);
        StompSession session = sessionAsync.get();

        String gruppenID = this.aktuelleGruppeID.toString();

        String destination = "/topic/gruppenChat/"+ gruppenID;

        session.subscribe(destination, sessionHandlerGruppenChat);
        this.session = session;
        return session;
    }
}




class SessionHandlerGruppenChat extends StompSessionHandlerAdapter {

    static DiskussionsGruppeChatNachricht diskussionsGruppeChatNachricht;

    @Override
    public Type getPayloadType(StompHeaders headers) {
        return DiskussionsGruppeChatNachricht.class;
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        DiskussionsGruppeChatNachricht empfangen = (DiskussionsGruppeChatNachricht) payload;

        System.out.println("FOLGENDE NACHRICHT IST ANGEKOMMEN: " + empfangen.getInhalt());
        //zuletzt empfangeneNachricht
        diskussionsGruppeChatNachricht = empfangen;
    }
}
