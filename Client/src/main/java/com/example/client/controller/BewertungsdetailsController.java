package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.NutzerEndpoint;
import com.example.client.model.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)

public class BewertungsdetailsController implements Initializable {

    @Autowired
    private FXMLLoader fxmlLoader;

    @Autowired
    private NutzerEndpoint nutzerEndpoint;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_profilOeffnen;

    @FXML
    private ImageView imageView_profilbild;

    @FXML
    private Label label_bewertungZuFilm;

    @FXML
    private Label label_filmname;

    @FXML
    private TextArea textarea_kommentar;

    private Long id;

    private Nutzer aktuellerNutzer;





    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        bewertungsdetailsAnzeigen(AktuelleBewertung.aktuelleBewertung);
        this.id = AktuelleBewertung.aktuelleBewertung.getNutzerId();
        System.out.println("Die Angezeigte Bewertung ist vom User mit der ID: " + this.id);

        getaktuellerNutzer(this.id);

        if(aktuellerNutzer != null)
            ChosenNutzer.nutzer = aktuellerNutzer;
        else
            System.out.println("Fehler");

        zeigeProfilbild();
    }

    public void getaktuellerNutzer(Long id){
        try {
            this.aktuellerNutzer = nutzerEndpoint.aktuellerNutzerAnhandId(id).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void bewertungsdetailsAnzeigen(Filmbewertung filmbewertung){
        this.label_bewertungZuFilm.setText(filmbewertung.getSterne());
        this.textarea_kommentar.setText(filmbewertung.getKommentar());
        this.textarea_kommentar.setEditable(false);
        this.label_filmname.setText("Bewertung zu " + ChosenFilm.film.getName());
    }

    public void zeigeProfilbild(){
        InputStream inputStream= new ByteArrayInputStream(ChosenNutzer.nutzer.getProfilbild());

        if(ChosenNutzer.nutzer.getProfilbild()==null || ChosenNutzer.nutzer.getProfilbild().length==0){
            return;
        }

        try {
            BufferedImage bild = ImageIO.read(inputStream);
            Image profilBild= SwingFXUtils.toFXImage(bild, null);
            this.imageView_profilbild.setImage(profilBild);
        }catch (IOException e){
            e.printStackTrace();
        }

    }

    public void zurueckZurFilmbewertung() throws IOException {
        fxmlLoader.setRoot("Filmbewertung");
    }

    public void profilOeffnen() throws IOException {
        fxmlLoader.setRoot("ProfilAndererNutzer");
    }
}
