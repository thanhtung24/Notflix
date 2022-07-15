package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmeinladungSendenEndpoint;
import com.example.client.endpoints.MailEndpoint;
import com.example.client.model.ChosenFilm;
import com.example.client.model.ChosenFilmeinladung;
import com.example.client.model.Filmeinladung;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import javafx.event.ActionEvent;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data


public class FilmEinladungsdetailController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    FilmeinladungSendenEndpoint filmeinladungSendenEndpoint;

    @Autowired
    MailEndpoint mailEndpoint;

    @Autowired
    FilmeinladungSendenController FilmeinladungSendenController;

    @FXML
    private Label label_einladung;

    @FXML
    private Label label_absender;

    @FXML
    private Label label_datum;

    @FXML
    private Label label_uhrzeit;

    @FXML
    private Label label_kommentar;

    @FXML
    private Label label_filmname;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_akzeptieren;

    @FXML
    private Button button_ablehnen;

    @FXML
    private TextField textField_filmname;

    @FXML
    private TextField textField_absender;

    @FXML
    private TextField textField_datum;

    @FXML
    private TextField textField_uhrzeit;

    @FXML
    private TextArea textArea_kommentar;

    @FXML
    private Label label_akzeptiert;

    @FXML
    private Label label_abgelehnt;

    public void switchToFilmeinladung ()throws IOException {
        fxmlLoader.setRoot("Filmeinladung");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zeigeFilmname(ChosenFilmeinladung.filmeinladung);
        zeigeAbsender(ChosenFilmeinladung.filmeinladung);
        zeigeKommentar(ChosenFilmeinladung.filmeinladung);
        zeigeDatum(ChosenFilmeinladung.filmeinladung);
        zeigeUhrzeit(ChosenFilmeinladung.filmeinladung);
        bereitsAkzeptiert(ChosenFilmeinladung.filmeinladung);
    }

    private void zeigeFilmname (Filmeinladung filmeinladung) {
        this.textField_filmname.setText(filmeinladung.getFilmname());
        this.textField_filmname.setEditable(false);
    }

    private void zeigeAbsender (Filmeinladung filmeinladung) {
        this.textField_absender.setText(filmeinladung.getEinladungssenderName());
        this.textField_absender.setEditable(false);
    }

    private void zeigeDatum (Filmeinladung filmeinladung){
        this.textField_datum.setText(filmeinladung.getDatum());
        this.textField_datum.setEditable(false);
    }

    private void zeigeUhrzeit (Filmeinladung filmeinladung){
        this.textField_uhrzeit.setText(filmeinladung.getUhrzeit());
        this.textField_uhrzeit.setEditable(false);
    }

    private void zeigeKommentar (Filmeinladung filmeinladung) {
        this.textArea_kommentar.setText(filmeinladung.getKommentar());
        this.textArea_kommentar.setEditable(false);
    }

    private void bereitsAkzeptiert (Filmeinladung filmeinladung) {
        if (filmeinladung.isAkzeptiert()){
            this.button_akzeptieren.setTextFill(Color.GREY);
            this.button_akzeptieren.setDisable(true);
        }
    }

    public void akzeptiereFilmeinladung(ActionEvent actionEvent) {
        this.button_akzeptieren.setOnMouseClicked(event -> {
            Response<Filmeinladung> response =null;
            try {
                response =this.filmeinladungSendenEndpoint.akzeptiereFilmeinladung(ChosenFilmeinladung.filmeinladung.getId()).execute();
                if (response.isSuccessful()){
                    this.button_akzeptieren.setTextFill(Color.GREY);
                    this.button_akzeptieren.setDisable(true);
                    sendeMailAkzeptiert ();
                    this.label_akzeptiert.setTextFill(Color.GREEN);
                    this.label_akzeptiert.setText("Filmeinladung erfolgreich angenommen");
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private void sendeMailAkzeptiert () {
        //um die Mail zu Senden, brauche ich die Filmeinladung!
        try {
            Response<Boolean> response = mailEndpoint.sendeFilmeinladungAkzeptiert(ChosenFilmeinladung.filmeinladung).execute();
            if (response.isSuccessful()){
                System.out.println("Mail der Akzeptanz erfolgreich verschickt?:"+response.body());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    public void filmeinladungAblehnen (ActionEvent actionEvent){
        this.button_ablehnen.setOnMouseClicked(event-> {

            try {
                Response <Long> response=this.filmeinladungSendenEndpoint.abgelehnteFilmeinladung(ChosenFilmeinladung.filmeinladung.getId()).execute();
                if (response.isSuccessful()){
                    sendeMailAbgelehnt ();
                    this.label_abgelehnt.setTextFill(Color.GREEN);
                    this.label_abgelehnt.setText("Filmeinladung erfolgreich abgelehnt");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


    private void sendeMailAbgelehnt () {
        //um die Mail zu Senden, brauche ich die Filmeinladung!
        try {
            Response<Boolean> response = mailEndpoint.sendeFilmeinladungAbgelehnt(ChosenFilmeinladung.filmeinladung).execute();
            if (response.isSuccessful()){
                System.out.println("Mail der Akzeptanz erfolgreich verschickt?:"+response.body());
            }
        } catch (IOException e){
            e.printStackTrace();
        }
    }

}
