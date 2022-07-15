package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.Hashing;
import com.example.client.endpoints.NutzerEndpoint;
import com.example.client.endpoints.PrivateEinstellungenEndpoint;
import com.example.client.model.Nutzer;
import com.example.client.model.PrivateEinstellungen;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class RegistrierungNutzerController  {

    @FXML
    private TextField text_vorname;

    @FXML
    private TextField text_nachname;

    @FXML
    private DatePicker datepicker_geburtsdatum;

    @FXML
    private TextField text_email;

    @FXML
    private PasswordField text_passwort;

    @FXML
    private PasswordField text_passwortBestaetigen;

    @FXML
    private ImageView imageView_profil;

    @FXML
    private Button button_absenden;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_bearbeiten;

    @FXML
    private Label label_nachricht;

    @FXML
    private Label label_warnung;

    private BufferedImage profilbild;


    @Autowired
    private NutzerEndpoint nutzerEndpoint;

    @Autowired
    private PrivateEinstellungenEndpoint privateEinstellungenEndpoint;

    @Autowired
    private FXMLLoader fxmlLoader;

    private int zaehlerGrossbuchstabe = 0;
    private int zaehlerKleinbuchstabe = 0;
    private int zaehlerZahlen = 0;
    private int zaehlerSonderzeichen = 0;

    public void zurueck() {
        try {
            fxmlLoader.setRoot("GlobaleStartseite");
        }
        catch (IOException e){
            e.printStackTrace();
        }
    }



    //gleiche Bedingungen wie beim Administrator müssen erfüllt werden
    public void absenden(ActionEvent actionEvent) {
        button_absenden.setOnMouseClicked(event -> {

            String vorname = text_vorname.getText();
            String nachname = text_nachname.getText();
            String geburtstag = datepicker_geburtsdatum.getEditor().getText();
            String email = text_email.getText();
            String passwort = text_passwort.getText();


            pruefeNachStarkenPasswort();

            if (text_passwort.getText().equals(text_passwortBestaetigen.getText()) && passwort.length() >= 6
                    && (!(pruefeEingabenachZahl(vorname) || pruefeEingabenachZahl(nachname))) && (zaehlerGrossbuchstabe >= 1
                    && zaehlerKleinbuchstabe >= 1 && zaehlerZahlen >= 1 && zaehlerSonderzeichen >= 1)
                    && pruefeNachRichtigerEmail(email)) {

                try{

                    String hashPasswort = "";

                    try {
                        hashPasswort = Hashing.toHexString(Hashing.getSHA(passwort));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }


                    Nutzer nutzer = new Nutzer(vorname, nachname, geburtstag, email, hashPasswort, getProfilbild());
                Response<Nutzer> response = nutzerEndpoint.nutzerRegistrierung(nutzer).execute();

                if(response.isSuccessful()){
                    nutzerStandardeinstellungenAnlegen(response.body());
                label_nachricht.setText("Registrierung erfolgreich");
                label_nachricht.setTextFill(Color.GREEN);
                label_warnung.setText("");
                }
                else{
                    label_nachricht.setText("Registrierung fehlgeschlagen. E-Mail existiert bereits");
                    label_nachricht.setTextFill(Color.RED);
                    label_warnung.setText("");
                }


                }
                catch(IOException e){
                    e.printStackTrace();
                }
            } else {

                label_warnung.setTextFill(Color.RED);
                if (!text_passwort.getText().equals(text_passwortBestaetigen.getText())) {
                    label_warnung.setText("Passwörter stimmen nicht überein");
                } else if (passwort.length() < 6) {
                    label_warnung.setText("Das Passwort ist zu kurz");
                } else if (zaehlerGrossbuchstabe < 1 || zaehlerKleinbuchstabe < 1 || zaehlerZahlen < 1 || zaehlerSonderzeichen < 1) {
                    label_warnung.setText("Das Passwort ist zu schwach");
                } else {
                    label_warnung.setText("");
                }

                label_nachricht.setText("Registrierung fehlgeschlagen");
                label_nachricht.setTextFill(Color.RED);

            }
        });
    }

    public void bearbeiten(ActionEvent actionEvent){

        button_bearbeiten.setOnMouseClicked(event-> {

            ImageView profilbild= imageView_profil;

            FileChooser fileChooser= new FileChooser();
            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Bilder", "*.png", "*.jpg");


            fileChooser.getExtensionFilters().add(filter);
            fileChooser.setTitle("Bilddatei auswählen");

            File ausgewaehlt= fileChooser.showOpenDialog(new Stage());

            try {
                if (ausgewaehlt != null) {
                    BufferedImage bild = ImageIO.read(ausgewaehlt);


                    profilbild.setImage(new Image(ausgewaehlt.toURI().toString()));
                    this.profilbild = bild;
                }
            } catch (IOException e){
                e.printStackTrace();
            }

        });

    }

    public byte[] getProfilbild(){
        ByteArrayOutputStream outputStream= new ByteArrayOutputStream();
        try{
            if (this.profilbild==null){
                return new byte[0];
            }
            ImageIO.write(this.profilbild, "png", outputStream);
        } catch(IOException e){
            e.printStackTrace();
        }
        return outputStream.toByteArray();
    }

    public boolean pruefeEingabenachZahl(String eingabe) {

        boolean eingabeBesitztZahl = true;

        for (int i = 0; i < eingabe.length(); i++) {

            if (Character.isDigit(eingabe.charAt(i)) == true) {
                eingabeBesitztZahl = true;
            } else {
                eingabeBesitztZahl = false;
            }

        }
        return eingabeBesitztZahl;
    }

    public void pruefeNachStarkenPasswort() {
        //1. Mind. 1 Großbuchstabe, 2. Mind. 1 Kleinbuchstabe, 3 Mind. 1 Zahl, 4. Mind. 1 Sonderzeichen

        String passwortPruefen = text_passwort.getText();

        for (int i = 0; i < passwortPruefen.length(); i++) {

            if (Character.isUpperCase(passwortPruefen.charAt(i))) {
                zaehlerGrossbuchstabe++;
            } else if (Character.isLowerCase(passwortPruefen.charAt(i))) {
                zaehlerKleinbuchstabe++;
            } else if (Character.isDigit(passwortPruefen.charAt(i))) {
                zaehlerZahlen++;
            } else if (!Character.isLetterOrDigit(passwortPruefen.charAt(i))) {
                zaehlerSonderzeichen++;
            }

        }
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

    private void nutzerStandardeinstellungenAnlegen(Nutzer nutzer) {
        Response<PrivateEinstellungen> response = null;
        try {
            response = this.privateEinstellungenEndpoint.nutzerStandardeinstellungenAnlegen(new PrivateEinstellungen(nutzer.getId())).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response.isSuccessful()) {
            System.out.println("Nutzer Standardeinstellungen wurden festgelegt");
        } else System.out.println("Fehler beim Festlegen von Nutzer Standardeinstellungen");
    }

}
