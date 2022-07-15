package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmeinladungSendenEndpoint;
import com.example.client.endpoints.FreundschaftEndpoint;
import com.example.client.endpoints.MailEndpoint;
import com.example.client.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class FilmeinladungSendenController {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    private FreundschaftEndpoint freundschaftEndpoint;

    @Autowired
    private FilmeinladungSendenEndpoint filmeinladungSendenEndpoint;

    @Autowired
    private MailEndpoint mailEndpoint;

    @FXML
    private Label label_einladung;

    @FXML
    private Label label_empfaenger;

    @FXML
    private Label label_kommentar;

    @FXML
    private Label label_filmname;

    @FXML
    private Label label_datum;

    @FXML
    private Label label_uhrzeit;

    @FXML
    private Label label_erledigt;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_senden;

    @FXML
    private TextField textField_filmname;

    @FXML
    private TextField textField_uhrzeit;

    @FXML
    private TextArea textArea_kommentar;

    @FXML
    private ComboBox comboBox_empfaenger;

    @FXML
    private DatePicker datePicker_datum;

    @FXML
    public void initialize (){
        filmnameEingeben();
        initializeComboBoxEmpfaenger();
    }

    public void switchToNutzerFilmuebersicht() throws IOException {
        this.fxmlLoader.setRoot("NutzerFilmuebersicht");
    }

    //Filmname wird direkt eingegeben
    private void filmnameEingeben () {
        this.textField_filmname.setText(ChosenFilm.film.getName());
        this.textField_filmname.setEditable(false);
    }

    private void modifiziereCombobox (){
        this.comboBox_empfaenger.setConverter(new StringConverter<Nutzer>() {
            @Override
            public String toString(Nutzer nutzer ) {
                return (nutzer==null) ? null: nutzer.getVorname() + " " + nutzer.getNachname();
            }
            @Override
            public Nutzer fromString(String s) {
                return null;
            }
        });
    }

    //noch nicht fertig
    private void initializeComboBoxEmpfaenger() {
        try {
            List <Nutzer> freundesListe = freundschaftEndpoint.meineFreunde(AktuellerNutzer.aktuellerNutzer.getId()).execute().body(); //warum .body() ?
            if (freundesListe != null){
                ObservableList <Nutzer> freunde =FXCollections.observableArrayList(freundesListe);
                comboBox_empfaenger.setItems(freunde);
                modifiziereCombobox();
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public void filmeinladungSenden () {
        button_senden.setOnMouseClicked(event -> {

            String uhrzeit =this.textField_uhrzeit.getText();
            if (this.comboBox_empfaenger.getSelectionModel().isEmpty()){
                this.label_erledigt.setTextFill(Color.RED);
                this.label_erledigt.setText("Bitte gib deinen Freund an.");
            }

            else if (this.datePicker_datum.getValue()==null){
                this.label_erledigt.setTextFill(Color.RED);
                this.label_erledigt.setText("Bitte gib ein Datum an.");
            }

            else if (this.textField_uhrzeit.getText().isEmpty()){
                this.label_erledigt.setTextFill(Color.RED);
                this.label_erledigt.setText("Bitte gib eine Uhrzeit an.");
            }

            else if (uhrzeit.length()!=5) {
                this.label_erledigt.setTextFill(Color.RED);
                this.label_erledigt.setText("Bitte gib eine Uhrzeit im Format: HH:mm an.");
            }

            else if (uhrzeit.matches("[0-1][0-9][:][0-5][0-9]") || uhrzeit.matches("[2][0-3][:][0-5][0-9]")){
                //Freund einspeichern
                Object a = comboBox_empfaenger.getSelectionModel().getSelectedItem();
                ChosenNutzer.nutzer = (Nutzer) a;

                Long ausgewaehlterFreundId = ChosenNutzer.nutzer.getId();
                Long meineId = AktuellerNutzer.aktuellerNutzer.getId();

                String meinName = AktuellerNutzer.aktuellerNutzer.getVorname() + " " + AktuellerNutzer.aktuellerNutzer.getNachname();
                LocalDate date = this.datePicker_datum.getValue();
                Date dateTwo = java.sql.Date.valueOf(date);

                SimpleDateFormat date_format = new SimpleDateFormat("dd-MM-yyyy");

                String dateThree = date_format.format(dateTwo);



                Filmeinladung neueFilmeinladung = new Filmeinladung(this.textField_filmname.getText(), meineId, meinName, ausgewaehlterFreundId, this.textArea_kommentar.getText(), dateThree, this.textField_uhrzeit.getText());
                Response<Filmeinladung> response = null;

                try {
                    response = this.filmeinladungSendenEndpoint.sendeFilmeinladung(neueFilmeinladung).execute();

                    if (response.isSuccessful()) {
                        sendeEinladungsMail(neueFilmeinladung);
                        this.button_senden.setDisable(true);
                        this.button_senden.setTextFill(Color.GREY);

                        this.label_erledigt.setTextFill(Color.GREEN);
                        this.label_erledigt.setText("Deine Filmeinladung wurde erfolgreich verschickt.");

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            else {
                this.label_erledigt.setTextFill(Color.RED);
                this.label_erledigt.setText("Bitte gib eine vernünftige Uhrzeit an.");
            }
            });
    }



    private void sendeEinladungsMail (Filmeinladung neueFilmeinladung) throws IOException {

        try {
            Response <Boolean> response = mailEndpoint.sendeFilmeinladungsMail(neueFilmeinladung).execute();
            if (response.isSuccessful()){
                System.out.println ("Wurde Verschickt?: "+response.body());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}