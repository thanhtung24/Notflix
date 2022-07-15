package com.example.server.adminstatistik;

import com.example.server.entities.BereitsGesehenEntity;
import com.example.server.entities.FilmStatistikEntity;
import com.example.server.entities.FilmbewertungEntity;
import com.example.server.repository.BereitsGesehenRepository;
import com.example.server.repository.FilmStatistikRepository;
import com.example.server.repository.FilmbewertungRepository;
import com.example.server.service.FilmStatistikService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class SystemadministratorStatistikTests {

    @Mock
    private FilmStatistikRepository filmStatistikRepository;

    @Mock
    private FilmbewertungRepository filmbewertungRepository;

    @Mock
    private BereitsGesehenRepository bereitsGesehenRepository;

    @InjectMocks
    private FilmStatistikService zuTesten;



    @Test
    public void statistikAnzahlUndDurcschnittlicheBewertung(){
        Long filmId = 11L;

        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        //Simmuliere UnterschiedlicheBewertungsTage
        Date datum1 = new Date(System.currentTimeMillis() - (6 * DAY_IN_MS));
        Date datum2 = new Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        Date datum3 = new Date(System.currentTimeMillis() - (8 * DAY_IN_MS));
        Date datum4 = new Date(System.currentTimeMillis() - (9 * DAY_IN_MS));

        Date datum5 = new Date(System.currentTimeMillis() - (10 * DAY_IN_MS));

        List<FilmbewertungEntity> filmbewertungEntityList = new ArrayList<>(Arrays.asList(
                new FilmbewertungEntity(filmId, 12L, "Albert", "Einstein", "4 Sterne", "", datum1, "Film 1"),
                new FilmbewertungEntity(filmId, 13L, "Frank", "Stein", "2 Sterne", "", datum1, "Film 2"),
                new FilmbewertungEntity(filmId, 14L, "Felix", "Vollbart", "3 Sterne", "", datum2, "Film 3"),
                new FilmbewertungEntity(filmId, 15L, "Gina", "Trade", "5 Sterne", "", datum3, "Film 4"),
                new FilmbewertungEntity(filmId, 16L, "Liam", "Scott", "2 Sterne", "", datum4, "Film 5")

        ));


        List<BereitsGesehenEntity> zuletztGeschauteFilme = new ArrayList<>(Arrays.asList(
                new BereitsGesehenEntity(filmId, 14L, datum3, "Film 3"),
                new BereitsGesehenEntity(filmId, 15L, datum4, "Film 4")
        ));

        when(filmbewertungRepository.findAllByFilmId(filmId)).thenReturn(filmbewertungEntityList);
        when(bereitsGesehenRepository.findByFilmId(filmId)).thenReturn(zuletztGeschauteFilme);

        FilmStatistikEntity filmStatistik = new FilmStatistikEntity(filmId, datum5);

        when(filmStatistikRepository.findByfilmId(filmId)).thenReturn(filmStatistik);

        ResponseEntity<HashMap<String,Integer>> actualResult = zuTesten.getAnzahl(filmId);

        ResponseEntity<String> tatsaechlicherDurchSchnitt = zuTesten.getDurchschnitt();

        ResponseEntity<String> erwarteterDurchschnitt = erwarteterDurchSchnitt();

        ResponseEntity<HashMap<String,Integer>> expectedResult = erwartetetsErgebnis(2, 5, 0, 2, 1, 1, 1);

        verify(filmbewertungRepository, times(6)).findAllByFilmId(filmId);
        verify(bereitsGesehenRepository, times(1)).findByFilmId(filmId);
        verify(filmStatistikRepository, times(6)).findByfilmId(filmId);

        assertEquals(tatsaechlicherDurchSchnitt.getBody(), erwarteterDurchschnitt.getBody());

        assertEquals(actualResult.getBody(), expectedResult.getBody());

        System.out.println("Tatsaechlicher Durchschnitt: " + tatsaechlicherDurchSchnitt.getBody());

        System.out.println("Erwarteter Durchschnitt: " + erwarteterDurchschnitt.getBody());


    }
    //ZU erwarteter
    public ResponseEntity<HashMap<String, Integer>> erwartetetsErgebnis(int gesehen, int bewertung, int eins,
                                                                        int zwei, int drei, int vier, int fuenf){
        HashMap<String, Integer> result = new HashMap<>();
        result.put("Anzahl Gesehen", gesehen);
        result.put("Anzahl Bewertung", bewertung);
        result.put("Anzahl 1 Stern", eins);
        result.put("Anzahl 2 Sterne", zwei);
        result.put("Anzahl 3 Sterne", drei);
        result.put("Anzahl 4 Sterne", vier);
        result.put("Anzahl 5 Sterne", fuenf);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    public ResponseEntity<String> erwarteterDurchSchnitt(){
        String ergebnis;

        double zwischenergebnis=0;

        double anzahlBewertung= 5;

        zwischenergebnis= ((0*1)+ (2*2) + (1*3)+ (1*4)+ (1*5)) / anzahlBewertung;


        zwischenergebnis= Math.round((zwischenergebnis*10)) / 10.00;


        ergebnis = zwischenergebnis +"";
        return new ResponseEntity<>(ergebnis, HttpStatus.OK);
    }
    @Test
    public void vorZurueckSetzen2FilmeNachZurueckSetzen1Film(){
        Long filmId = 22L;

        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        //Simmuliere UnterschiedlicheBewertungsTage
        Date datum = new Date(System.currentTimeMillis() - (6 * DAY_IN_MS));
        Date datum2 = new Date(System.currentTimeMillis() - (8 * DAY_IN_MS));
        //Datum in der Zukunft simmulieren denn bei zureucksetzen ab dem heute zeitpunkt, vergangenheit nicht beachtet nur zukunft
        //bsp. zureuck gesetzt am 05.07, dann nur > 05.07 und nicht < 05.07
        Date datum3 = new Date(System.currentTimeMillis() + (2 * DAY_IN_MS));

        List<FilmbewertungEntity> filmbewertungEntityList = new ArrayList<>(Arrays.asList(
                new FilmbewertungEntity(filmId, 12L, "Rudolf", "Heiss", "3 Sterne", "", datum, "Film 1"),
                new FilmbewertungEntity(filmId, 13L, "Martin", "Beck", "1 Stern", "", datum, "Film 2")

        ));
        //Leere Liste -> keine zuletzt gesehen
        List<BereitsGesehenEntity> zuletztGeschauteFilme = new ArrayList<>();

        FilmStatistikEntity filmStatistik = new FilmStatistikEntity(filmId, datum2);

        when(filmbewertungRepository.findAllByFilmId(filmId)).thenReturn(filmbewertungEntityList);
        when(bereitsGesehenRepository.findByFilmId(filmId)).thenReturn(zuletztGeschauteFilme);

        when(filmStatistikRepository.findByfilmId(filmId)).thenReturn(filmStatistik);

        ResponseEntity<HashMap<String,Integer>> actualResult = zuTesten.getAnzahl(filmId);

        ResponseEntity<HashMap<String,Integer>> expectedResult = erwartetetsErgebnis(0, 2, 1, 0, 1, 0, 0);

        assertEquals(actualResult.getBody(), expectedResult.getBody());


        zuTesten.zuruecksetzen(filmId);

        List<BereitsGesehenEntity> zuletztGeschauteFilme2 = new ArrayList<>(Arrays.asList(
                new BereitsGesehenEntity(filmId, 23L, datum3, null)
        ));

        when(bereitsGesehenRepository.findByFilmId(filmId)).thenReturn(zuletztGeschauteFilme2);

        ResponseEntity<HashMap<String,Integer>> actualResult2 = zuTesten.getAnzahl(filmId);

        ResponseEntity<HashMap<String,Integer>> expectedResult2 = erwartetetsErgebnis(1, 0, 0, 0, 0, 0, 0);

        assertEquals(actualResult2.getBody(), expectedResult2.getBody());
    }

}
