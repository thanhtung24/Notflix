package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.Hashing;
import com.example.client.endpoints.SystemadministratorEndpoint;
import com.example.client.model.Systemadministrator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;


@Component


public class RegistrierungAdminController {

    @FXML
    private TextField text_vorname;

    @FXML
    private TextField text_nachname;

    @FXML
    private TextField text_email;

    @FXML
    private PasswordField text_passwort;

    @FXML
    private PasswordField text_passwortBestaetigung;

    @FXML
    private Button button_absenden;

    @FXML
    private Button button_zurueck;

    @FXML
    private Label text_warningLabel;

    @FXML
    private Label text_messageLabel;

    @Autowired
    private FXMLLoader FXMLLoader;

    @Autowired
    private SystemadministratorEndpoint systemadministratorEndpoint;

    private int zaehlerGrossbuchstabe = 0;
    private int zaehlerKleinbuchstabe = 0;
    private int zaehlerZahlen = 0;
    private int zaehlerSonderzeichen = 0;

    public void zurueckZurStartseite() throws IOException {
        FXMLLoader.setRoot("GlobaleStartseite");
    }

    public void absenden(ActionEvent actionEvent) {
        button_absenden.setOnMouseClicked(event -> {
            String vorname = text_vorname.getText();
            String nachname = text_nachname.getText();
            String email = text_email.getText();
            String passwort = text_passwort.getText();
            String passwortBestaetigen = text_passwortBestaetigung.getText();


            pruefeNachStarkenPasswort();


            if (text_passwort.getText().equals(text_passwortBestaetigung.getText()) && passwort.length() >= 6
                    && (!(pruefeEingabenachZahl(vorname) || pruefeEingabenachZahl(nachname))) && (zaehlerGrossbuchstabe >= 1
                    && zaehlerKleinbuchstabe >= 1 && zaehlerZahlen >= 1 && zaehlerSonderzeichen >= 1)
                    && pruefeNachRichtigerEmail(email)) {

                try {


                    String hashPasswort = "";

                    try {
                        hashPasswort = Hashing.toHexString(Hashing.getSHA(passwort));
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    Systemadministrator systemadministrator = new Systemadministrator(vorname, nachname, email, hashPasswort);
                    Response<Systemadministrator> response = systemadministratorEndpoint.adminRegistrierung(systemadministrator).execute();
                    if(response.isSuccessful()) {
                        text_messageLabel.setText("Registrierung erfolgreich");
                        text_messageLabel.setTextFill(Color.GREEN);
                        text_warningLabel.setText("");
                    } else {
                        text_messageLabel.setText("E-Mail existiert bereits");
                        text_messageLabel.setTextFill(Color.RED);
                        text_warningLabel.setText("");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } else {

                if (!text_passwort.getText().equals(text_passwortBestaetigung.getText())) {
                    text_warningLabel.setText("Passwörter stimmen nicht überein");
                } else if (passwort.length() < 6) {
                    text_warningLabel.setText("Das Passwort ist zu kurz");
                } else if (zaehlerGrossbuchstabe < 1 || zaehlerKleinbuchstabe < 1 || zaehlerZahlen < 1 || zaehlerSonderzeichen < 1) {
                    text_warningLabel.setText("Das Passwort ist zu schwach");
                } else {
                    text_warningLabel.setText("");
                }

                text_messageLabel.setText("Registrierung fehlgeschlagen");
                text_messageLabel.setTextFill(Color.RED);

            }
            System.out.println("Button gedrückt");

        });


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

}
