package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmlisteEndpoint;
import com.example.client.model.ChosenFilm;
import com.example.client.model.Film;
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
import java.util.List;
import java.util.ResourceBundle;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FilmlisteController implements Initializable {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    FilmlisteEndpoint filmlisteEndpoint;

    @FXML
    private Label label_notflix;

    @FXML
    private TableView<Film> tableview_filmliste;

    @FXML
    private TableColumn<Film, String> column_filmName;

    @FXML
    private Label label_filmAnlegen;

    @FXML
    private Button button_filmManuellAnlegen;

    @FXML
    private Button button_filmAutoAnlegen;

    @FXML
    private Button button_abmelden;

    @FXML
    private Button button_reportanfragen;


    private ObservableList<Film> filmNamen;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getInformationToTable();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void switchToFilmManuellAnlegen () throws IOException {
        this.fxmlLoader.setRoot("FilmManuellAnlegenUndBearbeiten");
    }

    @FXML
    public void switchToReportanfragen () throws IOException{
        this.fxmlLoader.setRoot("ReportanfrageAdmin");
    }


    @FXML
    public void switchToFilmManuellAutomatischAnlegen () throws IOException {
        this.fxmlLoader.setRoot("FilmAutomatisiertAnlegenAngepasst");
    }

    @FXML
    public void abmelden() throws IOException {
        this.fxmlLoader.setRoot("SystemadministratorEinloggen");
    }

    @FXML
    public void reportanfragen() throws IOException {
        this.fxmlLoader.setRoot("ReportanfrageAdmin");
    }

    private void getInformationToTable() throws IOException {
        Response<List<Film>> filmNamenResponse = null;
        try {
            filmNamenResponse = filmlisteEndpoint.getAlleFilmNamen().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //
        this.filmNamen = FXCollections.observableArrayList();

        if(filmNamenResponse.body() != null) {
            for (Film film: filmNamenResponse.body()) {
                this.filmNamen.add(film);
            }
        }
        this.tableview_filmliste.setItems(this.filmNamen);

        column_filmName.setCellValueFactory(new PropertyValueFactory<>("name"));
        column_filmName.setMinWidth(199.0);
        column_filmName.setEditable(false);
        column_filmName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.filmOeffnen();
    }

    private void filmOeffnen() {
        this.tableview_filmliste.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
                    try {
                        ChosenFilm.film = tableview_filmliste.getSelectionModel().getSelectedItem();
                        if(ChosenFilm.film != null) {
                            fxmlLoader.setRoot("Filmuebersicht");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
