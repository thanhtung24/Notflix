package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.NutzersucheEndpoint;
import com.example.client.model.AktuellerNutzer;
import com.example.client.model.ChosenNutzer;
import com.example.client.model.Nutzer;
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

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class NutzersucheController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private NutzersucheEndpoint nutzersucheEndpoint;

    @FXML
    private Label label_vorname;

    @FXML
    private Label label_nachname;

    @FXML
    private Label label_filter;

    @FXML
    private TextField textField_vorname;

    @FXML
    private TextField textField_nachname;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_alle;

    @FXML
    private Button button_meineFreunde;

    @FXML
    private Button button_suchen;

    @FXML
    private TableView<Nutzer> tableView_nutzer;

    @FXML
    private TableColumn<Nutzer, String> tableColumn_vorname;

    @FXML
    private TableColumn<Nutzer, String> tableColumn_nachname;

    private ObservableList<Nutzer> nutzerObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialisiereNutzerTabelle();
        nutzerAufsuchen();
    }

    public void switchToNutzerstartseite() throws IOException {
        ChosenNutzer.nutzer = null;
        this.fxmlLoader.setRoot("Nutzerstartseite");
    }

    public void suche() {
        ObservableList<Nutzer> result = FXCollections.observableArrayList();
        String vorname = this.textField_vorname.getText();
        String nachname = this.textField_nachname.getText();

        if(!vorname.isEmpty() && !nachname.isEmpty()) {
            for(Nutzer nutzer: this.nutzerObservableList) {
                if(nutzer.getVorname().equals(vorname) && nutzer.getNachname().equals(nachname)) {
                    result.add(nutzer);
                }
            }
        } else if(!vorname.isEmpty() && nachname.isEmpty()) {
            for(Nutzer nutzer: this.nutzerObservableList) {
                if(nutzer.getVorname().equals(vorname)) {
                    result.add(nutzer);
                }
            }
        } else if(!nachname.isEmpty() && vorname.isEmpty()) {
            for(Nutzer nutzer: this.nutzerObservableList) {
                if(nutzer.getNachname().equals(nachname)) {
                    result.add(nutzer);
                }
            }
        } else {
            if(this.label_filter.getText().equals("Alle")) { zeigeAlleNutzerAn(); return; }
            if(this.label_filter.getText().equals("Freunde")) { zeigeAlleFreundeAn(); return; }
        }
        this.tableView_nutzer.setItems(result);
    }

    public void zeigeAlleNutzerAn() {
        initialisiereNutzerTabelle();
    }

    public void zeigeAlleFreundeAn() {
        nutzerObservableList.clear();
        Response<List<Nutzer>> response = null;
        try {
            response = this.nutzersucheEndpoint.getAlleFreunde(AktuellerNutzer.aktuellerNutzer.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null && response.isSuccessful() && response.body() != null) {
            this.nutzerObservableList = FXCollections.observableArrayList(response.body());
        }

        this.tableView_nutzer.setItems(nutzerObservableList);

        this.tableColumn_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        this.tableColumn_vorname.setMinWidth(199.0);
        this.tableColumn_vorname.setEditable(false);
        this.tableColumn_vorname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tableColumn_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        this.tableColumn_nachname.setMinWidth(199.0);
        this.tableColumn_nachname.setEditable(false);
        this.tableColumn_nachname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.label_filter.setText("Freunde");
    }

    private void initialisiereNutzerTabelle() {
        nutzerObservableList.clear();
        Response<List<Nutzer>> response = null;
        try {
            response = this.nutzersucheEndpoint.getAlleNutzer().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (response != null && response.isSuccessful() && response.body() != null) {
            this.nutzerObservableList = FXCollections.observableArrayList(response.body());
            this.nutzerObservableList.remove(AktuellerNutzer.aktuellerNutzer);
        }

        this.tableView_nutzer.setItems(this.nutzerObservableList);

        this.tableColumn_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        this.tableColumn_vorname.setMinWidth(199.0);
        this.tableColumn_vorname.setEditable(false);
        this.tableColumn_vorname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tableColumn_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        this.tableColumn_nachname.setMinWidth(199.0);
        this.tableColumn_nachname.setEditable(false);
        this.tableColumn_nachname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.label_filter.setText("Alle");
    }

    private void nutzerAufsuchen() {
        this.tableView_nutzer.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
                    try {
                        ChosenNutzer.nutzer = tableView_nutzer.getSelectionModel().getSelectedItem();
                        if(ChosenNutzer.nutzer != null) {
                            fxmlLoader.setRoot("ProfilAndererNutzer");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }
}
