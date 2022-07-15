package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.PersonAnlegenEndpoint;
import com.example.client.model.Person;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
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

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class PersonAnlegenController {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    PersonAnlegenEndpoint personAnlegenEndpoint;

    @FXML
    private Button button_personAnlegen;
    @FXML
    private Button button_zurueck;

    @FXML
    private TextField textField_nachname;
    @FXML
    private TextField textField_vorname;

    @FXML
    private Label label_personAnlegen;
    @FXML
    private Label label_nachname;
    @FXML
    private Label label_vorname;
    @FXML
    private Label label_benachrichtigung;

    public void switchToFilmliste () throws IOException {
        this.fxmlLoader.setRoot("FilmManuellAnlegenUndBearbeiten");
    }

    public void personAnlegen() {
        Person person = new Person(textField_nachname.getText(), textField_vorname.getText());
        System.out.println(person.toString());

        Response<Person> response = null;
        try {
            response = this.personAnlegenEndpoint.personAnlegen(person).execute();
            getPersonAnlegenBenachrichtigung(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getPersonAnlegenBenachrichtigung(Response<Person> response) throws IOException {
        if(response.isSuccessful()) {
            this.label_benachrichtigung.setTextFill(Color.GREEN);
            this.label_benachrichtigung.setText("Die person wurde erfolgreich hinzugefuegt");
            System.out.println("Film wurde erfolgreich angelegt");
        } else if (textField_nachname.getText().isEmpty() || textField_vorname.getText().isEmpty()) {
            this.label_benachrichtigung.setTextFill(Color.RED);
            this.label_benachrichtigung.setText("Nachname und Vorname der Person duerfen nicht leer sein");
        } else {
            this.label_benachrichtigung.setTextFill(Color.RED);
            this.label_benachrichtigung.setText("Die Person existiert bereits");
        }
    }
}
