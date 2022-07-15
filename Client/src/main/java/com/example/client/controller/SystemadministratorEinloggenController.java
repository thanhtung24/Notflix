package com.example.client.controller;

import com.example.client.Hashing;
import com.example.client.model.Systemadministrator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Data;
import com.example.client.endpoints.SystemadministratorEndpoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class SystemadministratorEinloggenController {
    @Autowired
    private com.example.client.FXMLLoader FXMLLoader;
    @Autowired
    private SystemadministratorEndpoint systemadministratorEndpoint;

    @FXML
    private TextField text_vorname;

    @FXML
    private TextField text_nachname;

    @FXML
    private TextField text_email;

    @FXML
    private TextField text_passwort;

    @FXML
    private Text antwort;

    @FXML
    private Button button_login;

    @FXML
    private Button button_Startseite;





    public void adminLogin(ActionEvent event) {
        button_login.setOnMouseClicked(actionEvent -> {
            String email, passwort;
            email = text_email.getText();
            passwort = text_passwort.getText();
            Response<Systemadministrator> systemadministratorResponse = null;

            String hashPassword = "";

            try {
                hashPassword = Hashing.toHexString(Hashing.getSHA(passwort));
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            try {
                systemadministratorResponse = systemadministratorEndpoint.adminLogin(new Systemadministrator(email, hashPassword)).execute();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println(systemadministratorResponse.code());

            if(systemadministratorResponse.code() == 200){
                antwort.setText("Login erfolgreich");
                antwort.setFill(Color.GREEN);
                try {
                    FXMLLoader.setRoot("Filmliste");
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                antwort.setText("Login fehlgeschlagen");
                antwort.setFill(Color.RED);
            }

        });

    }

    public void switchToStartseite() throws IOException {
        FXMLLoader.setRoot("GlobaleStartseite");
    }
}
