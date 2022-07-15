package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.PrivateEinstellungenEndpoint;
import com.example.client.model.AktuellerNutzer;
import com.example.client.model.PrivateEinstellungen;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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
public class PrivateEinstellungenController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private PrivateEinstellungenEndpoint privateEinstellungenEndpoint;

    @FXML
    private Label label_titel;

    @FXML
    private Label label_frage;

    @FXML
    private Label label_watchlist;

    @FXML
    private Label label_geseheneFilmeListe;

    @FXML
    private Label label_freundeListe;

    @FXML
    private Label label_bewertungen;

    @FXML
    private Label label_benachrichtigung;

    @FXML
    private ComboBox<String> comboBox_watchlist;

    @FXML
    private ComboBox<String> comboBox_geseheneFilmeListe;

    @FXML
    private ComboBox<String> comboBox_freundeListe;

    @FXML
    private ComboBox<String> comboBox_bewertungen;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_speichern;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialisiereComboboxes();
    }

    public void speichereEinstellungen() {
        String watchlistEinstellung = this.comboBox_watchlist.getSelectionModel().getSelectedItem();
        String geseheneFilmListeEinstellung = this.comboBox_geseheneFilmeListe.getSelectionModel().getSelectedItem();
        String freundeListeEinstellung = this.comboBox_freundeListe.getSelectionModel().getSelectedItem();
        String bewertungenEinstellung = this.comboBox_bewertungen.getSelectionModel().getSelectedItem();

        Response<PrivateEinstellungen> response = null;
        try {
            response = this.privateEinstellungenEndpoint
                    .speichereEinstellungen(AktuellerNutzer.aktuellerNutzer.getId(), watchlistEinstellung, geseheneFilmListeEinstellung, freundeListeEinstellung, bewertungenEinstellung).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response.isSuccessful()) {
            this.label_benachrichtigung.setTextFill(Color.GREEN);
            this.label_benachrichtigung.setText("Private Einstellungen wurden gespeichert");
            System.out.println("Private Einstellungen wurden gespeichert");
        } else System.out.println("Fehler beim Speichern von privaten Einstellungen");
    }

    public void switchToStartseite() throws IOException {
        this.fxmlLoader.setRoot("Nutzerstartseite");
    }

    private void initialisiereComboboxes() {
        List<String> einstellungen = new ArrayList<>(3);
        einstellungen.add("Alle");
        einstellungen.add("Freunde");
        einstellungen.add("Niemand");

        ObservableList<String> einstellungenObservableList = FXCollections.observableArrayList(einstellungen);

        this.comboBox_watchlist.setItems(einstellungenObservableList);
        this.comboBox_geseheneFilmeListe.setItems(einstellungenObservableList);
        this.comboBox_freundeListe.setItems(einstellungenObservableList);
        this.comboBox_bewertungen.setItems(einstellungenObservableList);


        Response<PrivateEinstellungen> response = null;
        try {
            response = this.privateEinstellungenEndpoint.getPrivateEinstellungen(AktuellerNutzer.aktuellerNutzer.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response.isSuccessful() && response.body() != null) {
            PrivateEinstellungen resEinstellungen = response.body();
            this.comboBox_watchlist.getSelectionModel().select(resEinstellungen.getWatchlistEinstellung());
            this.comboBox_geseheneFilmeListe.getSelectionModel().select(resEinstellungen.getGeseheneFilmeListeEinstellung());
            this.comboBox_freundeListe.getSelectionModel().select(resEinstellungen.getFreundelisteEinstellung());
            this.comboBox_bewertungen.getSelectionModel().select(resEinstellungen.getBewertungenEintstellung());
        }
    }
}
