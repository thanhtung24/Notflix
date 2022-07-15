package com.example.client.controller;

import com.example.client.FXMLLoader;
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

import java.io.IOException;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data

public class AdminBerechtigungController {

    @FXML
    private TextField text_code;

    @FXML
    private Button button_absenden_Code;

    @FXML
    private Label label_nachricht;

    @FXML
    private Button button_zurueck_Code;

    @Autowired
    private FXMLLoader fxmlLoader;


    public void absenden_Code(ActionEvent actionEvent){
        button_absenden_Code.setOnMouseClicked(event-> {

            String code= text_code.getText();

        if(code.equals("Admin_Q_2022")){
            try {
                fxmlLoader.setRoot("RegistrierungAdmin");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            this.label_nachricht.setText("Code ist nicht korrekt. Bitte versuchen Sie es erneut");
            this.label_nachricht.setTextFill(Color.RED);

        }


    });

    }

    public void zurueck(){
        try {
            fxmlLoader.setRoot("GlobaleStartseite");
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

}
