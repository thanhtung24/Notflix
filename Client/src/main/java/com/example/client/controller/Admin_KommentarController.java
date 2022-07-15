package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.Admin_KommentarEndpoint;
import com.example.client.model.ChosenReport;
import com.example.client.model.Report;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.paint.Color;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.awt.*;
import javafx.scene.control.TextField;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data

public class Admin_KommentarController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    Admin_KommentarEndpoint admin_kommentarEndpoint;

    @Autowired
    ReportanfrageAdminController reportanfrageAdminController;

    @FXML
    private Label label_detaillerteAnfrage;

    @FXML
    private Label label_kommentar;

    @FXML
    private TextArea textArea_kommentar;

    @FXML
    private Button button_erledigt;

    @FXML
    private Button button_zurueck;

    @FXML
    private Label label_erledigt;

    @FXML
    private Label label_filmname;

    @FXML
    private Label label_regisseur;

    @FXML
    private Label label_fehlerIn;

    @FXML
    private TextField textField_filmname;

    @FXML
    private TextField textField_fehlerIn;

    @FXML
    private TextField textField_regisseur;


    public void switchToReportanfrageAdmin() throws IOException{
        this.fxmlLoader.setRoot("ReportanfrageAdmin");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        zeigeReport(ChosenReport.report);
        zeigeFilmnamen(ChosenReport.report);
        zeigeRegisseur(ChosenReport.report);
        zeigeFehlerIn(ChosenReport.report);
    }

    public void zeigeReport (Report report){
        this.textArea_kommentar.setText(report.getKommentar());
        this.textArea_kommentar.setEditable(false);
    }

    public void zeigeFilmnamen(Report report){
        this.textField_filmname.setText(report.getFilmname());
        this.textField_filmname.setEditable(false);
    }

    public void zeigeRegisseur(Report report){
        this.textField_regisseur.setText(report.getRegisseur());
        this.textField_regisseur.setEditable(false);
    }

    public void zeigeFehlerIn(Report report){
        this.textField_fehlerIn.setText(report.getFehler_in());
        this.textField_fehlerIn.setEditable(false);
    }


    public void erledigt () {

        this.button_erledigt.setOnMouseClicked(event -> {
            Response <Report> response =null;
            boolean erledigt=true;
            try {
                response= this.admin_kommentarEndpoint.bearbeiteReport(ChosenReport.report.getId()).execute();
                ChosenReport.report = response.body();
                if (response.isSuccessful()){
                    this.label_erledigt.setTextFill(Color.GREEN);
                    this.label_erledigt.setText("Report wurde erfolgreich bearbeitet.");
                }
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }
}
