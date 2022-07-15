package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmeinladungSendenEndpoint;
import com.example.client.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)


public class FilmeinladungController {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    FilmeinladungSendenEndpoint filmeinladungSendenEndpoint;

  //  @Autowired
  //NutzerstartseiteController NutzerstartseiteController;

    @FXML
    private Label label_einladung_zum_film;

    @FXML
    private TableView <Filmeinladung> tableView_einladungen;

    @FXML
    private TableColumn <Film, String> tableColumn_filmname;

    @FXML
    private TableColumn <Nutzer, String> tableColumn_nutzer;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_offene;

    @FXML
    private Button button_akzeptierte;

    @FXML //So richtig?
    public void initialize (){
        initialisiereFilmeinladungstabelle();
    }
    public void switchToStartseite () throws IOException {
        this.fxmlLoader.setRoot("Nutzerstartseite");
    }


    public void initialisiereOffeneButton ()throws IOException {
        this.button_offene.setOnMouseClicked(event-> {
            initialisiereFilmeinladungstabelle();
       });
    }

    private void initialisiereFilmeinladungstabelle(){
        Response<List<Filmeinladung>> response = null;

        try {
            response =this.filmeinladungSendenEndpoint.getAlleFilmeinladungen(AktuellerNutzer.aktuellerNutzer.getId()).execute();

        } catch (IOException e){
            e.printStackTrace();
        }

        ObservableList<Filmeinladung> filmeinladungenObservalableList = FXCollections.observableArrayList();

        if (response != null && response.isSuccessful() & response.body()!=null){
            for (Filmeinladung filmeinladung : response.body()){
                if (!filmeinladung.isAkzeptiert()) {
                    filmeinladungenObservalableList.add(filmeinladung);
                }
            }
        }

        this.tableView_einladungen.setItems(filmeinladungenObservalableList);
        this.tableColumn_filmname.setCellValueFactory(new PropertyValueFactory<>("filmname"));
        this.tableColumn_nutzer.setCellValueFactory(new PropertyValueFactory<>("einladungssenderName"));

        try {
            einladungOeffnen();
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public int anzahlAkzeptierterAnfragen() {

       int anzahl =0;
        Response<List<Filmeinladung>> response = null;

        try {
            response =this.filmeinladungSendenEndpoint.getAlleFilmeinladungen(AktuellerNutzer.aktuellerNutzer.getId()).execute();
        } catch (IOException e){
            e.printStackTrace();
        }

        if (response != null && response.isSuccessful() & response.body()!=null){
            for (Filmeinladung filmeinladung : response.body()){
                if (!filmeinladung.isAkzeptiert()){
                    anzahl++;
                }
            }
        }

        Response <List<Filmeinladung>> response2=null;

        try { //alle von anderen Akzeptierten Filmeinladungen -> Theoretisch
           response2 =this.filmeinladungSendenEndpoint.getAlleFilmeinladungenWhereAkzeptiert(AktuellerNutzer.aktuellerNutzer.getId()).execute();
        } catch (IOException e){
            e.printStackTrace();
        }

        if (response2 != null && response2.isSuccessful() & response2.body()!=null){
            for (Filmeinladung filmeinladung : response2.body()){
                System.out.println(filmeinladung.isGesehen());
                if (!filmeinladung.isGesehen()){
                    anzahl++;
                }
            }
        }

        return anzahl;
    }

    public void akzeptierteEinladungen (){

        int anzahl=0;
        this.button_akzeptierte.setOnMouseClicked(event-> {
            Response <List<Filmeinladung>> response = null;
            Response <List<Filmeinladung>> response2 = null;
            try {
                response =this.filmeinladungSendenEndpoint.getAlleFilmeinladungen(AktuellerNutzer.aktuellerNutzer.getId()).execute();
                response2=this.filmeinladungSendenEndpoint.getAlleFilmeinladungenWhereAkzeptiert(AktuellerNutzer.aktuellerNutzer.getId()).execute();



            } catch (IOException e){
                e.printStackTrace();
            }

            ObservableList<Filmeinladung> offeneFilmeinladungenObservalableList = FXCollections.observableArrayList();

            if (response != null && response.isSuccessful() & response.body()!=null){
                for (Filmeinladung filmeinladung : response.body()){
                    if (filmeinladung.isAkzeptiert()) {
                        offeneFilmeinladungenObservalableList.add(filmeinladung);
                    }
                }
            }

            if (response2 != null && response2.isSuccessful() & response2.body()!=null){
                for (Filmeinladung filmeinladung: response2.body()){
                    offeneFilmeinladungenObservalableList.add(filmeinladung);
                }
            }

            this.tableView_einladungen.setItems(offeneFilmeinladungenObservalableList);
            this.tableColumn_filmname.setCellValueFactory(new PropertyValueFactory<>("filmname"));
            this.tableColumn_nutzer.setCellValueFactory(new PropertyValueFactory<>("einladungssenderName"));


        });

        try {
            einladungOeffnen();
        } catch (IOException e){
            e.printStackTrace();
        }
    }


    private void einladungOeffnen() throws IOException {
        this.tableView_einladungen.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2) {
                    try {
                        Object temp =tableView_einladungen.getSelectionModel().getSelectedItem();
                        ChosenFilmeinladung.filmeinladung= (Filmeinladung) temp;

                        if (ChosenFilmeinladung.filmeinladung!= null){
                            fxmlLoader.setRoot("FilmEinladungsdetail");
                            if (ChosenFilmeinladung.filmeinladung.isAkzeptiert() && ChosenFilmeinladung.filmeinladung.getEinladungssenderId()==AktuellerNutzer.aktuellerNutzer.getId()){
                                    try {
                                        Response<Filmeinladung>  response = filmeinladungSendenEndpoint.geseheneAkzeptiereFilmeinladung(ChosenFilmeinladung.filmeinladung.getId()).execute();
                                        System.out.println(response.isSuccessful());
                                    } catch (IOException e){
                                        e.printStackTrace();
                                    }

                                }
                            }

                    } catch (IOException e){
                        e.printStackTrace();
                    }
                }
            }
        });
    }



}
