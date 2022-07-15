package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmuebersichtEndpoint;
import com.example.client.model.ChosenFilm;
import com.example.client.model.Film;
import com.example.client.model.Person;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class FilmuebersichtController implements Initializable {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    FilmuebersichtEndpoint filmuebersichtEndpoint;

    //Buttons
    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_bearbeiten;


    //Textfelder
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


    //Textfelder
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

    @FXML
    private Button button_statistik;

    private ObservableList<Person> filmCast;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Film film = ChosenFilm.film;
        filmDatenAnzeigen(film);
        disableAllFields();
    }

    public void switchToFilmliste() throws IOException {
        ChosenFilm.BEARBEITUNGSSTATUS = false; // Bei Navigation zur Filmliste Seite muss der Bearbeitungsstatus zu false gesetzt werden
        fxmlLoader.setRoot("Filmliste");
    }

    public void filmBearbeiten() throws IOException {
        ChosenFilm.BEARBEITUNGSSTATUS = true;
        fxmlLoader.setRoot("FilmManuellAnlegenUndBearbeiten");
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

    public void statistikOeffnen(){
        try{
            fxmlLoader.setRoot("FilmStatistik");
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
