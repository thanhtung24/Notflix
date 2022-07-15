package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmuebersichtEndpoint;
import com.example.client.endpoints.NutzerEndpoint;
import com.example.client.endpoints.NutzerverhaltenStatistikEndpoint;
import com.example.client.endpoints.PersonAnlegenEndpoint;
import com.example.client.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.paint.Color;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Component
@Data
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class NutzerverhaltenStatistikController implements Initializable {
    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    private FilmuebersichtEndpoint filmuebersichtEndpoint;

    @Autowired
    private NutzerverhaltenStatistikEndpoint nutzerverhaltenStatistikEndpoint;

    @Autowired
    NutzerEndpoint nutzerEndpoint;

    private Long nutzerProfilID;

    @FXML
    private Button button_zurueck;

    @FXML
    private Button button_laden;

    @FXML
    private Button button_reset;

    @FXML
    private Label label_titel;

    @FXML
    private Label label_von;

    @FXML
    private Label label_bis;

    @FXML
    private Label label_gesamtzeitGeschauteFilme;

    @FXML
    private Label label_benachrichtigung;

    @FXML
    private TableView<Film> tableview_lieblingsfilme;

    @FXML
    private TableColumn<Film, String> column_lieblingsfilmnamen;

    @FXML
    private DatePicker datePicker_von;

    @FXML
    private DatePicker datePicker_bis;

    @FXML
    private TextField textfield_gesamtzeitGeschauteFilme;

    //Barcharts
    @FXML
    private BarChart barChart_lieblingsschauspieler;

    @FXML
    private BarChart barChart_lieblingskategorie;

    //X-Achsen
    @FXML
    private CategoryAxis categoryAxis_xAchseLieblingsschauspieler;

    @FXML
    private CategoryAxis categoryAxis_xAchseLieblingskategorie;

    //Y-Achsen
    @FXML
    private NumberAxis numberAxis_yAchseLieblingsschauspieler;

    @FXML
    private NumberAxis numberAxis_yAchseLieblingskategorie;

    //Anzahl für Kategorien (Barcharts)
    private int anzahlAction = 0;
    private int anzahlDrama = 0;
    private int anzahlComedy = 0;
    private int anzahlCrime = 0;
    private int anzahlFantasy = 0;
    private int anzahlHorror = 0;
    private int anzahlThriller = 0;

    private List<Person> geschauteSchauspieler;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Nutzer nutzer = AktuellerNutzer.aktuellerNutzer;
        System.out.println(nutzer.getId());
        this.nutzerProfilID = nutzer.getId();

        this.datePicker_von.getEditor().setDisable(true);
        this.datePicker_bis.getEditor().setDisable(true);
        this.textfield_gesamtzeitGeschauteFilme.setEditable(false);
    }

    //Navigation
    public void betaetigeZurueck() throws IOException {
        this.fxmlLoader.setRoot("Nutzerstartseite");
    }

    public void nutzerVerhaltenLaden(ActionEvent actionEvent) {
        button_laden.setOnMouseClicked(event -> {
            // bereinige Textfeld, Tabelle und Barcharts beim Laden
            reset();
            //keine o. unvollstaendige Daten
            if (datePicker_von.getValue() == null || datePicker_bis.getValue() == null) {
                this.label_benachrichtigung.setTextFill(Color.RED);
                this.label_benachrichtigung.setText("Zeitraumeingabe nicht vollständig");
                return;
            }
            //wenn Startdatum größer als Enddatum
            else if(datePicker_von.getValue().isAfter(datePicker_bis.getValue())){
                this.label_benachrichtigung.setTextFill(Color.RED);
                this.label_benachrichtigung.setText("Falsche Zeitraumeingabe");
                return;
            }else {
                Date startdatum = convertLocalToDate(datePicker_von.getValue());
                Date enddatum = convertLocalToDate(datePicker_bis.getValue());
                this.geschauteSchauspieler = getGeschauteSchauspieler(AktuellerNutzer.aktuellerNutzer.getId(), startdatum, enddatum);
                if (geschauteSchauspieler != null) {
                    ladeLieblingsschauspielerBarChart();
                    getGeschauteKategorie(startdatum, enddatum);
                    ladeLieblingsKategorie();
                    getLieblingsfilme(AktuellerNutzer.aktuellerNutzer.getId(), startdatum, enddatum);
                    getGesamtzeitGeschauteFilme(AktuellerNutzer.aktuellerNutzer.getId(), startdatum, enddatum);
                }
            }
        });
    }
    // Lieblingsschauspieler
    private List<Person> getGeschauteSchauspieler(Long nutzerId, Date startDatum, Date endDatum) {
        Response<List<Person>> response = null;
        try {
            response = this.nutzerverhaltenStatistikEndpoint.getGeschauteSchauspieler(nutzerId, startDatum, endDatum).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.body();
    }

    //in Bearbeitung
    private HashMap<String, Integer> zaehleSchauspieler() {
        HashMap<String, Integer> anzahlSchauspieler = new HashMap<String, Integer>();
        for (Person p : geschauteSchauspieler ) {
            String key = p.getVorname() +" "+ p.getNachname();
            if(anzahlSchauspieler.containsKey(key)){
                anzahlSchauspieler.replace(key, anzahlSchauspieler.get(key)+1);
            }else{
                anzahlSchauspieler.put(key, 1);
            }
        }
        return anzahlSchauspieler;
    }

    private List<XYChart.Data> getSchauspielerBarChart() {
        List<XYChart.Data> result = new ArrayList<>();
        HashMap<String, Integer> schauspielerDaten = zaehleSchauspieler();
        schauspielerDaten = sortByValue(schauspielerDaten);
        int counter = 0;
        for (String personNamen : schauspielerDaten.keySet()) {
            if (counter < 5) { // zeige nur die Top 5 Schauspieler
                result.add(new XYChart.Data<>(personNamen, schauspielerDaten.get(personNamen)));
                counter++;
            }else{
                break;
            }
        }
        return result;
    }

    private void ladeLieblingsschauspielerBarChart(){
            XYChart.Series<String, Integer> saeuleSchauspieler = new XYChart.Series<>();
            List<XYChart.Data> schauspielerDaten = getSchauspielerBarChart();
            for (XYChart.Data saeule : schauspielerDaten) {
                saeuleSchauspieler.getData().add(new XYChart.Data(saeule.getXValue(),saeule.getYValue()));
            }
            this.barChart_lieblingsschauspieler.getData().addAll(saeuleSchauspieler);
            saeuleSchauspieler.getChart().setLegendVisible(false);
            this.barChart_lieblingsschauspieler.setAnimated(false);
    }

    //Lieblingskategorie
    public void getGeschauteKategorie(Date startDatum, Date endDatum) {
        try {
            HashMap<String, Integer> statistikInformation = this.nutzerverhaltenStatistikEndpoint.getAnzahlKategorie(AktuellerNutzer.aktuellerNutzer.getId(), startDatum, endDatum).execute().body();
            this.anzahlAction = statistikInformation.get("Anzahl Action");
            this.anzahlComedy = statistikInformation.get("Anzahl Comedy");
            this.anzahlCrime = statistikInformation.get("Anzahl Crime");
            this.anzahlDrama = statistikInformation.get("Anzahl Drama");
            this.anzahlFantasy = statistikInformation.get("Anzahl Fantasy");
            this.anzahlHorror = statistikInformation.get("Anzahl Horror");
            this.anzahlThriller = statistikInformation.get("Anzahl Thriller");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void ladeLieblingsKategorie(){
        XYChart.Series<String, Integer> saeuleKategorie = new XYChart.Series<>();
        saeuleKategorie.getData().add(new XYChart.Data<>("Action", this.anzahlAction));
        saeuleKategorie.getData().add(new XYChart.Data<>("Drama", this.anzahlDrama));
        saeuleKategorie.getData().add(new XYChart.Data<>("Comedy", this.anzahlComedy));
        saeuleKategorie.getData().add(new XYChart.Data<>("Crime", this.anzahlCrime));
        saeuleKategorie.getData().add(new XYChart.Data<>("Fantasy", this.anzahlFantasy));
        saeuleKategorie.getData().add(new XYChart.Data<>("Horror", this.anzahlHorror));
        saeuleKategorie.getData().add(new XYChart.Data<>("Thriller", this.anzahlThriller));

        barChart_lieblingskategorie.getData().add(saeuleKategorie);
        saeuleKategorie.getChart().setLegendVisible(false);
        this.barChart_lieblingskategorie.setAnimated(false);
    }

    //Lieblingsfilme
    private List<Film> getLieblingsfilme(Long nutzerId, Date startDatum, Date endDatum) {
        Response<List<Film>> response = null;
        try {
            response = nutzerverhaltenStatistikEndpoint.getLieblingsfilme(nutzerId, startDatum, endDatum).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (response != null && response.isSuccessful() && response.body() != null) {
            ObservableList lieblingsfilmeliste = FXCollections.observableArrayList(response.body());
            tableview_lieblingsfilme.setItems(lieblingsfilmeliste);
            column_lieblingsfilmnamen.setCellValueFactory(new PropertyValueFactory<>("name"));
            column_lieblingsfilmnamen.setCellFactory(TextFieldTableCell.forTableColumn());
            column_lieblingsfilmnamen.setEditable(false);
        }
        return response.body();
    }

    //Gesamtzeit geschaute Filme
    private void getGesamtzeitGeschauteFilme(Long nutzerId, Date startDatum, Date endDatum){
        Response<Integer> response = null;
        try {
            response = nutzerverhaltenStatistikEndpoint.getGesamtzeitGeschauteFilme(nutzerId, startDatum, endDatum).execute();
            if(response.isSuccessful() && response.body() != null){
                int t = response.body();
                int stunden = t / 60; //Formatierung von stackoverflow
                int minuten = t % 60;
                System.out.printf("%d:%02d", stunden, minuten);
                String ergebnis = String.valueOf(stunden) + " Stunden " + String.valueOf(minuten) + " Minuten";
                textfield_gesamtzeitGeschauteFilme.setText(ergebnis);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void reset() {
            this.textfield_gesamtzeitGeschauteFilme.setText(null);
            this.label_benachrichtigung.setText(null);
            this.tableview_lieblingsfilme.getItems().clear();
            this.barChart_lieblingsschauspieler.getData().clear();
            this.barChart_lieblingskategorie.getData().clear();
    }

    //Umwandlung LocalDate zu Date (stackoverflow)
    private Date convertLocalToDate(LocalDate date) {
        Date datum = java.sql.Date.valueOf(date);
        return datum;
    }

    // Hashmap sortieren von groß nach klein (Hilfestellung: siehe Link)
    // https://www.geeksforgeeks.org/sorting-a-hashmap-according-to-values/
    public static HashMap<String, Integer> sortByValue(HashMap<String, Integer> hm)
    {
        // Erstelle eine Liste von Elementen von Hashmap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());
        // Sortiere Liste
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue())*-1;
            }
        });
        // Füge die Daten von sortiere Liste in die Hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

}