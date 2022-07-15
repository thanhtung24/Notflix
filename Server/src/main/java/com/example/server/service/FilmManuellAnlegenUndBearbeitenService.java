package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.FilmStatistikEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.repository.FilmRepository;
import com.example.server.repository.FilmStatistikRepository;
import com.example.server.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Base64;
import java.util.Date;
import java.util.List;

@Component
@Service
@Transactional
public class FilmManuellAnlegenUndBearbeitenService {

    @Autowired
    private FilmCastService filmCastService;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FilmStatistikRepository filmStatistikRepository;

    public ResponseEntity<FilmEntity> getFilmByNameAndRegisseur(String name, Long regisseurId) {
        List<FilmEntity> gefundeneFilme = filmRepository.findAllByNameAndRegisseurId(name, regisseurId);
        if(gefundeneFilme.isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(gefundeneFilme.get(0), HttpStatus.OK);
    }

    private Date datum;
    public ResponseEntity<FilmEntity> filmAnlegen(FilmEntity filmEntity) {
        if(filmRepository.findAllByNameAndRegisseurId(filmEntity.getName(), filmEntity.getRegisseurId()).isEmpty()) {
            filmRepository.save(filmEntity);

            //Manuell Angelegten Film für FilmStatistikEntity
            zuFilmStatistikEntityhinzufuegen(filmEntity);

            return new ResponseEntity<>(filmEntity, HttpStatus.OK);
        }
        return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
    }

    public void zuFilmStatistikEntityhinzufuegen(FilmEntity filmEntity){
        datum= new Date();
        FilmStatistikEntity filmStatistikEntity= new FilmStatistikEntity(filmEntity.getId(), datum);
        filmStatistikRepository.save(filmStatistikEntity);
    }

    public ResponseEntity<FilmEntity> bearbeiteFilm(Long filmId, String name, String kategorie, String filmLaenge, String erscheinungsdatum, Long regisseurId, Long drehbuchautorId, String filmbanner) {
        FilmEntity film = this.filmRepository.getById(filmId);
        try {
            film.setName(name);
            film.setKategorie(kategorie);
            film.setFilmLaenge(filmLaenge);
            film.setErscheinungsdatum(erscheinungsdatum);
            film.setRegisseurId(regisseurId);
            film.setDrehbuchautorId(drehbuchautorId);

            //verhindert, dass das aktuelle Filmbanner gelöscht wird, wenn der User kein neues Filmbanner hochlädt
            if(!filmbanner.isEmpty()) {
                film.setFilmbanner(Base64.getDecoder().decode(filmbanner));
            }

            this.filmRepository.save(film);

            //alten Filmcast löschen
            this.filmCastService.deleteFilmCastByFilmId(filmId);

            return new ResponseEntity<>(film, HttpStatus.OK);
        } catch (Exception e) {
            System.out.println("Fehler beim Bearbeiten von" + film.getName());
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public PersonEntity getPersonById(Long personId) {
        List<PersonEntity> personEntities = personRepository.findAllById(personId);
        if(!personEntities.isEmpty()) {
            return personEntities.get(0);
        }
        return null;
    }

    public List<PersonEntity> getPersonenByFilmId(Long filmId) {
        return personRepository.findAllByFilmId(filmId);
    }
}
