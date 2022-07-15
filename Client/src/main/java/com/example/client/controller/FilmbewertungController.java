package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmbewertungEndpoint;
import com.example.client.endpoints.PrivateEinstellungenEndpoint;
import com.example.client.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class FilmbewertungController implements Initializable {

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_bewertungSenden;

    @FXML
    private Button button_bewertungBearbeiten;

    @FXML
    private ComboBox<String> combobox_sterne;

    @FXML
    private TextArea textarea_kommentar;

    @FXML
    private TableView<Filmbewertung> tableview_bewertungenAndererNutzer;

    @FXML
    private TableColumn<Nutzer, String> tablecolumn_vorname;

    @FXML
    private TableColumn<Nutzer, String> tablecolumn_nachname;

    @FXML
    private TableColumn<Filmbewertung, String> tablecolumn_sterne;

    @FXML
    private Label text_warningLabel;

    @FXML
    private Label text_messageLabel;

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private FilmbewertungEndpoint filmbewertungEndpoint;

    @Autowired
    private PrivateEinstellungenEndpoint privateEinstellungenEndpoint;

    private ObservableList<Filmbewertung> nutzerUndBewertung;


    public void zurueckZurFilmuebersicht() throws IOException{
        AktuelleBewertung.aktuelleBewertung = null;
        AktuelleBewertung.BEARBEITUNGSSTATUS = false;
        fxmlLoader.setRoot("NutzerFilmuebersicht");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<String> sterne = new ArrayList<>();
        sterne.add("1 Stern");
        sterne.add("2 Sterne");
        sterne.add("3 Sterne");
        sterne.add("4 Sterne");
        sterne.add("5 Sterne");

        ObservableList<String> liste = FXCollections.observableArrayList(sterne);
        combobox_sterne.setItems(liste);
        AktuelleBewertung.aktuelleBewertung = getBewertungByFilmIdAndNutzerId(ChosenFilm.film.getId(), AktuellerNutzer.aktuellerNutzer.getId());
        filmbewertungAnzeigen(AktuelleBewertung.aktuelleBewertung);
        try {
            getInformationToTable(ChosenFilm.film.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void filmbewertungAnzeigen(Filmbewertung filmbewertung){
        if(filmbewertung != null){
            this.combobox_sterne.setValue(filmbewertung.getSterne());
            this.textarea_kommentar.setText(filmbewertung.getKommentar());


            disableAllFields();
            this.button_bewertungBearbeiten.setTextFill(Color.BLACK);
            this.button_bewertungBearbeiten.setDisable(false);
            this.button_bewertungSenden.setTextFill(Color.GRAY);
            this.button_bewertungSenden.setDisable(true);
        }
    }

    public void absenden(){
        button_bewertungSenden.setOnMouseClicked(event ->{
            if(AktuelleBewertung.BEARBEITUNGSSTATUS == true){
                enableAllFields();
            }
            String sterne = combobox_sterne.getValue();
            String kommentar = textarea_kommentar.getText();

            if(combobox_sterne.getSelectionModel().getSelectedItem() != null){
                Filmbewertung neueFilmbewertung = new Filmbewertung(ChosenFilm.film.getId(), AktuellerNutzer.aktuellerNutzer.getId(), AktuellerNutzer.aktuellerNutzer.getVorname(), AktuellerNutzer.aktuellerNutzer.getNachname(), sterne, kommentar, ChosenFilm.film.getName());

                if(AktuelleBewertung.BEARBEITUNGSSTATUS == true){
                    bewertungBearbeiten(AktuelleBewertung.aktuelleBewertung, neueFilmbewertung);
                    return;
                }

                try{
                    Response<Filmbewertung> response = this.filmbewertungEndpoint.absenden(neueFilmbewertung).execute();

                    if(response.isSuccessful()){
                        this.text_messageLabel.setTextFill(Color.GREEN);
                        this.text_messageLabel.setText("Die Bewertung wurde erfolgreich abgesendet.");
                        AktuelleBewertung.aktuelleBewertung = response.body();

                        this.combobox_sterne.setDisable(true);
                        this.textarea_kommentar.setEditable(false);

                        this.button_bewertungSenden.setTextFill(Color.GRAY);
                        this.button_bewertungSenden.setDisable(true);
                        this.button_bewertungBearbeiten.setTextFill(Color.BLACK);
                        this.button_bewertungBearbeiten.setDisable(false);
                    }

                } catch(IOException e){
                    e.printStackTrace();
                }

            }
            else{
                this.text_warningLabel.setTextFill(Color.RED);
                this.text_warningLabel.setText("Bitte wählen Sie eine Anzahl an Sternen.");
            }
        });
    }

    public void bewertungBearbeitenInitialisieren(){
        AktuelleBewertung.BEARBEITUNGSSTATUS = true;
        enableAllFields();
        this.button_bewertungSenden.setTextFill(Color.BLACK);
        this.button_bewertungSenden.setDisable(false);
        absenden();
    }

    private void bewertungBearbeiten(Filmbewertung filmBewertung, Filmbewertung neueFilmbewertung){
        Response<Filmbewertung> response = null;
        try{
            response = this.filmbewertungEndpoint.bewertungBearbeiten(filmBewertung.getId(),
                        filmBewertung.getFilmId(),
                        filmBewertung.getNutzerId(),
                        filmBewertung.getVorname(),
                        filmBewertung.getNachname(),
                        neueFilmbewertung.getSterne(),
                        neueFilmbewertung.getKommentar()).execute();

            AktuelleBewertung.aktuelleBewertung = response.body();
        } catch(IOException e){
            System.out.println("Fehler beim Bearbeiten der Bewertung.");
        }
        if(response != null && response.isSuccessful()){
            this.text_messageLabel.setTextFill(Color.GREEN);
            this.text_messageLabel.setText("Die Bewertung wurde erfolgreich bearbeitet.");

            this.button_bewertungSenden.setTextFill(Color.GRAY);
            this.button_bewertungSenden.setDisable(true);
            disableAllFields();
        }
        else{
            this.text_messageLabel.setTextFill(Color.RED);
            this.text_messageLabel.setText("Fehler beim Bearbeiten der Bewertung.");
        }
    }

    private void disableAllFields(){
        this.combobox_sterne.setDisable(true);
        this.textarea_kommentar.setEditable(false);
    }

    private void enableAllFields(){
        this.combobox_sterne.setDisable(false);
        this.textarea_kommentar.setEditable(true);
    }

    private void getInformationToTable(Long filmId) throws IOException {
        Response<List<Filmbewertung>> bewertungenResponse = null;
        try{
            bewertungenResponse = this.filmbewertungEndpoint.getBewertungenByFilmId(filmId).execute();

        } catch(IOException e){
            e.printStackTrace();
        }
        this.nutzerUndBewertung = FXCollections.observableArrayList();
        if(bewertungenResponse != null && bewertungenResponse.body() != null){
            for(Filmbewertung filmbewertung : bewertungenResponse.body()){
                if(!filmbewertung.getNutzerId().equals(AktuellerNutzer.aktuellerNutzer.getId()) && bewertungSollAngezeigtWerden(filmbewertung)){
                    this.nutzerUndBewertung.add(filmbewertung);
                }
            }
        }

        this.tableview_bewertungenAndererNutzer.setItems(nutzerUndBewertung);

        this.tablecolumn_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        this.tablecolumn_vorname.setEditable(false);
        this.tablecolumn_vorname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tablecolumn_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        this.tablecolumn_nachname.setEditable(false);
        this.tablecolumn_nachname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tablecolumn_sterne.setCellValueFactory(new PropertyValueFactory<>("sterne"));
        this.tablecolumn_sterne.setEditable(false);
        this.tablecolumn_sterne.setCellFactory(TextFieldTableCell.forTableColumn());

        this.bewertungOeffnen();
    }

    private boolean bewertungSollAngezeigtWerden(Filmbewertung filmbewertung) {
        Response<PrivateEinstellungen> response = null;
        try {
            response = this.privateEinstellungenEndpoint.getPrivateEinstellungen(filmbewertung.getNutzerId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String filmBewertungEinstellung = response.body().getBewertungenEintstellung();

        if(filmBewertungEinstellung.equals("Niemand")) {
            return false;
        } else if(filmBewertungEinstellung.equals("Freunde")) {
            Response<Boolean> rp = null;
            try {
                rp = this.filmbewertungEndpoint.freundschaftExistiert(AktuellerNutzer.aktuellerNutzer.getId(), filmbewertung.getNutzerId()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Boolean freundschaftExistiert = rp.body();

            if(!freundschaftExistiert) { return false; }
        }
        return true;
    }

    private void bewertungOeffnen() throws IOException{
        this.tableview_bewertungenAndererNutzer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2){
                    try{
                        AktuelleBewertung.aktuelleBewertung = tableview_bewertungenAndererNutzer.getSelectionModel().getSelectedItem();
                        AktuelleBewertung.aktuelleBewertung.setSterne(tableview_bewertungenAndererNutzer.getSelectionModel().getSelectedItem().getSterne());
                        AktuelleBewertung.aktuelleBewertung.setKommentar(tableview_bewertungenAndererNutzer.getSelectionModel().getSelectedItem().getKommentar());
                        if(AktuelleBewertung.aktuelleBewertung != null){
                            fxmlLoader.setRoot("Bewertungsdetail");
                        }
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public Filmbewertung getBewertungByFilmIdAndNutzerId(Long filmId, Long nutzerId){
            Response<Filmbewertung> response = null;
            try {
                response = this.filmbewertungEndpoint.getBewertungenByFilmIdAndNutzerId(filmId, nutzerId).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

        if(response != null && response.isSuccessful()) {
            return response.body();
        }
        return null;
    }
}
