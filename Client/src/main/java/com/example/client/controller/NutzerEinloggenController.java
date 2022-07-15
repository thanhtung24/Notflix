package com.example.client.controller;


import com.example.client.FXMLLoader;
import com.example.client.Hashing;
import com.example.client.endpoints.NutzerEndpoint;
import com.example.client.model.AktuellerNutzer;
import com.example.client.model.MailToSendTo;
import com.example.client.model.Nutzer;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data

public class NutzerEinloggenController {

    @FXML
    private TextField text_email;

    @FXML
    private TextField text_passwort;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_einloggen;

    @FXML
    private Label antwort;

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private NutzerEndpoint nutzerEndpoint;


    public void zurueck(){
        try {
            fxmlLoader.setRoot("GlobaleStartseite");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

     public void einloggen(ActionEvent actionEvent){
             button_einloggen.setOnMouseClicked(event -> {
                 String email, passwort;
                 email = text_email.getText();
                 passwort = text_passwort.getText();
                 Response<Nutzer> nutzerResponse = null;

                 String hashPassword = "";

                 try {
                     hashPassword = Hashing.toHexString(Hashing.getSHA(passwort));
                 } catch (NoSuchAlgorithmException e) {
                     e.printStackTrace();
                 }

                 try {
                     nutzerResponse = nutzerEndpoint.nutzerLogin(new Nutzer (email, hashPassword)).execute();

                 } catch (IOException e) {
                     e.printStackTrace();
                 }
                 System.out.println(nutzerResponse.code());

                 if(nutzerResponse.code() == 200){
                     antwort.setText("Login erfolgreich");
                     antwort.setTextFill(Color.GREEN);
                     AktuellerNutzer.aktuellerNutzer = nutzerResponse.body();
                     MailToSendTo.email = email;
                     try {
                         fxmlLoader.setRoot("NutzerZweiFaktorAuthentifizierung");
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                 } else {
                     antwort.setText("Login fehlgeschlagen");
                     antwort.setTextFill(Color.RED);
                 }

             });

         }
     }



