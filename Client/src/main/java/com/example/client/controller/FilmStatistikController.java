package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.BereitsGesehenEndpoint;
import com.example.client.endpoints.FilmStatistikEndpoint;
import com.example.client.endpoints.FilmbewertungEndpoint;
import com.example.client.model.AktuelleBewertung;
import com.example.client.model.ChosenFilm;
import com.example.client.model.Film;
import com.example.client.model.Filmbewertung;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.WritableImage;
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
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Data

public class FilmStatistikController implements Initializable {

    @FXML
    private TextField text_filmname;

    @FXML
    private TextField text_durchschnittBewertung;

    @FXML
    private TextField text_anzahlBewertung;

    @FXML
    private TextField text_anzahlGesehen;

    @FXML
    private Button button_herunterladen;

    @FXML
    private Button button_zuruecksetzen;

    @FXML
    private Button button_zurueck;

    @FXML
    private BarChart barchart_statistik;

    @FXML
    private NumberAxis numberAxis_geseheneFilme;

    private CategoryAxis xAchse;

    @FXML
    private Label label_nachricht;

    @Autowired
    private FXMLLoader fxmlLoader;

    @FXML
    private Label label_ueberschrift;

    @FXML
    private Label label_filmname;

    @FXML
    private Label label_durchschnittBewertung;

    @FXML
    private Label label_bewertungAnzahl;

    @FXML
    private Label label_anzahlGesehen;


    @Autowired
    private FilmStatistikEndpoint filmStatistikEndpoint;


    private int anzahleinStern=0;
    private int anzahlzweiSterne=0;
    private int anzahldreiSterne=0;
    private int anzahlvierSterne=0;
    private int anzahlfuenfSterne=0;

    private int anzahlBewertung= 0;

    private int anzahlGesehen=0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
     Film film = ChosenFilm.film;
     this.text_filmname.setText(film.getName());
     this.text_filmname.setEditable(false);
     this.text_anzahlBewertung.setEditable(false);
     this.text_anzahlGesehen.setEditable(false);
     this.text_durchschnittBewertung.setEditable(false);

     anzahlBerechnen();
     this.text_anzahlGesehen.setText(anzahlGesehen + "");
     this.text_anzahlBewertung.setText(this.anzahlBewertung + "");

        XYChart.Series<String, Integer> saeule=  new XYChart.Series<>();


            saeule.getData().add(new XYChart.Data<>("Gesehen", this.anzahlGesehen));
            saeule.getData().add(new XYChart.Data<>("Bewertungen", this.anzahlBewertung));
            saeule.getData().add(new XYChart.Data<>("1 Stern", this.anzahleinStern));
            saeule.getData().add(new XYChart.Data<>("2 Sterne", this.anzahlzweiSterne));
            saeule.getData().add(new XYChart.Data<>("3 Sterne", this.anzahldreiSterne));
            saeule.getData().add(new XYChart.Data<>("4 Sterne", this.anzahlvierSterne));
            saeule.getData().add(new XYChart.Data<>("5 Sterne", this.anzahlfuenfSterne));


       this.barchart_statistik.getData().addAll(saeule);


       durchschnittSterne();

       saeule.getChart().setLegendVisible(false);


    }


    public void anzahlBerechnen(){
        try{

           HashMap<String, Integer> statistikInformation=this.filmStatistikEndpoint.getAnzahl(ChosenFilm.film.getId()).execute().body();

           this.anzahlGesehen=statistikInformation.get("Anzahl Gesehen");
           this.anzahlBewertung= statistikInformation.get("Anzahl Bewertung");
           this.anzahleinStern= statistikInformation.get("Anzahl 1 Stern");
           this.anzahlzweiSterne=statistikInformation.get("Anzahl 2 Sterne");
           this.anzahldreiSterne=statistikInformation.get("Anzahl 3 Sterne");
           this.anzahlvierSterne=statistikInformation.get("Anzahl 4 Sterne");
           this.anzahlfuenfSterne=statistikInformation.get("Anzahl 5 Sterne");

        }catch (IOException e){
            e.printStackTrace();
        }

    }


    public void zurueck(){
        try {
            fxmlLoader.setRoot("Filmuebersicht");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void herunterladen(){

        this.button_herunterladen.setOnMouseClicked(event -> {
            Group alleInformationen= new Group();

            alleInformationen.getChildren().add(this.barchart_statistik);
            alleInformationen.getChildren().add(this.text_filmname);
            alleInformationen.getChildren().add(this.text_anzahlBewertung);
            alleInformationen.getChildren().add(this.text_anzahlGesehen);
            alleInformationen.getChildren().add(this.text_durchschnittBewertung);
            alleInformationen.getChildren().add(this.label_ueberschrift);
            alleInformationen.getChildren().add(this.label_filmname);
            alleInformationen.getChildren().add(this.label_durchschnittBewertung);
            alleInformationen.getChildren().add(this.label_anzahlGesehen);
            alleInformationen.getChildren().add(this.label_bewertungAnzahl);

            Scene aktuelleSzene= new Scene(alleInformationen, 900, 650);

            WritableImage bild= aktuelleSzene.snapshot(null);

            FileChooser.ExtensionFilter filter = new FileChooser.ExtensionFilter("Bilder", ".png", ".jpg");
            FileChooser fileChooser= new FileChooser();
            fileChooser.setTitle("Statistik speichern");
            fileChooser.getExtensionFilters().addAll(filter);

            File statistikAlsBildSpeichern= fileChooser.showSaveDialog(new Stage());

            BufferedImage zuSpeicherndeStatistik= SwingFXUtils.fromFXImage(bild, null);

                try {
                    ImageIO.write(zuSpeicherndeStatistik, "png", statistikAlsBildSpeichern);
                } catch (IOException e) {
                    e.printStackTrace();
                }

        });

        datenderSzeneAnzeigen();

    }

    public void datenderSzeneAnzeigen(){

        try {
            fxmlLoader.setRoot("FilmStatistik");
        } catch(IOException e){
            e.printStackTrace();
        }
    }

    public void zuruecksetzen(){
     this.button_zuruecksetzen.setOnMouseClicked(event-> {

         this.anzahlGesehen=0;
         this.anzahlBewertung =0;


         this.text_anzahlBewertung.setText(anzahlBewertung + "");
         this.text_durchschnittBewertung.setText(0.0 + " Sterne");
         this.text_anzahlGesehen.setText(anzahlGesehen + "");

         this.label_nachricht.setText("Zurücksetzen erfolgreich");
         this.label_nachricht.setTextFill(Color.GREEN);

         this.text_anzahlBewertung.setEditable(false);
         this.text_filmname.setEditable(false);
         this.text_durchschnittBewertung.setEditable(false);
         this.text_anzahlGesehen.setEditable(false);

        this.barchart_statistik.getData().clear();


        try {
             this.filmStatistikEndpoint.zuruecksetzen(ChosenFilm.film.getId()).execute();
         } catch (IOException e) {
             e.printStackTrace();
         }


     });
    }

    public void  durchschnittSterne(){

        String ergebnis="";

        try {
            ergebnis = this.filmStatistikEndpoint.getDurchschnitt().execute().body() + " Sterne";
        }catch (IOException e){
            e.printStackTrace();
        }

        text_durchschnittBewertung.setText(ergebnis);
    }


}
