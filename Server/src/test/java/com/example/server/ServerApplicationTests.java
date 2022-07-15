package com.example.server;

import com.example.server.entities.*;
import com.example.server.repository.*;
import com.example.server.restcontroller.BereitsGesehenRestController;
import com.example.server.restcontroller.FilmbewertungRestController;
import com.example.server.restcontroller.MailRestController;
import com.example.server.restcontroller.WatchListRestController;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.AssertTrue;
import java.util.Date;
import java.util.List;

@SpringBootTest
//@RunWith(SpringRunner.class)
//@DataJpaTest
class ServerApplicationTests {

    @Autowired
    private FilmbewertungRestController filmbewertungRestController;

    @Autowired
    private BereitsGesehenRestController bereitsGesehenRestController;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private BereitsGesehenRepository bereitsGesehenRepository;

    @Autowired
    private WatchListRestController watchListRestController;

    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    MailRestController mailRestController;


    @Test
    void contextLoads() {

    }

    @Test
    public void filmBewertungenTesten(){
        //ToDo: Film zu welchem eine bewertung geschrieben wird
        FilmEntity filmEntity = new FilmEntity("Mission Impossible", "Action", "120", "2. August 2018", 2L, 3L, new byte[0]);
        filmEntity.setId(77L);

        //ToDo: Nutzer welche die Bewertung anlegt
        NutzerEntity nutzer = new NutzerEntity("Alice", "Aros", "01.01.1990", "alicearos@gmx.de","Hallo123!", new byte[0]);
        nutzer.setId(200L);

        Long filmId = filmEntity.getId();
        Long nutzerId = nutzer.getId();

        String sternanzahl = "5";

        //ToDo: 1. Filmbewertung unverändert
        FilmbewertungEntity filmbewertungEntity = new FilmbewertungEntity(filmId, nutzerId, nutzer.getVorname(), nutzer.getNachname(), sternanzahl, null, new Date(), null);

        ResponseEntity<FilmbewertungEntity> antwort = filmbewertungRestController.absenden(filmbewertungEntity);

        Assert.assertTrue(antwort.getStatusCode().equals(HttpStatus.OK));

        Long filmBewertungId = antwort.getBody().getId();

        //ToDo: Soll 5 sterne sein
        Assert.assertTrue(antwort.getBody().getSterne().equals("5"));

        String neueSternanzahl = "4";

        ResponseEntity<FilmbewertungEntity> antwort2 = filmbewertungRestController.bewertungBearbeiten(filmBewertungId, filmId , nutzerId,nutzer.getVorname(), nutzer.getNachname(), neueSternanzahl, null);

        Assert.assertTrue(antwort.getStatusCode().equals(HttpStatus.OK));

        //ToDo: neue Bewertung soll jetzt 4 sterne haben
        Assert.assertTrue(antwort2.getBody().getSterne().equals("4"));


    }


    //Film der Bereits als gesehen gekennzeichnet ist, soll von der WatchList verschwinden
    @Test
    public void vonWatchListEntfernen(){
        //ToDo: 1. einen Film zur WatchList hinzufuegen, 2. diesen Film als bereits gesehen kennzeichen, 3. Assert das dieser Film nicht mehr auf der WatchList ist sondern als BereitsGesehen
        FilmEntity filmEntity = new FilmEntity("Mission Impossible", "Action", "120", "2. August 2018", 2L, 3L, new byte[0]);
        filmEntity.setId(100L);

        NutzerEntity nutzer = new NutzerEntity("Alice", "Aros", "01.01.1990", "alicearos@gmx.de","Hallo123!", new byte[0]);
        nutzer.setId(200L);

        Long filmdId = nutzer.getId();

        Long nutzerId = filmEntity.getId();

        WatchListEntity watchListEntity = new WatchListEntity(filmdId, nutzerId, null, null);

        ResponseEntity<WatchListEntity> antwort1 = watchListRestController.filmZuWatchListAnlegen(watchListEntity);

        //ToDo: Pruefen ob der Film zur WatchList hinzugefuegt wurde

        Assert.assertTrue(antwort1.getStatusCode().equals(HttpStatus.OK));

        BereitsGesehenEntity bereitsGesehenEntity = new BereitsGesehenEntity(filmdId,nutzerId, new Date(), null);

        ResponseEntity<BereitsGesehenEntity> antwort2 = bereitsGesehenRestController.filmZuBereitsGesehenAnlegen(bereitsGesehenEntity);

        //ToDo: Pruefen ob der Film zu BereitsGesehen hinzugefuegt wurde
        Assert.assertTrue(antwort2.getStatusCode().equals(HttpStatus.OK));


        //ToDo: Film von WatchList löschen mit WatchListRestController

        watchListRestController.filmVonWatchListEntfernen(filmdId);

        //ToDo: Schauen ob der Film anhand der FilmId nicht mehr in der WatchList ist sondern nur noch bei gesehen

        List<WatchListEntity> watchlistListe = watchListRepository.findAllByFilmIdAndNutzerId(filmdId, nutzerId);

        List<BereitsGesehenEntity> bereitsgesehendeListe = bereitsGesehenRepository.findAllByFilmIdAndNutzerId(filmdId,nutzerId);

        if(watchlistListe.isEmpty())
            System.out.println("Leerer Liste wie es sein soll");

        for(WatchListEntity watchList : watchlistListe){
            System.out.println(watchList.getFilmId());
        }

        //Watchlist muss leer sein
        Assert.assertTrue(watchlistListe.isEmpty());

        //GesehendeListe darf NICHT leer sein

        Assert.assertFalse(bereitsgesehendeListe.isEmpty());

    }

    @Test
    public void zweiFaktorAuthentifizierung(){
        NutzerEntity nutzer = new NutzerEntity("Tom", "Cruise", "03.07.1962", "t@gmail.com", "Tom123!", null);
        ResponseEntity<String> antwort1 = mailRestController.sendMail(nutzer.getEmail());

        String serverCode = antwort1.getBody();

        Assert.assertEquals(antwort1.getStatusCode(), HttpStatus.OK);

        ResponseEntity<Boolean> antwort2 = mailRestController.mailCode(nutzer.getEmail(), serverCode);

        Assert.assertTrue(antwort2.getBody());

    }
}

    /*
    //Film als gesehen Kennzeichnen
    @Test
    public void bereitsGesehen(){
        NutzerEntity nutzer = new NutzerEntity("Alice", "Aros", "01.01.1990", "alicearos@gmx.de","Hallo123!", new byte[0]);

        Long nutzerId = nutzerRepository.save(nutzer).getId();

        FilmEntity filmEntity = new FilmEntity("Wanted", "Thriller", "110", "2008", 2L, 3L, new byte[0]);

        Long filmId = filmRepository.save(filmEntity).getId();

        BereitsGesehenEntity bereitsGesehenEntity = new BereitsGesehenEntity(filmId, nutzerId);

        ResponseEntity<BereitsGesehenEntity> antwort = bereitsGesehenRestController.filmZuBereitsGesehenAnlegen(bereitsGesehenEntity);

        Assert.assertTrue(bereitsGesehenRepository.findAllByNutzerId(nutzerId) != null);

        List<BereitsGesehenEntity> liste = bereitsGesehenRepository.findAllByFilmIdAndNutzerId(filmId, nutzerId);

        Assert.assertTrue(bereitsGesehenRepository.findAllByFilmIdAndNutzerId(filmId, nutzerId) != null);

    }

     */





