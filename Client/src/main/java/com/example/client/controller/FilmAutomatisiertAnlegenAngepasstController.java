package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmAutomatisiertAnlegenAngepasstEndpoint;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;


@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class FilmAutomatisiertAnlegenAngepasstController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private FilmAutomatisiertAnlegenAngepasstEndpoint filmAutomatisiertAnlegenAngepasstEndpoint;

    @FXML
    private TextField text_erscheinungsjahr;

    @FXML
    private Button button_filmeAnlegen;

    @FXML
    private Button button_zurueck;

    @FXML
    private Text antwort;

    @FXML
    private ComboBox<String> combobox_kategorie;

    public void zurueckZurFilmListe() throws IOException {
        fxmlLoader.setRoot("Filmliste");
    }

    public void filmeAnlegen(ActionEvent actionEvent){
        button_filmeAnlegen.setOnMouseClicked(event ->{

            String kategorie = "";
            if(combobox_kategorie != null){
                kategorie = combobox_kategorie.getValue();
            }
            if(kategorie == null){
                kategorie = "";
            }


            String erscheinungsjahr = text_erscheinungsjahr.getText();
            String[] gueltigeJahre = zeitraum(erscheinungsjahr);

            System.out.println("Button gedrückt");


            //auto. filme anhand jahr anlegen
            if(!erscheinungsjahr.isEmpty() && kategorie.isEmpty()){
                System.out.println("Erscheinungsjahr ist nicht leer");
                try {
                    System.out.println("Es wird versucht Filme aus dem Jahr: " + erscheinungsjahr + " anzulegen");
                    int code = filmAutomatisiertAnlegenAngepasstEndpoint.automatisierteFilmeAnhandZeitraum(gueltigeJahre).execute().code();
                    if(code == 200){
                        System.out.println("Filme wurden erfolgreich angelegt");
                    } else {
                        System.out.println("Die Filme konnten nicht angelegt werden, bitte versuche es erneut");
                    }

                }
                catch(SocketTimeoutException socketTimeoutException){
                    antwort.setText("Das Film anlegen dauert laenger als gewohnt...");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                antwort.setText("Filme wurden erfolgreich angelegt");
            }



            // auto Filme anhand Kategorie anlegen
           else if(!kategorie.isEmpty() && erscheinungsjahr.isEmpty()){
                System.out.println("Kategorie ist nicht leer");
                try {
                    System.out.println("Es wird versucht Filme der Kategorie: " + kategorie + " anzulegen");
                    int code = filmAutomatisiertAnlegenAngepasstEndpoint.automatisierteFilmeAnhandKategorie(kategorie).execute().code();
                    if(code == 200){
                        System.out.println("Filme wurden erfolgreich angelegt");
                    } else {
                        System.out.println("Es gab einen Fehler filme anzulegen");
                    }
                }
                catch(SocketTimeoutException socketTimeoutException){
                    antwort.setText("Das Film anlegen dauert laenger als gewohnt...");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                antwort.setText("Das Film anlegen dauert laenger als gewohnt...");
            }


            //auto filme anlegen anhand kategorie und jahr

            else if(!kategorie.isEmpty() && !erscheinungsjahr.isEmpty() && gueltigeJahre.length > 0){
                //String kategorieUndJahr = kategorie + " " + erscheinungsjahr;

                String[] kategorieUndJahr = {gueltigeJahre[0], gueltigeJahre[1], kategorie};
                System.out.println("Kategorie UND Film sind nicht leer");
                System.out.println(Arrays.toString(kategorieUndJahr));
                try {
                    System.out.println("Es wird versucht Filme der Kategorie: " + kategorie + " anzulegen");
                    int code = filmAutomatisiertAnlegenAngepasstEndpoint.automatisierteFilmeAnhandKategorieUndJahr(kategorieUndJahr).execute().code();
                    if(code == 200){
                        System.out.println("Filme wurden erfolgreich angelegt");
                    } else {
                        System.out.println("Es gab einen Fehler filme anhand Jahr und Kategorie anzulegen");
                    }
                }
                catch(SocketTimeoutException socketTimeoutException){
                    antwort.setText("Das Film anlegen dauert laenger als gewohnt...");
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
                antwort.setText("Das Film anlegen dauert laenger als gewohnt...");
            }

        });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ArrayList<String> kategorien = new ArrayList<>();
        kategorien.add("action");
        kategorien.add("drama");
        kategorien.add("comedy");
        kategorien.add("crime");
        kategorien.add("fantasy");
        kategorien.add("horror");
        kategorien.add("thriller");


        ObservableList<String> liste = FXCollections.observableArrayList(kategorien);

        combobox_kategorie.setItems(liste);
    }

    public String[] zeitraum(String zeitraum){
        if(zeitraum.length() == 9 && zeitraum.contains("-")){
            String[] jahre = zeitraum.split("-", 2);
            int jahr1 = Integer.parseInt(jahre[0]);
            int jahr2 = Integer.parseInt(jahre[1]);
            //Prüfen, ob angegebene Jahre zwischen 2000 und 2022 liegen
            if(jahr1 >= 2000 && jahr1 <= 2022 && jahr2 >= 2000 && jahr2 <= 2022){
                System.out.println("Zeitraum ist gueltig.");
                if(jahr1 < jahr2){
                    System.out.println("Gueltige Jahre.");
                    return jahre;
                }
            }
            return new String[]{};
        }
        else{
            return new String[]{};
        }
    }
}
