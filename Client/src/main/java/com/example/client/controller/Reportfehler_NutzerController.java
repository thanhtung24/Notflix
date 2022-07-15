package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.MailEndpoint;
import com.example.client.endpoints.SystemadministratorEndpoint;
import com.example.client.model.Film;
import com.example.client.model.Report;
import com.example.client.model.ReportMailSendTo;
import com.example.client.model.Systemadministrator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import com.example.client.endpoints.Reportfehler_NutzerEndpoint;
import java.io.IOException;
import java.util.*;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)


public class Reportfehler_NutzerController {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    private Reportfehler_NutzerEndpoint Reportfehler_NutzerEndpoint;

    @Autowired
    private SystemadministratorEndpoint systemadministratorEndpoint;

    @Autowired
    private MailEndpoint mailEndpoint;

    @FXML
    private Label label_report_fuer_fehler;

    @FXML
    private Label label_filmname;

    @FXML
    private Label label_fehler_in;

    @FXML
    private Label label_kommentar;

    @FXML
    private Label label_regisseur;

    @FXML
    private TextField textField_filmname;

    @FXML
    private TextField textField_regisseur;

    @FXML
    private ComboBox <String> combobox_fehler_in;

    @FXML
    private TextArea textArea_kommentar;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_absenden;

    @FXML
    private Label label_benachrichtigung;

    String [] emailsInArray;

    public void switchToNutzerstartseite() throws IOException{
        this.fxmlLoader.setRoot("Nutzerstartseite");
    }

    //Übertragung aller Admins 1. Versuch in String
    private ObservableList<String> emailsAdmins;

    List <String> mailsFromAdmins =new ArrayList();
    @FXML
    public void initialize(){
        combobox_fehler_in.getItems().addAll("Name","Kategorie","Filmlänge",
                "Erscheinungsdatum","Regisseur","Drehbuchautor","Cast", "Filmbanner");



//        for (String a : ReportMailSendTo.email){
//            System.out.println(a);
//        }
    }

    private boolean vollstaendig (){
        if (!this.textField_filmname.getText().isEmpty()
          && !this.textField_regisseur.getText().isEmpty()
            && this.combobox_fehler_in.getSelectionModel().getSelectedItem()!=null){
                return true;
        }
        else {
            return false;
        }
    }


    //Anlegen des Reports
    public void reportAufnehmen() {
        button_absenden.setOnMouseClicked(event->{
            if (vollstaendig()==false) {
                this.label_benachrichtigung.setTextFill(Color.RED);
                if (this.textField_filmname.getText().isEmpty()){
                    this.label_benachrichtigung.setText("Bitte Filmname eingeben!");
                }
                else if (this.textField_regisseur.getText().isEmpty()){
                    this.label_benachrichtigung.setText("Bitte Regisseur eingeben!");
                }

                else if (this.combobox_fehler_in.getSelectionModel().getSelectedItem()==null){
                    this.label_benachrichtigung.setText("Bitte Fehler in auswählen!");
                }
            }

            Report neuerReport;
            if (this.textArea_kommentar.getText().isEmpty()){
                neuerReport = new Report (this.textField_filmname.getText(),
                        this.textField_regisseur.getText(),
                        false,
                        this.combobox_fehler_in.getSelectionModel().getSelectedItem());
            } else {
                neuerReport = new Report (this.textField_filmname.getText(),
                        this.textField_regisseur.getText(),
                        this.combobox_fehler_in.getSelectionModel().getSelectedItem(),
                        false,
                        this.textArea_kommentar.getText());
            }

            try {
                Response <Report> response = this.Reportfehler_NutzerEndpoint.reportAufnehmen(neuerReport).execute();
                if (response.isSuccessful()){
                    sendReportMail();
                    this.label_benachrichtigung.setTextFill(Color.GREEN);
                    this.label_benachrichtigung.setText("Report wurde erfolgreich angelegt.");

                }
            } catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    public void sendReportMail() throws IOException {

        Response <List<Systemadministrator>> systemAdministratorEmailResponse=null;

        try {
            systemAdministratorEmailResponse = systemadministratorEndpoint.getAlleAdmins().execute();
            if (systemAdministratorEmailResponse.isSuccessful()){
                this.emailsAdmins=FXCollections.observableArrayList();

                for (Systemadministrator systemadministrator : systemAdministratorEmailResponse.body()){
                    this.emailsAdmins.add(systemadministrator.getEmail());
                }

                emailsInArray=new String [emailsAdmins.size()];

               for (int i=0; i<emailsInArray.length;i++){
                   emailsInArray[i]=emailsAdmins.get(i);
               }

                for (int i=0; i<emailsInArray.length;i++){
                    System.out.println(emailsInArray[i]);
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            Response<Boolean> response = mailEndpoint.reportMailSenden(emailsInArray).execute();

           if (response.isSuccessful()) {
               System.out.println(response.body());
           }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
