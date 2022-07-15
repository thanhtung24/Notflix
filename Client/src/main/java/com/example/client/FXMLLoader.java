package com.example.client;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FXMLLoader {

    @Autowired
    private ApplicationContext applicationContext;

    private Scene scene;

    private Stage stage;

    private final int widthSize = 900;

    private final int heightSize = 650;

    public void setRoot(String fxml) throws IOException {
        if(fxml.equals("AdminBerechtigung") ){
            this.stage.setHeight(200);
            this.stage.setHeight(500);
        } else if(fxml.equals("Nutzerstartseite")) {
            this.stage.setHeight(650);
            this.stage.setWidth(900);
        }  else if(fxml.equals("FilmEinladungsdetail")) {
            this.stage.setHeight(700);
            this.stage.setWidth(900);
        } else if(fxml.equals("Nutzerprofil")){
            this.stage.setHeight(618);
            this.stage.setWidth(1196);
        }else if(fxml.equals("NutzerverhaltenStatistik")) {
            this.stage.setHeight(750);
            this.stage.setWidth(1000);
        } else if(fxml.equals("FilmVorschlagen")) {

        } else {
            this.stage.setWidth(widthSize);
            this.stage.setHeight(heightSize);
        }
        this.scene.setRoot(loadFxml(fxml));
    }

    public Parent loadFxml(String fxml) throws IOException {
        //alle Fxml datein sollten im ressources Ordner liegen
        javafx.fxml.FXMLLoader fxmlLoader = new javafx.fxml.FXMLLoader(ClientApplication.class.getResource( "/" + fxml + ".fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);
        return fxmlLoader.load();
    }

    //Nutzerstartseite
    //Filmliste
    public void initialize() {
        try {
            this.scene = new Scene(this.loadFxml("GlobaleStartseite"), widthSize, heightSize);
            this.stage = new Stage();
            this.stage.setScene(scene);
            this.stage.setTitle("Notflix");
            this.stage.show();
            //gut oder schlecht ist hier zu noch beurteilen
            this.stage.setResizable(!stage.isResizable());

            // schließe das Programm vollständig und gebe den genutzten Port frei
            this.stage.setOnCloseRequest(event -> System.exit(0));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
