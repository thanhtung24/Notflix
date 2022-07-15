package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FreundschaftsanfrageEndpoint;
import com.example.client.model.AktuellerNutzer;
import com.example.client.model.ChosenNutzer;
import com.example.client.model.Nutzer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
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
public class FreundschaftsanfrageController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private FreundschaftsanfrageEndpoint freundschaftsanfrageEndpoint;

    @FXML
    private Label label_freundschaftsanfragen;

    @FXML
    private Button button_startseite;

    @FXML
    private TableView<Nutzer> tableView_nuzter;

    @FXML
    private TableColumn<Nutzer, String> tableColumn_vorname;

    @FXML
    private TableColumn<Nutzer, String> tableColumn_nachname;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initialisiereFreundschaftsanfragenTabelle();
        nutzerAufsuchen();
    }

    public void switchToNutzerstartseite() throws IOException {
        this.fxmlLoader.setRoot("Nutzerstartseite");
    }

    private void initialisiereFreundschaftsanfragenTabelle() {
        Response<List<Nutzer>> response = null;

        try {
            response = this.freundschaftsanfrageEndpoint.getAlleFreundschaftsanfragen(AktuellerNutzer.aktuellerNutzer.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ObservableList<Nutzer> freundschaftsanfrageObservableList =  FXCollections.observableArrayList();

        if(response != null && response.isSuccessful() && response.body() != null) {
            for(Nutzer nutzer: response.body()) {
                freundschaftsanfrageObservableList.add(nutzer);
            }
        }

        this.tableView_nuzter.setItems(freundschaftsanfrageObservableList);

        this.tableColumn_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
        this.tableColumn_vorname.setMinWidth(199.0);
        this.tableColumn_vorname.setEditable(false);
        this.tableColumn_vorname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tableColumn_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
        this.tableColumn_nachname.setMinWidth(199.0);
        this.tableColumn_nachname.setEditable(false);
        this.tableColumn_nachname.setCellFactory(TextFieldTableCell.forTableColumn());

    }

    private void nutzerAufsuchen() {
        this.tableView_nuzter.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
                    try {
                        ChosenNutzer.nutzer = tableView_nuzter.getSelectionModel().getSelectedItem();
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
