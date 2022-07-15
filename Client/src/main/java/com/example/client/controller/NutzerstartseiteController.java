package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmVorschlagenEndpoint;
import com.example.client.endpoints.FilmlisteEndpoint;
import com.example.client.endpoints.FilmuebersichtEndpoint;
import com.example.client.model.AktuellerNutzer;
import com.example.client.model.ChosenFilm;
import com.example.client.model.Film;
import com.example.client.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
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
import java.util.*;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NutzerstartseiteController implements Initializable {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    FilmlisteEndpoint filmlisteEndpoint;

    @Autowired
    FilmuebersichtEndpoint filmuebersichtEndpoint;

    @Autowired
    FilmeinladungController filmeinladungController;

    @Autowired
    FilmVorschlagenEndpoint filmVorschlagenEndpoint;

    @FXML
    private Label label_notflix;

    @FXML
    private Label label_kategorie;

    @FXML
    private Label label_erscheinungsdatum;

    @FXML
    private MenuBar menuBar_nutzerstartseite;

    @FXML
    private Menu menu_meinedaten;

    @FXML
    private Menu menu_andereNutzer;

    @FXML
    private Menu menu_anfragen;

    @FXML
    private Menu menu_diskussionsgruppen;

    @FXML
    private Menu menu_benachrichtigungen;

    @FXML
    private Menu menu_einstellung;

    @FXML
    private Menu menu_report;

    @FXML
    private MenuItem  menuItem_diskussionsgruppen;

    @FXML
    private MenuItem menuItem_meinedaten;

    @FXML
    private MenuItem menuItem_meineStatistiken;

    @FXML
    private MenuItem menuItem_Kalender;

    @FXML
    private MenuItem menuItem_freundschaftsanfragen;

    @FXML
    private MenuItem menuItem_einladungen;

    @FXML
    private MenuItem menuItem_report;



    @FXML
    private Button button_abmelden;

    @FXML
    private Button button_suchen;

    @FXML
    private Button button_reset;

    @FXML
    private Button button_alle;

    @FXML
    private Button button_fuer_mich;

    @FXML
    private TextField textfield_name;

    @FXML
    private TextField textfield_cast;

    @FXML
    private TableView<Film> tableview_filmliste;

    @FXML
    private TableColumn<Film, String> column_filmName;

    @FXML
    private TableColumn<Film, String> column_kategorie;

    @FXML
    private TableColumn<Film, String> column_filmlaenge;

    @FXML
    private TableColumn<Film, String> column_erscheinungsdatum;

    @FXML
    private TableColumn<Film, String> column_regisseur;

    @FXML
    private TableColumn<Film, String> column_drehbuchautor;

    @FXML
    private ComboBox<String> combobox_kategorie;
    @FXML
    private DatePicker datePicker_erscheinungsdatum;

    private ObservableList<Film> filme;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //Dropdown Kategorie
        ArrayList<String> kategorien = new ArrayList<>();
        kategorien.add("Action");
        kategorien.add("Drama");
        kategorien.add("Comedy");
        kategorien.add("Crime");
        kategorien.add("Fantasy");
        kategorien.add("Horror");
        kategorien.add("Thriller");
        systemInternBenachrichtigen();
        ObservableList<String> liste = FXCollections.observableArrayList(kategorien);

        combobox_kategorie.setItems(liste);

        this.datePicker_erscheinungsdatum.getEditor().setDisable(true);

        try {
            getInformationToTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Navigation
    public void betaetigeAbmelden() throws IOException{
        this.fxmlLoader.setRoot("NutzerLogin");
    }
    public void betaetigeMeineDaten() throws IOException{
        this.fxmlLoader.setRoot("Nutzerprofil");
    }

    public void betaetigeMeineStatistiken() throws IOException{
        this.fxmlLoader.setRoot("NutzerverhaltenStatistik");
    }

    public void betaetigeKalender() throws IOException{
        this.fxmlLoader.setRoot("Kalender");
    }

    public void betaetigeAndereNutzer() throws IOException{
        this.fxmlLoader.setRoot("Nutzersuche");
    }

    public void betaetigeFreundschaftsanfragen() throws IOException{
        this.fxmlLoader.setRoot("Freundschaftsanfragen");
    }

    public void betaetigeEinstellung() throws IOException{
        this.fxmlLoader.setRoot("PrivateEinstellungen");
    }

    public void betaetigeEingegangeneFilmeinladungen() throws IOException{
        this.fxmlLoader.setRoot("Filmeinladung");
    }

    public void betaetigeReport() throws IOException{
        this.fxmlLoader.setRoot("Reportfehler_Nutzer");
    }

    public void zeigeFilmVorschlaegeAn() throws IOException {
        this.fxmlLoader.setRoot("FilmVorschlagen");
    }

    //Herzstück
    public void filmeSuchenUndFiltern() throws ParseException {
        if(combobox_kategorie.getValue() == null && datePicker_erscheinungsdatum.getValue() == null &&  textfield_name.getText().isEmpty() && textfield_cast.getText().isEmpty()){
            this.tableview_filmliste.setItems(this.filme);
        }else {
            ObservableList<Film> movies = FXCollections.observableArrayList();
            for (Film tempfilm : filme) {
                if ((!(combobox_kategorie.getValue() == null) && tempfilm.getKategorie().contains(combobox_kategorie.getValue())) || combobox_kategorie.getValue() == null) {
                    if ((!textfield_name.getText().isEmpty() && tempfilm.getName().contains(textfield_name.getText())) || textfield_name.getText().isEmpty()) {
                        String datum= "";
                        if(datePicker_erscheinungsdatum.getValue()!=null){
                            datum= dateFormatierung(tempfilm);
                        }
                        if (tempfilm.getErscheinungsdatum().equals(datum)|| datePicker_erscheinungsdatum.getValue() == null){
                            if(castVorhanden(getInformationToCastTable(tempfilm.getId()),textfield_cast.getText()) || textfield_cast.getText().isEmpty() ){
                                movies.add(tempfilm);
                            }
                        }
                    }
                }
            }
            this.tableview_filmliste.setItems(movies);
        }
    }

    public void systemInternBenachrichtigen () {
        int anzahl =filmeinladungController.anzahlAkzeptierterAnfragen();
             //   anzahl+=anzahlAkzeptierter;
        System.out.println ("AnzahlDerEinladungen: "+anzahl);
        if (anzahl>0) {
            this.menuItem_einladungen.setText(this.menuItem_einladungen.getText()+" ("+anzahl+")");
        }
    }

    private void getInformationToTable() throws IOException {
        Response<List<Film>> filmNamenResponse = null;
        try {
            filmNamenResponse = filmlisteEndpoint.getAlleFilmNamen().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.filme = FXCollections.observableArrayList();

        if(filmNamenResponse.body() != null) {
            for (Film film: filmNamenResponse.body()) {
                this.filme.add(film);
            }
        }
        this.tableview_filmliste.setItems(this.filme);
        // Filmliste für Nutzer
        column_filmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_filmName.setMinWidth(140.0);
        column_filmName.setEditable(false);
        column_filmName.setCellFactory(TextFieldTableCell.forTableColumn());

        column_kategorie.setCellValueFactory(new PropertyValueFactory<>("kategorie"));
        column_kategorie.setMinWidth(140.0);
        column_kategorie.setEditable(false);
        column_kategorie.setCellFactory(TextFieldTableCell.forTableColumn());

        column_filmlaenge.setCellValueFactory(new PropertyValueFactory<>("filmLaenge"));
        column_filmlaenge.setMinWidth(140.0);
        column_filmlaenge.setEditable(false);
        column_filmlaenge.setCellFactory(TextFieldTableCell.forTableColumn());

        column_erscheinungsdatum.setCellValueFactory(new PropertyValueFactory<>("erscheinungsdatum"));
        column_erscheinungsdatum.setMinWidth(140.0);
        column_erscheinungsdatum.setEditable(false);
        column_erscheinungsdatum.setCellFactory(TextFieldTableCell.forTableColumn());

        this.filmOeffnen();
    }
    // wenn man 2x auf Filmliste draufklickt
    private void filmOeffnen() throws IOException {
        this.tableview_filmliste.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
                    try {
                        ChosenFilm.film = tableview_filmliste.getSelectionModel().getSelectedItem();
                        if(ChosenFilm.film != null) {
                            fxmlLoader.setRoot("NutzerFilmuebersicht");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
    //isValid von stackoverflow
    public boolean isValid(String dateStr) { // überprüfe ob dieses Format des Datums, was im Film gespeichert wird, ob das in dd.MM.yyyy entspricht, ist dies Fall true sonst false
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        sdf.setLenient(false);
        try {
            sdf.parse(dateStr);
        } catch (ParseException e) {
            return false;
        }
        return true;
    }

    public void reset(){
        this.datePicker_erscheinungsdatum.setValue(null);
        this.textfield_name.setText("");
        this.textfield_cast.setText("");
        this.combobox_kategorie.setValue(null);
    }

    // Für FilmCast vergleichen
    // um Liste von personIds bekommen -> mittels FilmId, als Response Liste von personIds zurückgibt
    private ObservableList<Person> getInformationToCastTable(Long filmId) {
        ObservableList<Person> filmCast;
        Response<List<Person>> personenResponse = null;
        try {
            personenResponse = filmuebersichtEndpoint.getPersonenByFilmId(filmId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        filmCast = FXCollections.observableArrayList();
        if (personenResponse != null && personenResponse.body() != null) {
            for (Person person : personenResponse.body()) {
                filmCast.add(person);
            }
        }
        return filmCast;
    }

    public boolean castVorhanden(ObservableList<Person> filmcast, String eingabeCast){
        for (Person p: filmcast) {
            String name = p.getVorname() +" "+ p.getNachname();
            if(name.contains(eingabeCast)){
                return true;
            }
        }
        return false;
    }

    //Datum formatieren
    private String dateFormatierung(Film tempfilm) throws ParseException {
        String datum = "";
        if(datePicker_erscheinungsdatum.getValue() != null && !tempfilm.getErscheinungsdatum().equals("N/A")){
            if (!isValid(tempfilm.getErscheinungsdatum())) {
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
                Date date = dateFormat.parse(datePicker_erscheinungsdatum.getValue().toString());
                datum = dateFormat2.format(date);
            }else{
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd.MM.yyyy");
                Date date = dateFormat.parse(datePicker_erscheinungsdatum.getValue().toString());
                datum = dateFormat2.format(date);
            }
        }
        return datum;
    }

    public void zeigeAlleFilmeAn() throws IOException {
        getInformationToTable();
    }

    public void diskussionsgruppenSeite() throws IOException {
        fxmlLoader.setRoot("Diskussionsgruppen");
    }
}
