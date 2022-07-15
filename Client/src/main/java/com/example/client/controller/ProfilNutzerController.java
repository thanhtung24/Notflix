package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.DiskussionsGruppeEndpoint;
import com.example.client.endpoints.FreundschaftEndpoint;
import com.example.client.endpoints.NutzerEndpoint;
import com.example.client.model.AktuellerNutzer;
import com.example.client.model.DiskussionsGruppe;
import com.example.client.model.Film;
import com.example.client.model.Nutzer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.List;
import java.util.ResourceBundle;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class ProfilNutzerController implements Initializable {


    @Autowired
    private FreundschaftEndpoint freundschaftEndpoint;

    @Autowired
    private DiskussionsGruppeEndpoint diskussionsGruppeEndpoint;

    @FXML
    private TableView<DiskussionsGruppe> tableView_meineGruppen;

    @FXML
    private TableColumn<DiskussionsGruppe, String> tablecolumn_gruppenName;


    @FXML
    private TextField text_vorname;

    @FXML
    private TextField text_nachname;

    @FXML
    private TextField text_geburtsdatum;

    @FXML
    private TextField text_email;

    @FXML
    private Button button_zurueck;

    @FXML
    private ImageView imageView_profil;

    @FXML
    private TableView <Film> tableView_watchlist;

    @FXML
    private TableColumn<Film, String> tablecolumn_filmname;

    @FXML
    private TableView<Nutzer> tableView_meineFreunde;

    @FXML
    private TableColumn<Nutzer, String> tablecolumn_vorname;

    @FXML
    private TableColumn<Nutzer, String> tablecolumn_nachname;

    @FXML
    private TableColumn<Nutzer, String> tablecolumn_geburtsdatum;

    @FXML
    private TableView<Film> tableView_bereitsGesehen;

    @FXML
    private TableColumn<Film, String> tableColumn_filmnameBereitsGesehen;


    @Autowired
    private NutzerEndpoint nutzerEndpoint;

    @Autowired
    private FXMLLoader fxmlLoader;

    private Long nutzerProfilID;

    public Button button_bearbeiten;

    public Button button_speichern;

    public Label label_nachricht;

    public Button button_profilbildBearbeiten;

    public Button button_bearbeitenBeenden;

    public BufferedImage neuesProfilbild;

    public String email;

    public void zurueck(){
        if(AktuellerNutzer.BEARBEITUNGAKTIVIERT==true){
            this.label_nachricht.setText("Bitte verlassen Sie zunächst den Bearbeitungsmodus");
            this.label_nachricht.setTextFill(Color.RED);
        }
        else {
            try {
                fxmlLoader.setRoot("Nutzerstartseite");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        Nutzer nutzer= AktuellerNutzer.aktuellerNutzer;
        System.out.println(nutzer.getId());
        this.nutzerProfilID = nutzer.getId();

        zeigeProfildaten(nutzer);

        eingabeDeaktivieren();

        zeigemeineFreunde();

        zeigeWatchList();

        zeigeBereitsGesehen();

        zeigemeineGruppen();

        if(AktuellerNutzer.BEARBEITUNGAKTIVIERT==true){
            zeigeBearbeiteteProfildaten(nutzer);
        }

    }

    public void zeigeProfildaten(Nutzer nutzer)  {

        this.text_vorname.setText(nutzer.getVorname());
        this.text_nachname.setText(nutzer.getNachname());
        this.text_geburtsdatum.setText(nutzer.getGeburtsdatum());
        this.text_email.setText(nutzer.getEmail());

        zeigeProfilbild();


    }

    public void zeigeProfilbild(){
        InputStream inputStream= new ByteArrayInputStream(AktuellerNutzer.aktuellerNutzer.getProfilbild());

        if(AktuellerNutzer.aktuellerNutzer.getProfilbild()==null || AktuellerNutzer.aktuellerNutzer.getProfilbild().length==0){
            return;
        }

        try {
            BufferedImage bild = ImageIO.read(inputStream);
            Image profilBild= SwingFXUtils.toFXImage(bild, null);
            this.imageView_profil.setImage(profilBild);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void eingabeDeaktivieren(){
        this.text_vorname.setEditable(false);
        this.text_nachname.setEditable(false);
        this.text_geburtsdatum.setEditable(false);
        this.text_email.setEditable(false);

    }


    public void zeigeWatchList(){
        Response<List<Film>> response= null;

       try{
           response= nutzerEndpoint.watchlistAbrufen(nutzerProfilID).execute();
        } catch (IOException e){
            e.printStackTrace();
        }

       if (response!=null && response.isSuccessful() && response.body()!=null ){
           ObservableList watchlist= FXCollections.observableArrayList(response.body());

           tableView_watchlist.setItems(watchlist);
           tablecolumn_filmname.setCellValueFactory(new PropertyValueFactory<>("name"));
           tablecolumn_filmname.setCellFactory(TextFieldTableCell.forTableColumn());
           tablecolumn_filmname.setEditable(false);
       }

    }


    public void zeigeBereitsGesehen(){
        Response<List<Film>> response = null;

        try{
            response= nutzerEndpoint.bereitsGesehenAbrufen(nutzerProfilID).execute();
        }catch (IOException e){
            e.printStackTrace();
        }

        if(response != null && response.isSuccessful() && response.body() != null){
            ObservableList bereitsGesehen = FXCollections.observableArrayList(response.body());

            tableView_bereitsGesehen.setItems(bereitsGesehen);
            tableColumn_filmnameBereitsGesehen.setCellValueFactory(new PropertyValueFactory<>("name"));
            tableColumn_filmnameBereitsGesehen.setCellFactory(TextFieldTableCell.forTableColumn());
            tableColumn_filmnameBereitsGesehen.setEditable(false);
        }
    }

    public void zeigemeineFreunde()  {
        try {
            List<Nutzer> freundesListe = freundschaftEndpoint.meineFreunde(this.nutzerProfilID).execute().body();

            if(freundesListe != null){
                ObservableList<Nutzer> freunde =FXCollections.observableArrayList(freundesListe);


                tableView_meineFreunde.setItems(freunde);

                tablecolumn_vorname.setCellValueFactory(new PropertyValueFactory<>("vorname"));
                tablecolumn_vorname.setCellFactory(TextFieldTableCell.forTableColumn());

                tablecolumn_nachname.setCellValueFactory(new PropertyValueFactory<>("nachname"));
                tablecolumn_nachname.setCellFactory(TextFieldTableCell.forTableColumn());

                tablecolumn_geburtsdatum.setCellValueFactory(new PropertyValueFactory<>("geburtsdatum"));
                tablecolumn_geburtsdatum.setCellFactory(TextFieldTableCell.forTableColumn());
            }


            /*
            System.out.println(freundesListe.get(0).getVorname());
            System.out.println(freundesListe.get(1).getVorname());

             */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zeigemeineGruppen()  {
        try {
            List<DiskussionsGruppe> gruppenListe = diskussionsGruppeEndpoint.meineGruppen(this.nutzerProfilID).execute().body();

            if(gruppenListe != null){
                ObservableList<DiskussionsGruppe> gruppen =FXCollections.observableArrayList(gruppenListe);


                tableView_meineGruppen.setItems(gruppen);

                tablecolumn_gruppenName.setCellValueFactory(new PropertyValueFactory<>("gruppenName"));
                tablecolumn_gruppenName.setCellFactory(TextFieldTableCell.forTableColumn());
            }


            /*
            System.out.println(freundesListe.get(0).getVorname());
            System.out.println(freundesListe.get(1).getVorname());

             */
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void zeigeBearbeiteteProfildaten(Nutzer nutzer){
        this.text_vorname.setText(nutzer.getVorname());
        this.text_nachname.setText(nutzer.getNachname());
        this.text_geburtsdatum.setText(nutzer.getGeburtsdatum());
        this.text_email.setText(nutzer.getEmail());

        zeigeNeuesProfilbild();
    }
   public void profildatenBearbeiten(){
        button_bearbeiten.setOnMouseClicked(event -> {
            AktuellerNutzer.BEARBEITUNGAKTIVIERT=true;
            this.button_bearbeiten.setDisable(true);
            this.button_speichern.setVisible(true);
            this.button_profilbildBearbeiten.setVisible(true);
            this.button_bearbeitenBeenden.setVisible(true);

            this.text_vorname.setEditable(true);
            this.text_nachname.setEditable(true);
            this.text_geburtsdatum.setEditable(true);
            this.text_email.setEditable(true);

            if(this.button_profilbildBearbeiten.isPressed()==false){
                AktuellerNutzer.PROFILBILDBEARBEITUNG=false;
            }
            else if(this.button_profilbildBearbeiten.isPressed()==true){
                AktuellerNutzer.PROFILBILDBEARBEITUNG=true;
            }

        });
    }


    public void neuesProfilbildAuswaehlen(ActionEvent actionEvent) {
         button_profilbildBearbeiten.setOnMouseClicked(event -> {
             AktuellerNutzer.PROFILBILDBEARBEITUNG=true;
             FileChooser fileChooser = new FileChooser();
             FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg");

             fileChooser.getExtensionFilters().add(filter);
             fileChooser.setTitle("Neues Profilbild auswählen");

             File neuesProfilbild = fileChooser.showOpenDialog(new Stage());

             try {
                 if (neuesProfilbild != null) {
                     BufferedImage bild = ImageIO.read(neuesProfilbild);

                     this.imageView_profil.setImage(new Image(neuesProfilbild.toURI().toString()));

                     this.neuesProfilbild = bild;


                 }
             } catch (IOException e) {
                 e.printStackTrace();
             }

         });
     }

    public byte [] neuesProfilbildSpeichern(){
         ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
         try {
             if (this.neuesProfilbild == null) {
                 return new byte[0];
             }
             else {
                 ImageIO.write(this.neuesProfilbild,"png", outputStream);
             }
         } catch(IOException e){
             e.printStackTrace();
         }
         return outputStream.toByteArray();
     }


     public void bearbeitungderDatenSpeichern(ActionEvent actionEvent) {

         button_speichern.setOnMouseClicked(event -> {

             if(this.pruefeEingabenachZahl(text_vorname.getText()) == false && this.pruefeEingabenachZahl(text_nachname.getText()) == false &&
             this.pruefeNachRichtigerEmail(text_email.getText()) && pruefeNachGeburtsdatum()==true) {
                 Response<Nutzer> response = null;
                 try {

                     if (AktuellerNutzer.PROFILBILDBEARBEITUNG == false) {
                         response = this.nutzerEndpoint.profilBearbeiten(AktuellerNutzer.aktuellerNutzer.getId(),
                                 this.text_vorname.getText(), this.text_nachname.getText(), this.text_geburtsdatum.getText(), this.text_email.getText()).execute();
                     } else if (AktuellerNutzer.PROFILBILDBEARBEITUNG == true) {
                         response = this.nutzerEndpoint.profildatenUndProfilbildBearbeiten(AktuellerNutzer.aktuellerNutzer.getId(), this.text_vorname.getText(), this.text_nachname.getText(),
                                 this.text_geburtsdatum.getText(), this.text_email.getText(),Base64.getEncoder().encodeToString(neuesProfilbildSpeichern())).execute();
                     }

                     if (response.isSuccessful()) {
                         AktuellerNutzer.aktuellerNutzer = response.body();
                     }
                 } catch (IOException e) {
                     e.printStackTrace();
                 }


                 if (response != null && response.isSuccessful()) {
                     this.label_nachricht.setText("Die Daten wurden erfolgreich geändert");
                     this.label_nachricht.setTextFill(Color.GREEN);

                 }
                 else {
                     this.label_nachricht.setText("Fehler bei der Bearbeitung. Kontrollieren Sie Ihre Eingabe");
                     this.label_nachricht.setTextFill(Color.RED);
                 }
             }

             else{
                 this.label_nachricht.setText("Bitte geben Sie nur gültige Eingaben ein");
                 this.label_nachricht.setTextFill(Color.RED);
             }


         });
     }


     public void zeigeNeuesProfilbild(){
        InputStream inputStream= new ByteArrayInputStream(neuesProfilbildSpeichern());

        if (neuesProfilbildSpeichern().length==0 && neuesProfilbildSpeichern()==null){
            return;
        }
        else{
            try{
                BufferedImage neu= ImageIO.read(inputStream);
                Image neuesProfilbild= SwingFXUtils.toFXImage(neu,null);
                this.imageView_profil.setImage(neuesProfilbild);

            } catch (IOException e){
                e.printStackTrace();
            }
        }
     }

     public void bearbeitenBeenden(){
        this.button_profilbildBearbeiten.setVisible(false);
        this.button_speichern.setVisible(false);
        this.button_bearbeitenBeenden.setVisible(false);

        this.text_vorname.setEditable(false);
        this.text_nachname.setEditable(false);
        this.text_email.setEditable(false);
        this.text_geburtsdatum.setEditable(false);

        this.label_nachricht.setText("");

        this.button_bearbeiten.setDisable(false);

        AktuellerNutzer.BEARBEITUNGAKTIVIERT=false;

     }

    public boolean pruefeNachGeburtsdatum(){
        String eingabe=text_geburtsdatum.getText();
        boolean gueltig;
        int zaehlerPunkt=0;

       for(int i=0; i<eingabe.length(); i++){

            char punkt= eingabe.charAt(i);
            if(punkt== '.'){
                zaehlerPunkt++;
            }
        }

        String erstePruefung= eingabe.substring(0, eingabe.indexOf("."));
        String zweitePruefung= eingabe.substring(eingabe.indexOf(".")+1, eingabe.length()-5);
        String drittePruefung= eingabe.substring(eingabe.length()-4, eingabe.length());

        int zaehlererstePruefung=0;
        for(int i=0 ; i< erstePruefung.length(); i++){
            if(Character.isDigit(erstePruefung.charAt(i))) {
                zaehlererstePruefung++;
            }
        }
        int zaehlerzweitePrufung=0;
        for(int i=0; i<zweitePruefung.length(); i++){
            if(Character.isDigit(zweitePruefung.charAt(i))) {
                zaehlerzweitePrufung++;
            }
        }
        int zaehlerdrittePruefung=0;
        for(int i=0; i<drittePruefung.length(); i++){
            if(Character.isDigit(drittePruefung.charAt(i))) {
                zaehlerdrittePruefung++;
            }
        }

        if(zaehlerPunkt==2 && eingabe.length()==10 && zaehlererstePruefung==2 && zaehlerzweitePrufung==2 && zaehlerdrittePruefung==4){
            gueltig=true;
        }
        else{
            gueltig=false;
        }

        return gueltig;
    }
    public boolean pruefeEingabenachZahl(String eingabe){
        boolean besitztZahl=false;
        for (int i=0; i< eingabe.length(); i++){

            if(Character.isDigit(eingabe.charAt(i))){
                besitztZahl=true;
            }
            else{
                besitztZahl=false;
            }
        }
        return besitztZahl;
    }


    public boolean pruefeNachRichtigerEmail(String eingabe){

        if(!eingabe.contains("@") || eingabe.length()<5){
            return false;
        }

        String erstePruefung = eingabe.substring(0, eingabe.indexOf("@"));
        String zweitePruefung = eingabe.substring(eingabe.indexOf("@") + 1, eingabe.length());

        if(!zweitePruefung.contains(".")) {
            return false;
        }

        String zweitePruefung_1 = zweitePruefung.substring(0, zweitePruefung.indexOf("."));
        String zweitePruefung_2 = zweitePruefung.substring(zweitePruefung.indexOf(".") + 1, zweitePruefung.length());


        return pruefeNachRichtigemString(erstePruefung, "-._") &&
                pruefeNachRichtigemString(zweitePruefung_1, "-")&&
                pruefeNachRichtigemString(zweitePruefung_2, "");


    }

    private boolean ersteUndLetzteZeichenKeineSonderzeichen(String pruefeString) {
        return Character.isLetterOrDigit(pruefeString.charAt(0))
                && Character.isLetterOrDigit(pruefeString.charAt(pruefeString.length()-1));
    }

    private boolean pruefeNachRichtigemString(String string, String erlaubteZeichen){
        if (string.isEmpty()|| !ersteUndLetzteZeichenKeineSonderzeichen(string)){
            return false;
        }

        for(int i=0; i<string.length()-1; i++) {
            char character = string.charAt(i);
            char folgeCharacter = string.charAt(i + 1);

            if (!pruefeNachErlaubtenZeichen(erlaubteZeichen,character, folgeCharacter)) {
                return false;
            }
        }


        return true;

    }

    private boolean pruefeNachErlaubtenZeichen(String erlaubtezeichen, char character, char folgeCharacter){
        if (erlaubtezeichen.contains(character+ "")){
            return Character.isLetterOrDigit(folgeCharacter);
        }
        return Character.isLetterOrDigit(character);
    }

}
