package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.DiskussionsGruppeEndpoint;
import com.example.client.endpoints.FreundschaftEndpoint;
import com.example.client.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.ScheduledAnnotationBeanPostProcessor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

@Configuration
@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@EnableScheduling
@EnableAsync
public class DiskussionsgruppenUebersichtController implements Initializable {
    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private DiskussionsGruppeEndpoint diskussionsGruppeEndpoint;

    @Autowired
    private ScheduledAnnotationBeanPostProcessor postProcessor;

    @Autowired
    private FreundschaftEndpoint freundschaftEndpoint;

    @FXML
    private Button button_gruppeErstellen;

    @FXML
    private TextField textfield_gruppenName;

    @FXML
    private TextField textfield_gruppenNameSuchen;

    @FXML
    private TableView<DiskussionsGruppe> tableview_diskussionsgruppe;

    @FXML
    private TableColumn<DiskussionsGruppe, String> tablecolumn_gruppenNamen;

    @FXML
    private Button button_suchen;

    @FXML
    private Button button_alleGruppen;

    @FXML
    private Label label_message;

    @FXML
    private ComboBox<String> combobox_privateEinstellungen;

    private List<DiskussionsGruppe> alleGruppen = new ArrayList<>();

    private List<DiskussionsGruppe> neueAlleGruppen = new ArrayList<>();

    private ObservableList<DiskussionsGruppe> diskussionsGruppenNamen;

    private int alteGroesse;

    private Long aktuellerNutzerId;

    private List<Nutzer> freundesListeVonAktuellemNutzer = new ArrayList<>();

    private boolean suchtGruppe;

    private boolean nutzerIstMitAdminBefreundet;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.aktuellerNutzerId = AktuellerNutzer.aktuellerNutzer.getId();

        //ToDo:moeglichkeiten zur privaten gruppen erstellung

        ArrayList<String> kategorien = new ArrayList<>();
        kategorien.add("Öffentlich");
        kategorien.add("Privat");

        ObservableList<String> liste = FXCollections.observableArrayList(kategorien);

        this.combobox_privateEinstellungen.setItems(liste);

        //ToDo: hier sollen alle Gruppen angezeigt werden die es schon gibt
        try {
            this.alleGruppen = this.diskussionsGruppeEndpoint.alleDiskussionsGruppe().execute().body();
            this.freundesListeVonAktuellemNutzer = this.freundschaftEndpoint.meineFreunde(this.aktuellerNutzerId).execute().body();
        } catch (IOException e) {
            System.out.println("Fehler alle Gruppen zu bekommen");
        }
        if(alleGruppen == null){
            System.out.println("Gruppen sind null");
        } else {
            System.out.println(alleGruppen.size());
        }
        this.alteGroesse = alleGruppen.size();

        this.diskussionsGruppenNamen = FXCollections.observableArrayList();

        //alleGruppen.stream().collect(Collectors.toCollection(() -> diskussionsGruppenNamen));

        gruppenAuswahl();

        this.tableview_diskussionsgruppe.setItems(this.diskussionsGruppenNamen);

        tablecolumn_gruppenNamen.setCellValueFactory(new PropertyValueFactory<>("gruppenName"));
        //tablecolumn_gruppenNamen.setMinWidth(199.0);
        tablecolumn_gruppenNamen.setEditable(false);
        //tablecolumn_gruppenNamen.setCellFactory(TextFieldTableCell.forTableColumn());

