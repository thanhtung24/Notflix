package com.example.client.controller;

import com.example.client.FXMLLoader;
import com.example.client.endpoints.FilmVorschlagenEndpoint;
import com.example.client.model.AktuellerNutzer;
import com.example.client.model.ChosenFilm;
import com.example.client.model.Film;
import com.example.client.model.FilmVorschlag;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import lombok.Data;
import org.controlsfx.dialog.ProgressDialog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.*;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Component
@Data
public class FilmVorschlagenController implements Initializable {

    @Autowired
    FXMLLoader fxmlLoader;

    @Autowired
    FilmVorschlagenEndpoint filmVorschlagenEndpoint;

    @FXML
    AnchorPane anchorPane;

    @FXML
    Button button;

    @FXML
    Label label_titel;

    private static double FILMBANNER_WIDTH = 121;
    private static double FILMBANNER_HEIGHT = 155;
    private static double FILMBANNER_LAYOUT_X = 0;
    private static double FILMBANNER_LAYOUT_Y = 0;
    private static double FILMBANNER_SPACE_X = 200;

    private static double BUTTON_FILM_OEFFNEN_WIDTH = 83;
    private static double BUTTON_FILM_OEFFNEN_HEIGHT = 27;
    private static double BUTTON_FILM_OEFFNEN_LAYOUT_X = FILMBANNER_LAYOUT_X;
    private static double BUTTON_FILM_OEFFNEN_LAYOUT_Y = FILMBANNER_HEIGHT;
    private static double BUTTON_FILM_OEFFNEN_SPACE_Y = FILMBANNER_HEIGHT + FILMBANNER_SPACE_X;

    private static double LABEL_WIDTH = FILMBANNER_WIDTH;
    private static double LABEL_HEIGHT = FILMBANNER_SPACE_X;
    private static double LABEL_LAYOUT_X = FILMBANNER_LAYOUT_X;
    private static double LABEL_LAYOUT_Y = BUTTON_FILM_OEFFNEN_LAYOUT_Y + 10;
    private static double LABEL_SPACE_Y = FILMBANNER_HEIGHT + BUTTON_FILM_OEFFNEN_HEIGHT + FILMBANNER_SPACE_X;

    Task task;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        task = createTask();
        ProgressDialog progressDialog = new ProgressDialog(task);
        progressDialog.setContentText("Für dich werden nach Filmvorschlägen gesucht");
        progressDialog.setTitle("Filme für dich");
        progressDialog.setTitle("Notflix Filmvorschläge");

