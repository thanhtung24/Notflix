package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.java.FilmVorschlag;
import com.example.server.repository.FilmRepository;
import com.example.server.repository.PersonRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FilmVorschlagenServiceTest {

    @InjectMocks
    private FilmVorschlagenService testObjekt;

    @Mock
    private FilmRepository filmRepository;

    @Mock
    private PersonRepository personRepository;

    @Test
    public void soll_15_Filme_vorschlagen() {

        // set up

        // Id vom aktuellen Nutzer
        Long nutzerId = 1L;
        
        List<Long> randomResultFromFilmRepository = Arrays.asList(0L);
        Long randomLong = 24L;

        // Alle geschauten Filme
        List<FilmEntity> zuletztGeschauteFilme = new ArrayList<>(Arrays.asList(
                new FilmEntity("Iron Man", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Iron Man 2", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null)
        ));

        // Ids von geschauten Filmen
        List<Long> geschauteFilmeIds = new ArrayList<>(Arrays.asList(1L, 2L));

        // ID vom meist geschauten Schauspieler
        Long favActorId = 10L;
        PersonEntity favActor = new PersonEntity("Robert", "Downey");
        favActor.setId(favActorId);

        // proceed
        when(filmRepository.getLastWatchedFilm(nutzerId)).thenReturn(geschauteFilmeIds);

        int filmId = 0;
        when(filmRepository.getById(anyLong()))
                .thenReturn(

                        // erwartet, dass alle zuletzt geschauten Filme zurückgegeben werden
                        zuletztGeschauteFilme.get(filmId++), zuletztGeschauteFilme.get(filmId++),

                        new FilmEntity("Best film based on actor, category and rate", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                        new FilmEntity("Best film based on actor, category", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                        new FilmEntity("Best film based on category", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                        new FilmEntity("Best film based on actor", "Horror", "2 Stunden", "24.01.2000", 1L, 1L, null),

                        new FilmEntity("Relevant film", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),

                        new FilmEntity("Best film based on friend's watched films", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),

                        new FilmEntity("Best film in database", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                        new FilmEntity("Most watched film", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null)
                );

        when(filmRepository.getMostWatchedActor(nutzerId)).thenReturn(favActorId);
        when(personRepository.getById(favActorId)).thenReturn(favActor);

        when(filmRepository.getFilmsBasedOnActorAndRate(favActor.getId())).thenReturn(Arrays.asList(randomLong));

        when(filmRepository.getFilmsBasedOnActor(favActor.getId())).thenReturn(Arrays.asList(randomLong));

        when((filmRepository.getMostWatchedActor(nutzerId))).thenReturn(favActorId);

        when(filmRepository.getSuggestedFilmsBasedOnWatchedFilm(any(), eq(nutzerId))).thenReturn(randomResultFromFilmRepository);

        when(filmRepository.getBestFilmsFromFriends(nutzerId)).thenReturn(randomResultFromFilmRepository);

        when(filmRepository.getBestFilmsInDB()).thenReturn(randomResultFromFilmRepository);

        when(filmRepository.getMostWatchedFilms()).thenReturn(randomResultFromFilmRepository);

        when(filmRepository.findAll()).thenReturn(Arrays.asList(
                new FilmEntity("Random film 1", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 2", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 3", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 4", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 5", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 6", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 7", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 8", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null)
        ));

        // assert
        ResponseEntity<List<FilmVorschlag>> erwarteteResponse = getErwartetesErgebnissVomFilmVorschlagen();
        ResponseEntity<List<FilmVorschlag>> response = testObjekt.schlageFilmeVor(nutzerId);

        verify(filmRepository).getLastWatchedFilm(nutzerId);
       // verify(filmRepository).findAllById(geschauteFilmeIds);
        verify(filmRepository, times(10)).getById(anyLong());
        verify(filmRepository).getMostWatchedActor(nutzerId);
        verify(personRepository).getById(favActorId);
        verify(filmRepository, times(2)).getFilmsBasedOnActorAndRate(favActor.getId());
        verify(filmRepository).getMostWatchedActor(nutzerId);
        verify(filmRepository, times(1)).getSuggestedFilmsBasedOnWatchedFilm(any(), eq(nutzerId));
        verify(filmRepository).getBestFilmsFromFriends(nutzerId);
        verify(filmRepository, times(2)).getBestFilmsInDB();
        verify(filmRepository).getMostWatchedFilms();
        verify(filmRepository).findAll();

        assertEquals(erwarteteResponse.getBody(), response.getBody());
        assertEquals(erwarteteResponse.getStatusCode(), response.getStatusCode());
    }

    private ResponseEntity<List<FilmVorschlag>> getErwartetesErgebnissVomFilmVorschlagen() {

        List<FilmVorschlag> expectedResult = new ArrayList<>();

        List<FilmEntity> vorgeschlageneFilme = new ArrayList<>(Arrays.asList(
                new FilmEntity("Best film based on actor, category and rate", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Best film based on actor, category", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Best film based on category", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Best film based on actor", "Horror", "2 Stunden", "24.01.2000", 1L, 1L, null),

                new FilmEntity("Relevant film", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),

                new FilmEntity("Best film based on friend's watched films", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),

                new FilmEntity("Best film in database", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Most watched film", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),

                new FilmEntity("Random film 1", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 2", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 3", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 4", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 5", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 6", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Random film 7", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null)
        ));

        expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(0), "Hier ist ein guter Film basiert auf deine Lieblingskategorie und deinen Lieblingsschauspieler"));
        expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(1), "Hier ist ein zufälliger Film basiert auf deine Lieblingskategorie und deinen Lieblingsschauspieler"));
        expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(2), "Hier ist einer der besten Action Filme in unserer Filmdatenbank"));
        expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(3), "Hier ist ein guter Film basiert auf deinen Lieblingsschauspieler"));

        expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(4), "Nutzer, die Iron Man geschaut haben, finden diesen Action Film auch gut"));

        expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(5), "Deine Freunde haben diesen Action Film gerne geschaut"));

        expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(6), "Hier ist einer der besten Filme in unserer Filmdatenbank"));
        expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(7), "Hier ist einer der meist gesehenen Filme in unserer Filmdatenbank"));

        for(int i=8; i < vorgeschlageneFilme.size(); i++) {
            expectedResult.add(new FilmVorschlag(vorgeschlageneFilme.get(i), "Hier ist ein zufälliger Film für dich"));
        }

        return new ResponseEntity<>(expectedResult, HttpStatus.OK);
    }

    @Test
    public void soll_die_meist_geschaute_Kategorie_zurueckgeben() {
        // set up
        List<String> alleGeschautenKategorien = new ArrayList<>(Arrays.asList(
                "Action", "Action", "Horror", "Fantasy", "Drama", "Horror", "Action"
        ));

        String erwartetesErgebnis = "Action";

        // assert
        String dieMeistGeschauteKategorie = testObjekt.getMostWatchedCategory(alleGeschautenKategorien);

        assertEquals(dieMeistGeschauteKategorie, erwartetesErgebnis);
    }

    @Test
    public void soll_Filme_nach_der_Reihenfolge_der_filmIds_zurueckgeben() {
        // set up
        List<Long> alleFilmIds = new ArrayList<>(Arrays.asList(
                1L, 2L, 3L, 4L, 5L, 6L
        ));

        List<FilmEntity> erwartetesErgebnis = new ArrayList<>(Arrays.asList(
                new FilmEntity("Film1", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Film2", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Film3", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),
                new FilmEntity("Film4", "Horror", "2 Stunden", "24.01.2000", 1L, 1L, null),

                new FilmEntity("Film5", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null),

                new FilmEntity("Film6", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null)
        ));

        for(int i=0; i < alleFilmIds.size() ; i++) {
            erwartetesErgebnis.get(i).setId(alleFilmIds.get(i));
        }

        // proceed
        when(filmRepository.getById(1L)).thenReturn(erwartetesErgebnis.get(0));
        when(filmRepository.getById(2L)).thenReturn(erwartetesErgebnis.get(1));
        when(filmRepository.getById(3L)).thenReturn(erwartetesErgebnis.get(2));
        when(filmRepository.getById(4L)).thenReturn(erwartetesErgebnis.get(3));
        when(filmRepository.getById(5L)).thenReturn(erwartetesErgebnis.get(4));
        when(filmRepository.getById(6L)).thenReturn(erwartetesErgebnis.get(5));

        // assert
        List<FilmEntity> filme = testObjekt.findeFilmeAnhandFilmIds(alleFilmIds);

        verify(filmRepository).getById(1L);
        verify(filmRepository).getById(2L);
        verify(filmRepository).getById(3L);
        verify(filmRepository).getById(4L);
        verify(filmRepository).getById(5L);
        verify(filmRepository).getById(6L);
        assertEquals(filme, erwartetesErgebnis);
    }
}