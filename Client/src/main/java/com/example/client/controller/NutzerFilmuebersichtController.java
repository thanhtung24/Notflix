package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.BereitsGesehenEndpoint;
import com.example.client.endpoints.FilmuebersichtEndpoint;
import com.example.client.endpoints.WatchListEndpoint;
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
import javafx.scene.text.Text;
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
import java.util.List;
import java.util.ResourceBundle;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class NutzerFilmuebersichtController implements Initializable {
    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    FilmuebersichtEndpoint filmuebersichtEndpoint;

    @Autowired
    WatchListEndpoint watchListEndpoint;

    @Autowired
    BereitsGesehenEndpoint bereitsGesehenEndpoint;

    @FXML
    private Button button_zuWatchListHinzufuegen;

    @FXML
    private Button button_bewertung;

    @FXML
    private Button button_alsGesehenMarkieren;

    @FXML
    private Button button_einladungSenden;

    @FXML
    private Text name;

    @FXML
    private Text kategorie;

    @FXML
    private Text filmlaenge;

    @FXML
    private Text erscheinungsdatum;

    @FXML
    private Text regisseur;

    @FXML
    private Text drehbuchautor;

    @FXML
    private Text cast;

    @FXML
    private TextField text_name;

    @FXML
    private TextField text_kategorie;

    @FXML
    private TextField text_filmlaenge;

    @FXML
    private TextField text_regisseur;

    @FXML
    private TextField text_drehbuchautor;

    @FXML
    private Label label_benachrichtigung;

    @FXML
    private TableView<Person> tableView_cast;

    @FXML
    private TableColumn<Person, String> tableColumn_vorname;
    @FXML
    private TableColumn<Person, String> tableColumn_nachname;

    //DatePicker
    @FXML
    private TextField textField_erscheinungsdatum;

    //ImageView
    @FXML
    private ImageView image;

    private ObservableList<Person> filmCast;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Film film = ChosenFilm.film;
        filmDatenAnzeigen(film);
        disableAllFields();
        initialisiereAlsGesehenMarkierenButton();
        initialisiereWatchlistButton();
    }

    //Navigation
    public void betaetigeZurueck() throws IOException {
        fxmlLoader.setRoot("Nutzerstartseite");
    }

    public void betaetigeBewertung() throws IOException{
        fxmlLoader.setRoot("Filmbewertung");
    }

    public void betaetigeEinladungSenden () throws IOException {
        fxmlLoader.setRoot("FilmEinladungSenden");
    }

    private void filmDatenAnzeigen(Film film) {
        System.out.println("Film Übersicht: Daten von " + film.getName() + " werden angezeigt");

        Person regisseur = getPersonById(film.getRegisseurId());
        Person drebuchautor = getPersonById(film.getDrehbuchautorId());

        this.text_name.setText(film.getName());
        this.text_kategorie.setText(film.getKategorie());
        this.text_filmlaenge.setText(film.getFilmLaenge());
        this.textField_erscheinungsdatum.setText(film.getErscheinungsdatum());
        this.text_regisseur.setText(regisseur.getVorname() + " " + regisseur.getNachname());
        this.text_drehbuchautor.setText(drebuchautor.getVorname() + " " + drebuchautor.getNachname());

        getInformationToCastTable(film.getId());

        showFilmbanner();

    }

    private void disableAllFields() {
        this.text_name.setEditable(false);
        this.text_kategorie.setEditable(false);
        this.text_filmlaenge.setEditable(false);
        this.textField_erscheinungsdatum.setEditable(false);
        this.text_regisseur.setEditable(false);
        this.text_drehbuchautor.setEditable(false);
        this.tableView_cast.setEditable(false);
    }

    private void getInformationToCastTable(Long filmId) {
        Response<List<Person>> personenResponse = null;
        try {
            personenResponse = filmuebersichtEndpoint.getPersonenByFilmId(filmId).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.filmCast = FXCollections.observableArrayList();
        if(personenResponse != null && personenResponse.body() != null) {
            for(Person person: personenResponse.body()) {
                this.filmCast.add(person);
            }
        }

        this.tableView_cast.setItems(this.filmCast);

        this.tableColumn_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        this.tableColumn_vorname.setEditable(false);
        this.tableColumn_vorname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tableColumn_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        this.tableColumn_nachname.setEditable(false);
        this.tableColumn_nachname.setCellFactory(TextFieldTableCell.forTableColumn());
    }


    private void showFilmbanner() {
        try {
            if(ChosenFilm.film.getFilmbanner().length == 0) return;
            InputStream inputStream = new ByteArrayInputStream(ChosenFilm.film.getFilmbanner());

            //wandere filmbanner in Image
            BufferedImage bufferedImage = ImageIO.read(inputStream);
            Image filmBannerImage = SwingFXUtils.toFXImage(bufferedImage, null);
            this.image.setImage(filmBannerImage);
        } catch (IOException ex) {
            System.out.println("Kein Filmbanner vorhanden");
        }
    }

    private Person getPersonById(Long id) {
        Response<Person> response = null;

        try {
            response = this.filmuebersichtEndpoint.getPersonById(id).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body();
    }
    public void zuWatchListHinzufuegen(){
        WatchListItem watchListItem = new WatchListItem(ChosenFilm.film.getId(), AktuellerNutzer.aktuellerNutzer.getId(), ChosenFilm.film.getName());
        try{
            Response<WatchListItem> response = this.watchListEndpoint.filmZuWatchListAnlegen(watchListItem).execute();
            if(response.isSuccessful()){
                this.label_benachrichtigung.setTextFill(Color.GREEN);
                this.label_benachrichtigung.setText(ChosenFilm.film.getName() + " wurde hinzugefuegt");
                this.button_zuWatchListHinzufuegen.setTextFill(Color.GRAY);
                this.button_zuWatchListHinzufuegen.setDisable(true);
                System.out.println("Film wurde zu Watchlist hinzugefügt");
            }
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    private void vonWatchListEntfernen(){
        try{
            Response<WatchListItem> response = this.watchListEndpoint.getWatchListItemByFilmIdAndNutzerId(ChosenFilm.film.getId(), AktuellerNutzer.aktuellerNutzer.getId()).execute();
            if(response.body() != null){
                this.watchListEndpoint.filmVonWatchListEntfernen(ChosenFilm.film.getId()).execute();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    private void initialisiereWatchlistButton(){
        try{
            Response<WatchListItem> response = this.watchListEndpoint.getWatchListItemByFilmIdAndNutzerId(ChosenFilm.film.getId(), AktuellerNutzer.aktuellerNutzer.getId()).execute();
                if(response.body() != null || this.button_alsGesehenMarkieren.isDisabled()){
                    this.button_zuWatchListHinzufuegen.setTextFill(Color.GRAY);
                    this.button_zuWatchListHinzufuegen.setDisable(true);
                }else{
                        this.button_zuWatchListHinzufuegen.setDisable(false);
                }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void alsGesehenMarkieren(){
        BereitsGesehenItem bereitsGesehenItem = new BereitsGesehenItem(ChosenFilm.film.getId(), AktuellerNutzer.aktuellerNutzer.getId(), ChosenFilm.film.getName());
        try{
            Response<BereitsGesehenItem> response = this.bereitsGesehenEndpoint.filmZuBereitsGesehenAnlegen(bereitsGesehenItem).execute();
            if(response.isSuccessful()){
                this.label_benachrichtigung.setTextFill(Color.GREEN);
                this.label_benachrichtigung.setText(ChosenFilm.film.getName() + " wurde als gesehen markiert.");
                this.button_alsGesehenMarkieren.setTextFill(Color.GRAY);
                this.button_alsGesehenMarkieren.setDisable(true);

                System.out.println("Film wurde als gesehen markiert.");
                vonWatchListEntfernen();
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    private void initialisiereAlsGesehenMarkierenButton(){
        try{
            Response<BereitsGesehenItem> response = this.bereitsGesehenEndpoint.getBereitsGesehenItemByFilmIdAndNutzerId(ChosenFilm.film.getId(), AktuellerNutzer.aktuellerNutzer.getId()).execute();
                if(response.body() != null){
                    this.button_alsGesehenMarkieren.setTextFill(Color.GRAY);
                    this.button_alsGesehenMarkieren.setDisable(true);
                }
                else{
                    this.button_alsGesehenMarkieren.setDisable(false);
                }
        }catch (IOException e){
            e.printStackTrace();
        }
    }

}

