package com.example.client.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class GlobaleStartseiteController {

    @FXML
    private Button button_login_admin;

    @FXML
    private Button button_registrierung_admin;

    @FXML
    private Button button_login_nutzer;

    @FXML
    private Button button_registrierung_nutzer;

    @Autowired
    private com.example.client.FXMLLoader FXMLLoader;

    public void loginPressed_admin() throws IOException {
        FXMLLoader.setRoot("SystemadministratorEinloggen");
    }

    public void registerPressed_admin() throws IOException {
        FXMLLoader.setRoot("AdminBerechtigung");
    }

    public void registerPressed_nutzer(){

        try {
            FXMLLoader.setRoot("RegistrierungNutzer");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loginPressed_nutzer(){

        try{
            FXMLLoader.setRoot("NutzerLogin");
        }
        catch(IOException e){
            e.printStackTrace();
        }

    }




}
