package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FreundschaftsanfrageEndpoint;
import com.example.client.endpoints.PrivateEinstellungenEndpoint;
import com.example.client.endpoints.ProfilAndererNutzerEndpoint;
import com.example.client.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class ProfilAndererNutzerController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private ProfilAndererNutzerEndpoint profilAndererNutzerEndpoint;

    @Autowired
    private FreundschaftsanfrageEndpoint freundschaftsanfrageEndpoint;

    @Autowired
    private PrivateEinstellungenEndpoint privateEinstellungenEndpoint;

    @FXML
    private Label label_profil;

    @FXML
    private Label label_vorname;

    @FXML
    private Label label_nachname;

    @FXML
    private Label label_freunde;

    @FXML
    private Label label_watchlist;

    @FXML
    private Label label_bereitsgesehen;

    @FXML
    private Label label_benachrichtigung;

    @FXML
    private TextField textField_vorname;

    @FXML
    private TextField textField_nachname;

    @FXML
    private Button button_startseite;

    @FXML
    private Button button_freundschaftsanfragen;

    @FXML
    private Button button_nutzersuche;

    @FXML
    private Button button_senden;

    @FXML
    private Button button_nachrichtSchreiben;

    @FXML
    private TableView tableView_freunde;

    @FXML
    private TableView tableView_watchlist;

    @FXML
    private TableView tableView_bereitsgesehen;

    @FXML
    private TableColumn<Nutzer, String> tableColumn_vorname;

    @FXML
    private TableColumn<Nutzer, String> tableColumn_nachname;

    @FXML
    private TableColumn<Film, String> tableColumn_watchlist_filmname;

    @FXML
    private TableColumn<Film, String> tableColumn_bereitsgesehen_filmname;

    @FXML
    private ImageView profildbild;

    private final String BUTTON_SENDEN_ANFRAGESENDEN = "Freundschaftsanfrage senden";
    private final String BUTTON_SENDEN_ANFRAGEAKZEPTIEREN = "Freundschaftsanfrage akzeptieren";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        deaktiviereAlleFelder();

        this.textField_vorname.setText(ChosenNutzer.nutzer.getVorname());
        this.textField_nachname.setText(ChosenNutzer.nutzer.getNachname());

        ueberpruefePrivateEinstellungen();

        if(this.tableView_freunde.isVisible()) {
            initialisiereFreundeTabelle();
        }

        if(this.tableView_watchlist.isVisible()) {
            innitialisiereWatchlist();
        }

        if(this.tableView_bereitsgesehen.isVisible()) {
            initialisiereListeBereitsGesehenerFilme();
        }

        initialisiereButtonSenden();
        profilbildAnzeigen();
    }

    private void deaktiviereAlleFelder() {
        this.textField_nachname.setEditable(false);
        this.textField_vorname.setEditable(false);
        this.tableView_freunde.setEditable(false);
        this.tableView_bereitsgesehen.setEditable(false);
        this.tableView_watchlist.setEditable(false);
    }

    private void ueberpruefePrivateEinstellungen() {
        Response<PrivateEinstellungen> response = null;
        try {
            response = this.privateEinstellungenEndpoint.getPrivateEinstellungen(ChosenNutzer.nutzer.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        PrivateEinstellungen privateEinstellungen = response.body();

        // Freundeliste
        boolean freundeListeWirdAngezeigt = ueberpruefePrivateEinstellungen(privateEinstellungen.getFreundelisteEinstellung(), this.tableView_freunde);
        if(!freundeListeWirdAngezeigt) { this.label_freunde.setText("Freundeliste: Nicht sichtbar");}

        // Watchlist
        boolean watchlistWirdAngezeigt =ueberpruefePrivateEinstellungen(privateEinstellungen.getWatchlistEinstellung(), this.tableView_watchlist);
        if(!watchlistWirdAngezeigt) { this.label_watchlist.setText("Watchlist: nicht sichtbar");}

        // gesehene Filme Liste
        boolean geseheneFilmlisteWirdAngezeigt =ueberpruefePrivateEinstellungen(privateEinstellungen.getGeseheneFilmeListeEinstellung(), this.tableView_bereitsgesehen);
        if(!geseheneFilmlisteWirdAngezeigt) { this.label_bereitsgesehen.setText("Filmliste: nicht sichtbar");}
    }

    private boolean ueberpruefePrivateEinstellungen(String privateEinstellung, TableView tableView) {
        if(privateEinstellung.equals("Niemand")) {
            tableView.setVisible(false);
            return false;
        } else if(privateEinstellung.equals("Freunde")) {
            Response<Boolean> response = null;
            try {
                response = this.profilAndererNutzerEndpoint.freundschaftExistiert(AktuellerNutzer.aktuellerNutzer.getId(), ChosenNutzer.nutzer.getId()).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Boolean freundschaftExistiert = response.body();

            if(!freundschaftExistiert) {
                tableView.setVisible(false);
                return false;
            }
        }
        return true;
    }

    private void initialisiereFreundeTabelle() {
        Response<List<Nutzer>> response = null;
        try {
            response = this.profilAndererNutzerEndpoint.getAlleFreunde(ChosenNutzer.nutzer.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<Nutzer> nutzerObservableList = FXCollections.observableArrayList();

        if (response != null && response.isSuccessful() && response.body() != null) {
            nutzerObservableList = FXCollections.observableArrayList(response.body());
        }

        this.tableView_freunde.setItems(nutzerObservableList);

        this.tableColumn_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        this.tableColumn_vorname.setMinWidth(199.0);
        this.tableColumn_vorname.setEditable(false);
        this.tableColumn_vorname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tableColumn_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        this.tableColumn_nachname.setMinWidth(199.0);
        this.tableColumn_nachname.setEditable(false);
        this.tableColumn_nachname.setCellFactory(TextFieldTableCell.forTableColumn());
    }

    private void innitialisiereWatchlist() {
        Response<List<Film>> response = null;
        try {
            response = this.profilAndererNutzerEndpoint.getWatchlist(ChosenNutzer.nutzer.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null && response.isSuccessful() && response.body() != null) {
            ObservableList watchlist = FXCollections.observableArrayList(response.body());

            this.tableView_watchlist.setItems(watchlist);

            this.tableColumn_watchlist_filmname.setCellValueFactory(new PropertyValueFactory<>("name"));
            this.tableColumn_watchlist_filmname.setEditable(false);
            this.tableColumn_watchlist_filmname.setCellFactory(TextFieldTableCell.forTableColumn());
        }
    }

    private void initialisiereListeBereitsGesehenerFilme() {
        Response<List<Film>> response = null;
        try {
            response = this.profilAndererNutzerEndpoint.getlisteBereitsGesehenerFilme(ChosenNutzer.nutzer.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response != null && response.isSuccessful() && response.body() != null) {
            ObservableList listeBereitsGesehenerFilme = FXCollections.observableArrayList(response.body());

            this.tableView_bereitsgesehen.setItems(listeBereitsGesehenerFilme);

            this.tableColumn_bereitsgesehen_filmname.setCellValueFactory(new PropertyValueFactory<>("name"));
            this.tableColumn_bereitsgesehen_filmname.setEditable(false);
            this.tableColumn_bereitsgesehen_filmname.setCellFactory(TextFieldTableCell.forTableColumn());
        }
    }

    private void initialisiereButtonSenden() {
        // wenn der aktuelle Nutzer bereits eine Freundschaftsanfrage vom anderen Nutzer bekommen hat,
        // wird der Button "Freundschaftsanfrage senden" zu "Freundschaftsanfrage akzeptieren" umbenannt

        Long meineID = AktuellerNutzer.aktuellerNutzer.getId();
        Long aufgesuchterNutzerId = ChosenNutzer.nutzer.getId();

        Response<Boolean> response = null;
        try {
            response = this.freundschaftsanfrageEndpoint.anfrageExistiert(aufgesuchterNutzerId, meineID).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.body() == true) {
            this.button_senden.setText(BUTTON_SENDEN_ANFRAGEAKZEPTIEREN);
            return;
        }

        // wenn der aktuelle Nutzer bereits eine Freundschaftsanfrage an den anderen Nutzer gesendet hat,
        // wird der Button "Freundschaftsanfrage senden" nicht anklickbar sein
        try {
            response = this.freundschaftsanfrageEndpoint.anfrageExistiert(meineID, aufgesuchterNutzerId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.body() == true) {
            this.button_senden.setDisable(true);
            return;
        }

        // wenn der Nutzer bereits ein Freund vom aktuellen Nutzer ist,
        // wird der Button "Freundschaftsanfrage senden" nicht anklickbar sein und zu "Ihr seid Freunde" umbenannt
        try {
            response = this.profilAndererNutzerEndpoint.freundschaftExistiert(meineID, aufgesuchterNutzerId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.body() == true) {
            this.button_senden.setText("Ihr seid Freunde");
            this.button_senden.setDisable(true);
        }
    }

    public void sendOrAcceptRequest() {

        Long anfrageSenderId = AktuellerNutzer.aktuellerNutzer.getId();
        Long aufgesuchterNutzerId = ChosenNutzer.nutzer.getId();

        if(this.button_senden.getText().equals(BUTTON_SENDEN_ANFRAGESENDEN)){

            Response<Freundschaftsanfrage> response = null;
            try {
                response = this.freundschaftsanfrageEndpoint.sendeFreundschaftsanfrage(new Freundschaftsanfrage(aufgesuchterNutzerId, anfrageSenderId)).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.isSuccessful()) {
                this.button_senden.setTextFill(Color.GRAY);
                this.button_senden.setDisable(true);
                this.label_benachrichtigung.setTextFill(Color.GREEN);
                this.label_benachrichtigung.setText("Freundschaftsanfrage wurde gesendet");
                System.out.println("Freundschaftsanfrage wurde gesendet");
                return;
            }
        }

        if(this.button_senden.getText().equals(BUTTON_SENDEN_ANFRAGEAKZEPTIEREN)){
            Response<List<Freundschaft>> response = null;
            try {
                List<Freundschaft> freundschaften = new ArrayList<>();
                freundschaften.add(new Freundschaft(anfrageSenderId, aufgesuchterNutzerId));
                freundschaften.add(new Freundschaft(aufgesuchterNutzerId, anfrageSenderId));

                response = this.profilAndererNutzerEndpoint.akzeptiereAnfrage(freundschaften).execute();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (response.isSuccessful()) {
                this.button_senden.setTextFill(Color.GRAY);
                this.button_senden.setDisable(true);
                this.label_benachrichtigung.setTextFill(Color.GREEN);
                this.label_benachrichtigung.setText("Freundschaftsanfrage wurde akzeptiert");
                System.out.println("Freundschaftsanfrage wurde akzeptiert");
            }
        }
    }

    private void profilbildAnzeigen() {
        try {
            byte[] profilbildByte = ChosenNutzer.nutzer.getProfilbild();
            if(profilbildByte == null || profilbildByte.length == 0) return;
            InputStream inputStream = new ByteArrayInputStream(profilbildByte);

            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Image profilbildImage = SwingFXUtils.toFXImage(bufferedImage, null);
            this.profildbild.setImage(profilbildImage);
        } catch (IOException ex) {
            System.out.println("Kein Profilbild vorhanden");
        }
    }

    public void switchToStartseite() throws IOException {
        ChosenNutzer.nutzer = null;
        this.fxmlLoader.setRoot("Nutzerstartseite");
    }

    public void switchToFreundschaftsanfragen() throws IOException {
        ChosenNutzer.nutzer = null;
        this.fxmlLoader.setRoot("Freundschaftsanfragen");
    }

    public void switchToNutzersuche() throws IOException {
        ChosenNutzer.nutzer = null;
        this.fxmlLoader.setRoot("Nutzersuche");
    }

    public void switchToChat() throws IOException {
        fxmlLoader.setRoot("PrivaterChat");
    }

}