        diskussionsGruppenChatFenster();


    }

    public void alleGruppenAnzeigen(){
        diskussionsGruppenNamen.clear();
        this.diskussionsGruppenNamen = FXCollections.observableArrayList();
        alleGruppen.stream().collect(Collectors.toCollection(() -> diskussionsGruppenNamen));
        this.tableview_diskussionsgruppe.setItems(this.diskussionsGruppenNamen);
        tablecolumn_gruppenNamen.setCellValueFactory(new PropertyValueFactory<>("gruppenName"));
        tablecolumn_gruppenNamen.setEditable(false);
    }

    public void alleGruppen(ActionEvent actionEvent){
        button_alleGruppen.setOnMouseClicked(mouseEvent -> {
            alleGruppenAnzeigen();
            this.suchtGruppe = false;
        });
    }

    public void zurueck() throws IOException {
        postProcessor.postProcessBeforeDestruction(this, "scheduledTasks");
        fxmlLoader.setRoot("Nutzerstartseite");
    }

    public void gruppeErstellen(ActionEvent actionEvent){
        button_gruppeErstellen.setOnMouseClicked(mouseEvent -> {
            String privateEinstellungen = combobox_privateEinstellungen.getValue();
            boolean istPrivat;

            if(privateEinstellungen == null) {
                label_message.setTextFill(Color.RED);
                label_message.setText("Wähle einen Gruppenstatus");
                return;
            }

            if(privateEinstellungen.equals("Öffentlich"))
                istPrivat = false;
            else
                istPrivat = true;
            if(!this.textfield_gruppenName.getText().isEmpty()){
                try {
                    DiskussionsGruppe angelegteGruppe = diskussionsGruppeEndpoint.diskussionsGruppeErstellen(DiskussionsGruppe.builder()
                            .gruppenName(this.textfield_gruppenName.getText())
                            .gruppenErstellerID(this.aktuellerNutzerId)
                            .istPrivat(istPrivat)
                            .build())
                            .execute()
                            .body();
                    textfield_gruppenName.clear();
                    combobox_privateEinstellungen.getSelectionModel().clearSelection();

                    DiskussionsGruppenMitglied diskussionsGruppenMitglied = this.diskussionsGruppeEndpoint.gruppenMitgliedHinzufuegen(DiskussionsGruppenMitglied.builder()
                            .gruppenId(angelegteGruppe.getId())
                            .nutzerId(this.aktuellerNutzerId)
                            .build())
                            .execute()
                            .body();
                } catch (IOException e) {
                    System.out.println("Fehlgeschlagen");

                }
            } else {
                label_message.setTextFill(Color.RED);
                label_message.setText("Gib einen Gruppenname ein!");
            }
        });
    }

    public void diskussionsGruppenChatFenster() {
        this.tableview_diskussionsgruppe.setOnMousePressed(mouseEvent ->  {
            if(mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 2) {
                try {
                    if(tableview_diskussionsgruppe.getSelectionModel().getSelectedItem() != null){
                        AusgewaehlteGruppe.diskussionsGruppe = tableview_diskussionsgruppe.getSelectionModel().getSelectedItem();
                        AusgewaehlteGruppe.gruppenName = tableview_diskussionsgruppe.getSelectionModel().getSelectedItem().getGruppenName();
                        AusgewaehlteGruppe.gruppenErstellerId = tableview_diskussionsgruppe.getSelectionModel().getSelectedItem().getGruppenErstellerID();
                        AusgewaehlteGruppe.nutzerIstMitAdminBefreundet = this.nutzerIstMitAdminBefreundet;
                        if(AusgewaehlteGruppe.diskussionsGruppe != null) {
                            postProcessor.postProcessBeforeDestruction(this, "scheduledTasks");
                            fxmlLoader.setRoot("Gruppenchat");
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public List<DiskussionsGruppe> gruppenAuswahl(){
        diskussionsGruppenNamen.clear();
        List<DiskussionsGruppe> ausgewaehlteGruppen = new ArrayList<>();
        for(DiskussionsGruppe gruppe : alleGruppen){
            if(gruppe.isIstPrivat()){
                if(this.aktuellerNutzerId.equals(gruppe.getGruppenErstellerID())){
                    diskussionsGruppenNamen.add(gruppe);
                    ausgewaehlteGruppen.add(gruppe);
                } else {
                    for(Nutzer meineFreunde : this.freundesListeVonAktuellemNutzer){
                        if(meineFreunde.getId().equals(gruppe.getGruppenErstellerID())){
                            //ToDo: Nutzer und Admin sind befreundet -> Gruppe anzeigen
                            this.nutzerIstMitAdminBefreundet = true;
                            diskussionsGruppenNamen.add(gruppe);
                            ausgewaehlteGruppen.add(gruppe);
                        }
                    }
                }

            } else {
                diskussionsGruppenNamen.add(gruppe);
                ausgewaehlteGruppen.add(gruppe);
            }


        }
        return ausgewaehlteGruppen;
    }

    public void gruppenSuchen(ActionEvent actionEvent){
        button_suchen.setOnMouseClicked(mouseEvent -> {
            this.suchtGruppe = true;
            String nameZumSuchen = textfield_gruppenNameSuchen.getText();
            System.out.println(alleGruppen.toString());
            textfield_gruppenNameSuchen.clear();
            diskussionsGruppenNamen.clear();

            ArrayList<DiskussionsGruppe> gruppen = new ArrayList<>();

            //ToDo: Nur durch die Liste von Gruppen gehen die man als Nutzer auch sehen sollte

            for(DiskussionsGruppe gruppe: gruppenAuswahl()){
                if(gruppe.getGruppenName().toLowerCase().contains(nameZumSuchen.toLowerCase())){
                    gruppen.add(gruppe);
                }
            }

            this.tableview_diskussionsgruppe.setItems(FXCollections.observableList(gruppen));

            tablecolumn_gruppenNamen.setCellValueFactory(new PropertyValueFactory<>("gruppenName"));
            //tablecolumn_gruppenNamen.setMinWidth(199.0);
            tablecolumn_gruppenNamen.setEditable(false);
        });
    }



    @Scheduled(fixedRate = 1000)
    public void aktualisiereGruppen(){
        //ToDo: schauen ob sich etwas an der gesamten Gruppenanzahl verändert hat, wenn ja dann wird die List neu gesetzt sonst nicht
        //System.out.println("Im Schedule Uberischt Controller ");

        try {
            neueAlleGruppen = diskussionsGruppeEndpoint.alleDiskussionsGruppe().execute().body();
            this.alleGruppen = neueAlleGruppen;
        } catch (IOException e) {
            System.out.println("Fehler");
        }
        if(diskussionsGruppenNamen != null && !this.suchtGruppe){
            diskussionsGruppenNamen.clear();

            //neueAlleGruppen.stream().collect(Collectors.toCollection(() -> diskussionsGruppenNamen));

            gruppenAuswahl();

            this.tableview_diskussionsgruppe.setItems(this.diskussionsGruppenNamen);

            tablecolumn_gruppenNamen.setCellValueFactory(new PropertyValueFactory<>("gruppenName"));
            //tablecolumn_gruppenNamen.setMinWidth(199.0);
            tablecolumn_gruppenNamen.setEditable(false);
            //tablecolumn_gruppenNamen.setCellFactory(TextFieldTableCell.forTableColumn());

            diskussionsGruppenChatFenster();
        }

        /*
        if(aktuellegroesse > alteGroesse && diskussionsGruppenNamen != null){
            //ToDo: wenn anzahl der neueGruppen groeser ist als die der alten dann gibt es neue gruppen
            System.out.println("ES GIBT NEUE GRUPPEN");

            //ToDo: Alle Gruppen in die Tabelle tun
            diskussionsGruppenNamen.clear();

            //neueAlleGruppen.stream().collect(Collectors.toCollection(() -> diskussionsGruppenNamen));

            gruppenAuswahl();

            this.tableview_diskussionsgruppe.setItems(this.diskussionsGruppenNamen);

            tablecolumn_gruppenNamen.setCellValueFactory(new PropertyValueFactory<>("gruppenName"));
            //tablecolumn_gruppenNamen.setMinWidth(199.0);
            tablecolumn_gruppenNamen.setEditable(false);
            //tablecolumn_gruppenNamen.setCellFactory(TextFieldTableCell.forTableColumn());


            //ToDo: neuer groesster Wert
            this.alteGroesse = aktuellegroesse;

            diskussionsGruppenChatFenster();
        }

         */

    }

}
