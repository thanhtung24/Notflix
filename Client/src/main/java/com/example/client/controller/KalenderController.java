package com.example.client.controller;


import com.example.client.FXMLLoader;
import com.example.client.endpoints.KalenderEndpoint;
import com.example.client.endpoints.NotizEndpoint;
import com.example.client.model.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class KalenderController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private KalenderEndpoint kalenderEndpoint;

    @Autowired
    private NotizEndpoint notizEndpoint;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_suche;

    @FXML
    private Button button_speichern;

    @FXML
    private DatePicker datePicker_datum;

    @FXML
    private TextArea textArea_eintraege;

    @FXML
    private TextArea textArea_notizen;

    @FXML
    private Label text_messageLabel;

    List<Filmbewertung> eintraegeBewertungen = new ArrayList<>();
    List<WatchListItem> eintraegeWatchlist = new ArrayList<>();
    List<BereitsGesehenItem> eintraegeBereitsGesehen = new ArrayList<>();
    List<Filmeinladung> eintraegeEinladungen = new ArrayList<>();

    public void zurueckZurNutzerstartseite() throws IOException {
        fxmlLoader.setRoot("Nutzerstartseite");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Date datum = new Date();
        int monat = datum.getMonth() + 1;
        int jahr = datum.getYear() + 1900;

        LocalDate localDate = LocalDate.ofInstant(datum.toInstant(), ZoneId.systemDefault());

        Kalendereintrag.bewertungen = getBewertungenByNutzerId(AktuellerNutzer.aktuellerNutzer.getId());
        Kalendereintrag.watchListItems = getWatchlistByNutzerId(AktuellerNutzer.aktuellerNutzer.getId());
        Kalendereintrag.bereitsGesehenItems = getBereitsGesehenByNutzerId(AktuellerNutzer.aktuellerNutzer.getId());
        Kalendereintrag.filmeinladungen = getFilmeinladungenByNutzerId(AktuellerNutzer.aktuellerNutzer.getId());
        Kalendereintrag.notizen = getNotizenByNutzerId(AktuellerNutzer.aktuellerNutzer.getId());

        datePicker_datum.setValue(localDate);
        datePicker_datum.setEditable(false);
        textArea_eintraege.setEditable(false);

        try {
            getKalendereintraege(datum.getDate(), monat, jahr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        notizAnzeigen(datum.getDate(), monat, jahr);
    }



    public void getKalendereintraege(int tag, int monat, int jahr) throws ParseException {
        for(int i = 0; i < Kalendereintrag.bewertungen.size(); i++){
            Filmbewertung aktuelleBewertung = Kalendereintrag.bewertungen.get(i);
            if(tag == aktuelleBewertung.datum.getDate() && monat == aktuelleBewertung.datum.getMonth() +1 && jahr == aktuelleBewertung.datum.getYear() + 1900){
                eintraegeBewertungen.add(aktuelleBewertung);
            }
        }

        for(int i = 0; i < Kalendereintrag.bereitsGesehenItems.size(); i++){
            BereitsGesehenItem aktuellesBereitsGesehenItem = Kalendereintrag.bereitsGesehenItems.get(i);
            if(tag == aktuellesBereitsGesehenItem.datum.getDate() && monat == aktuellesBereitsGesehenItem.datum.getMonth() +1 && jahr == aktuellesBereitsGesehenItem.datum.getYear() + 1900){
                eintraegeBereitsGesehen.add(aktuellesBereitsGesehenItem);
            }
        }

        for(int i = 0; i < Kalendereintrag.watchListItems.size(); i++){
            WatchListItem aktuellesWatchlistItem = Kalendereintrag.watchListItems.get(i);
            if(tag == aktuellesWatchlistItem.datum.getDate() && monat == aktuellesWatchlistItem.datum.getMonth() +1 && jahr == aktuellesWatchlistItem.datum.getYear() + 1900){
                eintraegeWatchlist.add(aktuellesWatchlistItem);
            }
        }

        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        Date datumEinladung;
        for(int i = 0; i < Kalendereintrag.filmeinladungen.size(); i++){
            Filmeinladung aktuelleFilmeinladung = Kalendereintrag.filmeinladungen.get(i);
            datumEinladung = formatter.parse(aktuelleFilmeinladung.getDatum());
            if(tag == datumEinladung.getDate() && monat == datumEinladung.getMonth() +1 && jahr == datumEinladung.getYear() + 1900){
                eintraegeEinladungen.add(aktuelleFilmeinladung);
            }
        }

        eintraegeAnzeigen();
    }

    private void eintraegeAnzeigen(){
        StringBuilder eintraege = new StringBuilder();
        if(!eintraegeBewertungen.isEmpty()){
            eintraege.append(eintraegeBewertungen.size() + " Bewertung(en) zu dem/ den folgenden Film(en): ");
            for(int i = 0; i < eintraegeBewertungen.size(); i++){
                if(i != eintraegeBewertungen.size() - 1){
                    eintraege.append(eintraegeBewertungen.get(i).getFilmname() + ", ");
                }
                else{
                    eintraege.append(eintraegeBewertungen.get(i).getFilmname() + ".");
                }
            }
        }

        if(!eintraegeWatchlist.isEmpty()){
            eintraege.append("\n \n");
            eintraege.append(eintraegeWatchlist.size() + " Film(e) zur Watchlist hinzugefügt: ");
            for(int i = 0; i < eintraegeWatchlist.size(); i++){
                if(i != eintraegeWatchlist.size() - 1){
                    eintraege.append(eintraegeWatchlist.get(i).getFilmname() + ", ");
                }
                else{
                    eintraege.append(eintraegeWatchlist.get(i).getFilmname() + ".");
                }
            }
        }

        if(!eintraegeBereitsGesehen.isEmpty()){
            eintraege.append("\n \n");
            eintraege.append(eintraegeBereitsGesehen.size() + " Film(e) als gesehen markiert: ");
            for(int i = 0; i < eintraegeBereitsGesehen.size(); i++){
                if(i != eintraegeBereitsGesehen.size() - 1){
                    eintraege.append(eintraegeBereitsGesehen.get(i).getFilmname() + ", ");
                }
                else{
                    eintraege.append(eintraegeBereitsGesehen.get(i).getFilmname() + ".");
                }
            }
        }

        if(!eintraegeEinladungen.isEmpty()){
            eintraege.append("\n \n");
            eintraege.append(eintraegeEinladungen.size() + " Filmeinladung(en) erhalten: ");
            for(int i = 0; i < eintraegeEinladungen.size(); i++){
                if(i != eintraegeEinladungen.size() - 1){
                    eintraege.append(eintraegeEinladungen.get(i).getFilmname() + ", ");
                }
                else{
                    eintraege.append(eintraegeEinladungen.get(i).getFilmname() + ".");
                }
            }
        }

        textArea_eintraege.setText(String.valueOf(eintraege));
    }

    public void notizAnzeigen(int tag, int monat, int jahr){
        for(int i = 0; i < Kalendereintrag.notizen.size(); i++){
            Notiz aktuelleNotiz = Kalendereintrag.notizen.get(i);
            if(aktuelleNotiz.datum.getDate() == tag && monat == aktuelleNotiz.datum.getMonth() + 1 && jahr == aktuelleNotiz.datum.getYear() + 1900){
                textArea_notizen.setText(aktuelleNotiz.getText());
            }
        }
    }

    public void notizSpeichern(){
        button_speichern.setOnMouseClicked(event ->{
            String notiz = textArea_notizen.getText();
            Date datum = convertLocalToDate(datePicker_datum.getValue());
            if(notiz != null){
                Notiz neueNotiz = new Notiz(AktuellerNutzer.aktuellerNutzer.getId(), notiz, datum);
                try{
                    Response<Notiz> response =  this.notizEndpoint.speichern(neueNotiz).execute();

                    if(response.isSuccessful()){
                        this.text_messageLabel.setTextFill(Color.GREEN);
                        this.text_messageLabel.setText("Die Notiz wurde erfolgreich gespeichert.");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
            else{
                this.text_messageLabel.setTextFill(Color.RED);
                this.text_messageLabel.setText("Bitte Feld beschreiben.");
            }
        });
    }

    public void suche() throws ParseException {
        Date datum = convertLocalToDate(datePicker_datum.getValue());
        leereEintraege();
        getKalendereintraege(datum.getDate(), datum.getMonth() + 1, datum.getYear() + 1900);
        textArea_notizen.setText(null);
        notizAnzeigen(datum.getDate(), datum.getMonth() + 1, datum.getYear() + 1900);
    }

    private Date convertLocalToDate(LocalDate date) {
        Date datum = java.sql.Date.valueOf(date);
        return datum;
    }

    public void leereEintraege(){
        eintraegeBewertungen.clear();
        eintraegeWatchlist.clear();
        eintraegeBereitsGesehen.clear();
        eintraegeEinladungen.clear();
    }

    public List<Filmbewertung> getBewertungenByNutzerId(Long nutzerId){
        Response<List<Filmbewertung>> response = null;
        try{
            response = this.kalenderEndpoint.getBewertungenByNutzerId(nutzerId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null && response.isSuccessful()){
            return response.body();
        }
        return null;
    }

    public List<WatchListItem> getWatchlistByNutzerId(Long nutzerId){
        Response<List<WatchListItem>> response = null;
        try{
            response = this.kalenderEndpoint.getWatchlistByNutzerId(nutzerId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null && response.isSuccessful()){
            return response.body();
        }
        return null;
    }

    public List<BereitsGesehenItem> getBereitsGesehenByNutzerId(Long nutzerId){
        Response<List<BereitsGesehenItem>> response = null;
        try{
            response = this.kalenderEndpoint.getBereitsGesehenByNutzerId(nutzerId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null && response.isSuccessful()){
            return response.body();
        }
        return null;
    }

    public List<Filmeinladung> getFilmeinladungenByNutzerId(Long nutzerId){
        Response<List<Filmeinladung>> response = null;
        try{
            response = this.kalenderEndpoint.getFilmeinladungenByNutzerId(nutzerId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null && response.isSuccessful()){
            return response.body();
        }
        return null;
    }

    public List<Notiz> getNotizenByNutzerId(Long nutzerId){
        Response<List<Notiz>> response = null;
        try{
            response = this.notizEndpoint.getNotizenByNutzerId(nutzerId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null && response.isSuccessful()){
            return response.body();
        }
        return null;
    }
}
