package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmCastEndpoint;
import com.example.client.endpoints.FilmManuellAnlegenUndBearbeitenEndpoint;
import com.example.client.endpoints.PersonAnlegenEndpoint;
import com.example.client.model.ChosenFilm;
import com.example.client.model.Film;
import com.example.client.model.FilmCast;
import com.example.client.model.Person;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
public class FilmManuellAnlegenUndBearbeitenController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private FilmManuellAnlegenUndBearbeitenEndpoint filmManuellAnlegenUndBearbeitenEndpoint;

    @Autowired
    private PersonAnlegenEndpoint personAnlegenEndpoint;

    @Autowired
    private FilmCastEndpoint filmCastEndpoint;

    //Buttons
    @FXML
    private Button button_filmAnlegen;
    @FXML
    private Button button_zurueck;
    @FXML
    private Button button_folder;
    @FXML
    private Button button_personAnlegen;

    //Labels
    @FXML
    private Label label_filmManuellAnlegen;
    @FXML
    private Label label_name;
    @FXML
    private Label label_kategorie;
    @FXML
    private Label label_filmLaenge;
    @FXML
    private Label label_filmLaengeHinweis;
    @FXML
    private Label label_erscheinungsdatum;
    @FXML
    private Label label_regisseur;
    @FXML
    private Label label_drehbuchautor;
    @FXML
    private Label label_cast;
    @FXML
    private Label label_filmbanner;
    @FXML
    private Label label_benachrichtigung;

    //TextField und DatePicker
    @FXML
    private TextField textfield_name;

    @FXML
    private TextField textfield_filmlaenge;
    @FXML
    private DatePicker datePicker_erscheinungsdatum;

    @FXML
    private ComboBox<String> combobox_kategorie;
    @FXML
    private ComboBox<Person> comboBox_regisseur;
    @FXML
    private ComboBox<Person>  comboBox_drehbuchautor;
    @FXML
    private CheckComboBox<Person> comboBox_cast;
    @FXML
    private TextField bildNameTextField;

    private BufferedImage filmBanner;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ArrayList<String> kategorien = new ArrayList<>();
        kategorien.add("Action");
        kategorien.add("Drama");
        kategorien.add("Comedy");
        kategorien.add("Crime");
        kategorien.add("Fantasy");
        kategorien.add("Horror");
        kategorien.add("Thriller");

        ObservableList<String> liste = FXCollections.observableArrayList(kategorien);

        combobox_kategorie.setItems(liste);

        this.datePicker_erscheinungsdatum.getEditor().setDisable(true);

        ObservableList<Person> personen = findeAllePersonen();
        this.comboBox_regisseur.setItems(personen);
        this.comboBox_drehbuchautor.setItems(personen);
        this.comboBox_cast.getItems().addAll(personen);
        modifiziereCombobox();

        // Wenn ein Film im Bearbeitungsstatus ist, wird die FilmManuellAnlegen Seite zu FilmBearbeiten Seite
        if(ChosenFilm.BEARBEITUNGSSTATUS == true) {
            initialisiereFilmBearbeitenSeite(ChosenFilm.film);
        }
    }


    public void betaetigeZurueckButton() throws IOException {
        if(ChosenFilm.BEARBEITUNGSSTATUS == true) {
            this.fxmlLoader.setRoot("Filmuebersicht"); // Wenn der Bearbeitungsstatus noch an ist, navigiert man zu Filmübersicht seite zurück
            return;
        }
        this.fxmlLoader.setRoot("Filmliste");
    }

    public void switchToPersonAnlegen () throws IOException {
        this.fxmlLoader.setRoot("PersonAnlegen");
    }


    public void filmAnlegen() {
        button_filmAnlegen.setOnMouseClicked(event -> {

            if(!sindEingabenVollstaendig()) {
                this.label_benachrichtigung.setTextFill(Color.RED);
                this.label_benachrichtigung.setText("Keine leeren Felder");
                return;
            }

            if(ChosenFilm.BEARBEITUNGSSTATUS == false && !filmNichtVorhanden()) {
                this.label_benachrichtigung.setTextFill(Color.RED);
                this.label_benachrichtigung.setText(this.textfield_name.getText() + " existiert bereits");
                return;
            }

            Film neuerFilm = new Film(textfield_name.getText(),
                    combobox_kategorie.getSelectionModel().getSelectedItem(),
                    textfield_filmlaenge.getText(),
                    (ChosenFilm.BEARBEITUNGSSTATUS == false) ? formatiereDatum(datePicker_erscheinungsdatum.getEditor().getText()) : datePicker_erscheinungsdatum.getEditor().getText(),
                    comboBox_regisseur.getSelectionModel().getSelectedItem().getId(),
                    comboBox_drehbuchautor.getSelectionModel().getSelectedItem().getId(),
                    getFilmBanner());

            if(ChosenFilm.BEARBEITUNGSSTATUS == true) {
                bearbeiteFilm(ChosenFilm.film, neuerFilm);
                filmCastAnlegen(neuerFilm);
                return;
            }

            System.out.println("Filmanlegen Prozess wird gestartet");
            System.out.println(neuerFilm.getName() + " wird gespeichert");

            try {
                Response<Film> response = this.filmManuellAnlegenUndBearbeitenEndpoint.filmAnlegen(neuerFilm).execute();
                if(response.isSuccessful()) {
                    this.label_benachrichtigung.setTextFill(Color.GREEN);
                    this.label_benachrichtigung.setText(textfield_name.getText() + " wurde erfolgreich hinzugefuegt");

                    filmCastAnlegen(neuerFilm);

                    // dient der Rücknavigation von "Film Bearbeiten" zu "Film Übersicht"
                    // Chosenfilm wird neugesetzt, weil der Film bearbeitet wurde
                    if(ChosenFilm.BEARBEITUNGSSTATUS == true) {
                        ChosenFilm.film = neuerFilm;
                        ChosenFilm.film.setId(response.body().getId());
                    }

                    if(ChosenFilm.BEARBEITUNGSSTATUS == false) {
                        aktualisiereSeite();
                    }

                    System.out.println("Film wurde erfolgreich angelegt");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void bearbeiteFilm(Film film, Film neuerFilm) {
        Response<Film> response = null;
        try {
           response = this.filmManuellAnlegenUndBearbeitenEndpoint.bearbeiteFilm(film.getId(),
                   neuerFilm.getName(),
                   neuerFilm.getKategorie(),
                   neuerFilm.getFilmLaenge(),
                   neuerFilm.getErscheinungsdatum(),
                   neuerFilm.getRegisseurId(),
                   neuerFilm.getDrehbuchautorId(),
                   Base64.getEncoder().encodeToString(neuerFilm.getFilmbanner())
           ).execute();

           ChosenFilm.film = response.body();
        } catch (IOException e) {
            System.out.println("Fehler beim Bearbeiten von " +  film.getName());
        }
        if(response != null && response.isSuccessful()) {
            this.label_benachrichtigung.setTextFill(Color.GREEN);
            this.label_benachrichtigung.setText(film.getName() + " wurde erfolgreich bearbeitet");
        } else {
            this.label_benachrichtigung.setTextFill(Color.RED);
            this.label_benachrichtigung.setText("Fehler beim Bearbeiten von " + film.getName());
        }
    }

    private byte[] getFilmBanner() {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            if(this.filmBanner == null) return new byte[0];
            ImageIO.write(this.filmBanner, "png", outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    private void filmCastAnlegen(Film film) {
        List<FilmCast> filmCastList = new ArrayList<>();

        Long filmId = getFilmId(film);

        List<Person> cast = this.comboBox_cast.getCheckModel().getCheckedItems();

        for(Person person: cast) {
            filmCastList.add(new FilmCast(filmId, person.getId()));
        }
        System.out.println("FilmCastList Size: " + filmCastList.size());

        try {
            for(FilmCast filmCast: filmCastList) {
                this.filmCastEndpoint.filmCastAnlegen(filmCast).execute();
                System.out.println("FilmCastEntity wurde in Datenbank gespeichert");
            }
        } catch (IOException e) {
            System.out.println("Filmcast wurde nicht gespeichert");
            e.printStackTrace();
        }
    }

    public void filmBannerHochladen(javafx.event.ActionEvent actionEvent) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .addAll(new FileChooser.ExtensionFilter("Bilder", "*.png"), new FileChooser.ExtensionFilter("Bilder", "*.jpg"));

        File ausgewaehlteFile = fileChooser.showOpenDialog(null);

        if(ausgewaehlteFile != null && ImageIO.read(ausgewaehlteFile) instanceof BufferedImage) {
            this.bildNameTextField.setText(ausgewaehlteFile.getName());

            BufferedImage image = ImageIO.read(ausgewaehlteFile);

            this.filmBanner = image;

            System.out.println("Filmbanner wurde hochgeladen");
        }
    }

    private ObservableList<Person> findeAllePersonen() {
        Response<List<Person>> response = null;
        try {
            response = this.personAnlegenEndpoint.getAllePersonen().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<Person> personen = FXCollections.observableArrayList();

        if(response.body() != null) {
            for(Person person: response.body()) {
                personen.add(person);
            }
        }
        return personen;
    }

    private Long getFilmId(Film film) {
        Response<Film> filmResponse = null;
        try {
            filmResponse =  this.filmManuellAnlegenUndBearbeitenEndpoint.getFilmByNameAndRegisseur(film.getName(), film.getRegisseurId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filmResponse.body().getId();
    }

    // diese Methode sorgt dafür, dass die comboboxes nur Personennamen anzeigen.
    private void modifiziereCombobox() {
        this.comboBox_regisseur.setConverter(new StringConverter<Person>() {
            @Override
            public String toString(Person person) {
                return (person == null) ? null : person.getVorname() + " " + person.getNachname();
            }

            @Override
            public Person fromString(String s) {
                return null;
            }
        });

        this.comboBox_drehbuchautor.setConverter(new StringConverter<Person>() {
            @Override
            public String toString(Person person) {
                return (person == null) ? null : person.getVorname() + " " + person.getNachname();
            }

            @Override
            public Person fromString(String s) {
                return null;
            }
        });

        this.comboBox_cast.setConverter(new StringConverter<Person>() {
            @Override
            public String toString(Person person) {
                return (person == null) ? null : person.getVorname() + " " + person.getNachname();
            }

            @Override
            public Person fromString(String s) {
                return null;
            }
        });
    }

    private void initialisiereFilmBearbeitenSeite(Film film) {
        this.textfield_name.setText(film.getName());
        this.combobox_kategorie.getSelectionModel().select(film.getKategorie());
        this.textfield_filmlaenge.setText(film.getFilmLaenge());
        this.datePicker_erscheinungsdatum.getEditor().setText(film.getErscheinungsdatum());

        Person regisseur = getPersonById(film.getRegisseurId());
        Person drehbuchautor = getPersonById(film.getDrehbuchautorId());

        this.comboBox_regisseur.getSelectionModel().select(regisseur);
        this.comboBox_drehbuchautor.getSelectionModel().select(drehbuchautor);
        Response<List<Person>> response = null;
        try {
            response = this.filmManuellAnlegenUndBearbeitenEndpoint.getPersonenByFilmId(getFilmId(film)).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Person> cast = response.body();
        for (Person person: cast) {
            this.comboBox_cast.getCheckModel().check(person);
        }

        this.label_filmManuellAnlegen.setText("Film Bearbeiten");
        this.button_filmAnlegen.setText("Film Speichern");
    }

    private Person getPersonById(Long id) {
        Response<Person> response = null;

        try {
            response = this.filmManuellAnlegenUndBearbeitenEndpoint.getPersonById(id).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    private boolean sindEingabenVollstaendig() {
        boolean eingabenGueltig = !this.textfield_name.getText().isEmpty()
                                    && !(this.combobox_kategorie.getSelectionModel().getSelectedItem() == null)
                                    && !this.textfield_filmlaenge.getText().isEmpty()
                                    && !this.datePicker_erscheinungsdatum.getEditor().getText().isEmpty()
                                    && !(this.comboBox_regisseur.getSelectionModel().getSelectedItem() == null)
                                    && !(this.comboBox_drehbuchautor.getSelectionModel().getSelectedItem() == null)
                                    && !(this.comboBox_cast.getCheckModel().getCheckedItems().isEmpty());
        return eingabenGueltig;
    }

    private boolean filmNichtVorhanden() {
        Response<Film> response = null;
        try {
            response = this.filmManuellAnlegenUndBearbeitenEndpoint.getFilmByNameAndRegisseur(this.textfield_name.getText(), this.comboBox_regisseur.getSelectionModel().getSelectedItem().getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(response.isSuccessful() && response.body() != null) {
            return false;
        }
        return true;
    }

    private void aktualisiereSeite() {
        this.textfield_name.setText(null);
        this.combobox_kategorie.getSelectionModel().clearSelection();
        this.textfield_filmlaenge.setText(null);
        this.datePicker_erscheinungsdatum.getEditor().setText(null);
        this.bildNameTextField.setText(null);

        this.comboBox_regisseur.getSelectionModel().clearSelection();
        this.comboBox_drehbuchautor.getSelectionModel().clearSelection();

        this.comboBox_cast.getCheckModel().clearChecks();

        this.bildNameTextField.setText(null);
    }

    private String formatiereDatum(String datum) {
        String[] datumArray = datum.split("\\.");
        String monat = datumArray[1];

        switch (monat) {
            case "01": monat = "Jan"; break;
            case "02": monat = "Feb"; break;
            case "03": monat = "Mar"; break;
            case "04": monat = "Apr"; break;
            case "05": monat = "Mai"; break;
            case "06": monat = "Jun"; break;
            case "07": monat = "Jul"; break;
            case "08": monat = "Aug"; break;
            case "09": monat = "Sep"; break;
            case "10": monat = "Oct"; break;
            case "11": monat = "Nov"; break;
            case "12": monat = "Dec"; break;
        }

        return datumArray[0] + " " + monat + " " + datumArray[2];
    }
}
