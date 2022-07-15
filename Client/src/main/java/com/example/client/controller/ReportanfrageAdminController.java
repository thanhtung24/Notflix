package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.ReportanfrageAdminEndpoint;
import com.example.client.model.ChosenReport;
import com.example.client.model.Report;
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
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data

public class ReportanfrageAdminController implements Initializable {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    ReportanfrageAdminEndpoint reportanfrageAdminEndpoint;

    @FXML
    private Button button_zurueck;

    @FXML
    private Label label_reportanfragen;

    @FXML
    private TableView <Report> tableView_reportanfrage;

    @FXML
    private TableColumn<Report, String> tableColumn_filmname;

    @FXML
    private TableColumn<Report, String> tableColumn_regisseur;

    @FXML
    private TableColumn <Report, String> tableColumn_fehlerIn;

    ObservableList<Report> report;

    @FXML
    public void switchToFilmliste() throws IOException {
        this.fxmlLoader.setRoot("Filmliste");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            getInformationenInTabelle();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void getInformationenInTabelle() throws IOException {
        Response <List<Report>> reportResponse=null;

        try {
           reportResponse = reportanfrageAdminEndpoint.getAlleReports().execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.report= FXCollections.observableArrayList();

        try{
             for (Report report : reportResponse.body()) {
                   if (!report.isErledigt()){
                       this.report.add(report);
                   }
             }

        } catch (NullPointerException e){
                e.printStackTrace();
        }



        this.tableView_reportanfrage.setItems(this.report);

        this.tableColumn_filmname.setCellValueFactory(new PropertyValueFactory<>("Filmname"));
        this.tableColumn_filmname.setEditable(false);
        this.tableColumn_filmname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tableColumn_regisseur.setCellValueFactory(new PropertyValueFactory<>("Regisseur"));
        this.tableColumn_regisseur.setEditable(false);
        this.tableColumn_regisseur.setCellFactory(TextFieldTableCell.forTableColumn());

        this.tableColumn_fehlerIn.setCellValueFactory(new PropertyValueFactory<>("Fehler_in"));
        this.tableColumn_fehlerIn.setEditable(false);
        this.tableColumn_fehlerIn.setCellFactory(TextFieldTableCell.forTableColumn());

        reportOeffnen();
    }

    private void reportOeffnen () {

        this.tableView_reportanfrage.setOnMousePressed(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {

                    try {
                        ChosenReport.report = tableView_reportanfrage.getSelectionModel().getSelectedItem();
                        if(ChosenReport.report != null) {
                            fxmlLoader.setRoot("Admin_Kommentar");
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

    }
}
