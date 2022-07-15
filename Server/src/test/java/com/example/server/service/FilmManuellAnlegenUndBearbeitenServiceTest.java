package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.FilmStatistikEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.repository.FilmRepository;
import com.example.server.repository.FilmStatistikRepository;
import com.example.server.repository.PersonRepository;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class FilmManuellAnlegenUndBearbeitenServiceTest {

    @Mock
    FilmCastService filmCastService;

    @Mock
    FilmRepository filmRepository;

    @Mock
    PersonRepository personRepository;

    @Mock
    FilmStatistikRepository filmStatistikRepository;

    @InjectMocks
    FilmManuellAnlegenUndBearbeitenService testObjekt;

    @Test
    public void soll_einen_Film_anhand_name_und_regisseur_zurueckgeben() {
        // set up
        String filmName = "Iron Man";
        Long regisseurId = 1L;

        List<FilmEntity> filmEntities = new ArrayList<>(Arrays.asList(
                new FilmEntity("Iron Man", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null)
        ));

        when(filmRepository.findAllByNameAndRegisseurId(filmName, regisseurId)).thenReturn(filmEntities);

        // proceed

        ResponseEntity<FilmEntity> erwarteteResponse = new ResponseEntity<>(filmEntities.get(0), HttpStatus.OK);
        ResponseEntity<FilmEntity> response = testObjekt.getFilmByNameAndRegisseur(filmName, regisseurId);

        // assert
        verify(filmRepository).findAllByNameAndRegisseurId(filmName, regisseurId);
        assertEquals(response.getBody(), erwarteteResponse.getBody());
        assertEquals(response.getStatusCode(), erwarteteResponse.getStatusCode());
    }

    @Test
    public void soll_keinen_Film_anhand_name_und_regisseur_zurueckgeben() {
        // set up
        String filmName = "Iron Man";
        Long regisseurId = 1L;
        List<FilmEntity> filmEntities = new ArrayList<>();

        when(filmRepository.findAllByNameAndRegisseurId(anyString(), anyLong())).thenReturn(filmEntities);

        // proceed

        ResponseEntity<FilmEntity> erwarteteResponse = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        ResponseEntity<FilmEntity> response = testObjekt.getFilmByNameAndRegisseur(filmName, regisseurId);

        // assert
        verify(filmRepository).findAllByNameAndRegisseurId(filmName, regisseurId);
        assertEquals(response.getBody(), erwarteteResponse.getBody());
        assertEquals(response.getStatusCode(), erwarteteResponse.getStatusCode());
    }

    @Test
    public void soll_einen_Film_anlegen() {
        // set up
        FilmEntity neuerFilm = new FilmEntity("Iron Man", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null);
        neuerFilm.setId(0L);
        FilmStatistikEntity filmStatistik = new FilmStatistikEntity(neuerFilm.getId(), new Date());
        List<FilmEntity> filmEntities = new ArrayList<>();

        when(filmRepository.findAllByNameAndRegisseurId(neuerFilm.getName(), neuerFilm.getRegisseurId())).thenReturn(filmEntities);
        when(filmRepository.save(neuerFilm)).thenReturn(neuerFilm);
        when(filmStatistikRepository.save(any())).thenReturn(filmStatistik);

        // proceed
        ResponseEntity<FilmEntity> erwarteteResponse = new ResponseEntity<>(neuerFilm, HttpStatus.OK);
        ResponseEntity<FilmEntity> response = testObjekt.filmAnlegen(neuerFilm);

        // assert
        verify(filmRepository).findAllByNameAndRegisseurId(neuerFilm.getName(), neuerFilm.getRegisseurId());
        verify(filmRepository).save(neuerFilm);
        verify(filmStatistikRepository).save(any());
        assertEquals(response.getBody(), erwarteteResponse.getBody());
        assertEquals(response.getStatusCode(), erwarteteResponse.getStatusCode());
    }

    @Test
    public void soll_keinen_Film_anlegen() {
        // set up
        FilmEntity neuerFilm = new FilmEntity("Iron Man", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null);
        List<FilmEntity> filmEntities = new ArrayList<>(Arrays.asList(neuerFilm));

        when(filmRepository.findAllByNameAndRegisseurId(neuerFilm.getName(), neuerFilm.getRegisseurId())).thenReturn(filmEntities);

        // proceed
        ResponseEntity<FilmEntity> erwarteteResponse = new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        ResponseEntity<FilmEntity> response = testObjekt.filmAnlegen(neuerFilm);

        // assert
        verify(filmRepository).findAllByNameAndRegisseurId(neuerFilm.getName(), neuerFilm.getRegisseurId());
        assertEquals(response.getBody(), erwarteteResponse.getBody());
        assertEquals(response.getStatusCode(), erwarteteResponse.getStatusCode());
    }

    @Test
    public void soll_einen_Film_bearbeiten() {
        // set up
        Long filmId = 1L;
        FilmEntity zuBearbeitenderFilm = new FilmEntity("Iron Man", "Action", "2 Stunden", "24.01.2000", 1L, 1L, null);
        zuBearbeitenderFilm.setId(filmId);
        ResponseEntity<Long> responseEntity = new ResponseEntity<>(1L, HttpStatus.OK);

        when(filmRepository.getById(filmId)).thenReturn(zuBearbeitenderFilm);
        when(filmRepository.save(zuBearbeitenderFilm)).thenReturn(zuBearbeitenderFilm);
        when(filmCastService.deleteFilmCastByFilmId(filmId)).thenReturn(responseEntity);

        // proceed
        ResponseEntity<FilmEntity> erwarteteResponse = new ResponseEntity<>(zuBearbeitenderFilm, HttpStatus.OK);
        ResponseEntity<FilmEntity> response = testObjekt.bearbeiteFilm(1L,"Iron Man 2", "Horror", "3 Stunden", "01.01.2000", 1L, 1L, "");

        // assert
        verify(filmRepository).getById(filmId);
        verify(filmRepository).save(zuBearbeitenderFilm);
        verify(filmCastService).deleteFilmCastByFilmId(filmId);
        assertEquals(response.getBody(), erwarteteResponse.getBody());
        assertEquals(response.getStatusCode(), erwarteteResponse.getStatusCode());
    }

    @Test
     public void soll_eine_Person_finden() {
        // set up
        PersonEntity gefundenePerson = new PersonEntity("Jack", "Jack");
        Long personId = 1L;
        gefundenePerson.setId(1L);

        List<PersonEntity> personen = new ArrayList<>(Arrays.asList(gefundenePerson));

        when(personRepository.findAllById(personId)).thenReturn(personen);

        // proceed
        PersonEntity erwartetePerson = gefundenePerson;
        PersonEntity person = testObjekt.getPersonById(personId);

        // assert
        verify(personRepository).findAllById(personId);
        assertEquals(person.getId(), erwartetePerson.getId());
        assertEquals(person.getVorname(), erwartetePerson.getVorname());
        assertEquals(person.getNachname(), erwartetePerson.getNachname());
    }

    @Test
    public void soll_keine_Person_finden() {
        // set up
        PersonEntity gefundenePerson = new PersonEntity("Jack", "Jack");
        Long personId = 1L;
        gefundenePerson.setId(1L);

        List<PersonEntity> personen = new ArrayList<>();

        // proceed
        PersonEntity person = testObjekt.getPersonById(personId);

        // assert
        assert(person == null);
    }

    @Test
    public void getPersonenByFilmId() {
        // set up
        PersonEntity gefundenePerson = new PersonEntity("Jack", "Jack");
        Long personId = 1L;
        gefundenePerson.setId(1L);

        Long filmId = 2L;
        List<PersonEntity> personen = new ArrayList<>(Arrays.asList(
           new PersonEntity("Justin", "Justin"),
                new PersonEntity("Thor", "Thor"),
                new PersonEntity("Stark", "Stark")
        ));

        when(personRepository.findAllByFilmId(filmId)).thenReturn(personen);

        // proceed
        List<PersonEntity> erwartetePersonEntities = personen;
        List<PersonEntity> personEntities = testObjekt.getPersonenByFilmId(filmId);

        // assert
        verify(personRepository).findAllByFilmId(filmId);
        assertEquals(personEntities, erwartetePersonEntities);

    }
}