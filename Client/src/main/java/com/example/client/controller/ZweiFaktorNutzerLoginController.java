package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.MailEndpoint;
import com.example.client.model.MailToSendTo;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ResourceBundle;


@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data

public class ZweiFaktorNutzerLoginController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private MailEndpoint mailEndpoint;

    @FXML
    private TextField text_code;

    @FXML
    private Button button_erneuteMail;

    @FXML
    private Button button_absenden;

    @FXML
    private Button button_zurueck;

    @FXML
    private Text text_status;

    private String email;

    private String code;

    public void zurueck(){
        try {
            fxmlLoader.setRoot("NutzerLogin");
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void nutzerStartseite(){
        try {
            fxmlLoader.setRoot("Nutzerstartseite");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void absenden(ActionEvent event){
        button_absenden.setOnMouseClicked(actionEvent -> {

            String eingabe = text_code.getText();
            boolean check = false;

            try {
                check = mailEndpoint.mailCode(email, eingabe).execute().body();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if(check){
                nutzerStartseite();
                System.out.println("Erfolgreiche bestaetigung der 2 Faktor Auth.");
            } else {
                text_status.setText("Falscher Code");
            }


        });

    }

    public void erneuterVersuch(ActionEvent event){
        button_erneuteMail.setOnMouseClicked(actionEvent -> {
            sendMail();
        });
    }


    //damit bei sofortigem aufruf der Seite eine email geschickt wird
    //sollte kein mail im postfach liegen, dann im Spamfach gucken
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sendMail();
    }

    public void sendMail(){
        email = MailToSendTo.email;
        try {
            Response<String> response = mailEndpoint.mailSenden(email).execute();

            if(response.isSuccessful()){
                System.out.println("Email wurde erfolgreich geschickt");
                //this.code = response.body();
                text_status.setText("E-Mail wurde verschickt");
            } else {
                System.out.println("Es gab einen Fehler beim Email Senden auf dem Client");
            }

        } catch (SocketTimeoutException e){
            //System.out.println("Zeitueberschreitung beim Email verschicken, bitte nochmal verschicken");
            //text_status.setText("Bitte nochmal versuchen");
            System.out.println("SocketTimeOut");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }
}