        new Thread(task).start();
        progressDialog.showAndWait();

    }

    public Task createTask() {
        return new Task() {
            @Override
            protected Object call() throws Exception {
                zeigeFilmeAn(getFilmVorschlaege());
                return null;
            }
        };
    }

    private void zeigeFilmeAn(List<FilmVorschlag> filmVorschlaege) {

        for(FilmVorschlag filmVorschlag: filmVorschlaege) {
            // Filmbanner
            ImageView filmbanner = new ImageView();
            filmbanner.setFitWidth(FILMBANNER_WIDTH);
            filmbanner.setFitHeight(FILMBANNER_HEIGHT);

            if (FILMBANNER_LAYOUT_X > anchorPane.getPrefWidth() - FILMBANNER_WIDTH) {
                FILMBANNER_LAYOUT_Y += FILMBANNER_HEIGHT + FILMBANNER_SPACE_X;
                BUTTON_FILM_OEFFNEN_LAYOUT_Y += BUTTON_FILM_OEFFNEN_SPACE_Y;
                LABEL_LAYOUT_Y = BUTTON_FILM_OEFFNEN_LAYOUT_Y + 10;

                FILMBANNER_LAYOUT_X = 0;
                BUTTON_FILM_OEFFNEN_LAYOUT_X = FILMBANNER_LAYOUT_X;
                LABEL_LAYOUT_X = FILMBANNER_LAYOUT_X;
            }

            filmbanner.setLayoutX(FILMBANNER_LAYOUT_X);
            filmbanner.setLayoutY(FILMBANNER_LAYOUT_Y);

            zeigeFilmbannerAn(filmbanner, filmVorschlag.getVorgeschlagenerFilm().getFilmbanner());



            // Button Film Öffnen
            zeigeFilmOeffnenButtonAn(filmVorschlag);

            // Filmvorschlag Kommentar
            zeigeFilmVorschlagKommentar(filmVorschlag.getFilmVorschlagKommentar());

        }
    }




    private void zeigeFilmbannerAn(ImageView filmbanner, byte[] filmbannerByte) {

        InputStream inputStream = null;

        if(filmbannerByte == null || filmbannerByte.length == 0) {
            try {
                inputStream = new FileInputStream("Client/src/main/resources/FilmbannerNotFound.png");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            inputStream = new ByteArrayInputStream(filmbannerByte);
        }


        //wandere filmbanner in Image
        BufferedImage bufferedImage = null;
        try {
            bufferedImage = ImageIO.read(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Image filmBannerImage = SwingFXUtils.toFXImage(bufferedImage, null);

        filmbanner.setImage(filmBannerImage);

        anchorPane.getChildren().add(filmbanner);
        FILMBANNER_LAYOUT_X += FILMBANNER_WIDTH + FILMBANNER_SPACE_X;
    }

    private void zeigeFilmOeffnenButtonAn(FilmVorschlag filmVorschlag) {
        Button button_film_oeffnen = new Button();
        button_film_oeffnen.setText("Film öffnen");
        button_film_oeffnen.setPrefWidth(BUTTON_FILM_OEFFNEN_WIDTH);
        button_film_oeffnen.setPrefHeight(BUTTON_FILM_OEFFNEN_HEIGHT);
        button_film_oeffnen.setLayoutX(BUTTON_FILM_OEFFNEN_LAYOUT_X);
        button_film_oeffnen.setLayoutY(BUTTON_FILM_OEFFNEN_LAYOUT_Y);
        button_film_oeffnen.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    ChosenFilm.film = filmVorschlag.getVorgeschlagenerFilm();
                    fxmlLoader.setRoot("NutzerFilmuebersicht");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        anchorPane.getChildren().add(button_film_oeffnen);
        BUTTON_FILM_OEFFNEN_LAYOUT_X = FILMBANNER_LAYOUT_X;
    }

    private void zeigeFilmVorschlagKommentar(String kommentar) {
        Label label = new Label();
        label.setWrapText(true);
        label.setPrefWidth(LABEL_WIDTH);
        label.setPrefHeight(LABEL_HEIGHT);
        label.setLayoutX(LABEL_LAYOUT_X);
        label.setLayoutY(LABEL_LAYOUT_Y);

        anchorPane.getChildren().add(label);
        LABEL_LAYOUT_X = FILMBANNER_LAYOUT_X;

        label.setText(kommentar);
    }

    private List<FilmVorschlag> getFilmVorschlaege() {
        Response<List<FilmVorschlag>> response = null;
        try {
            response = filmVorschlagenEndpoint.getFilmVorschlaege(AktuellerNutzer.aktuellerNutzer.getId()).execute();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if(response != null && response.isSuccessful()) {
            List<FilmVorschlag> vorgeschlageneFilme = response.body();

            System.out.println(vorgeschlageneFilme.size() + " Filmvorschlaege wurde angezeigt");

            return vorgeschlageneFilme;
        }
        return new ArrayList<>();
    }

    public void switchToNutzerstartseite() throws IOException {
        fxmlLoader.setRoot("Nutzerstartseite");

        FILMBANNER_WIDTH = 121;
        FILMBANNER_HEIGHT = 155;
        FILMBANNER_LAYOUT_X = 0;
        FILMBANNER_LAYOUT_Y = 0;
        FILMBANNER_SPACE_X = 200;

        BUTTON_FILM_OEFFNEN_WIDTH = 83;
        BUTTON_FILM_OEFFNEN_HEIGHT = 27;
        BUTTON_FILM_OEFFNEN_LAYOUT_X = FILMBANNER_LAYOUT_X;
        BUTTON_FILM_OEFFNEN_LAYOUT_Y = FILMBANNER_HEIGHT;
        BUTTON_FILM_OEFFNEN_SPACE_Y = FILMBANNER_HEIGHT + FILMBANNER_SPACE_X;

        LABEL_WIDTH = FILMBANNER_WIDTH;
        LABEL_HEIGHT = FILMBANNER_SPACE_X;
        LABEL_LAYOUT_X = FILMBANNER_LAYOUT_X;
        LABEL_LAYOUT_Y = BUTTON_FILM_OEFFNEN_LAYOUT_Y + 10;
        LABEL_SPACE_Y = FILMBANNER_HEIGHT + BUTTON_FILM_OEFFNEN_HEIGHT + FILMBANNER_SPACE_X;
    }
}
