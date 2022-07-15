package com.example.server.restcontroller;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.service.FilmManuellAnlegenUndBearbeitenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filmManuellAnlegenUndBearbeiten")
public class FilmManuellAnlegenUndBearbeitenRestController {

    @Autowired
    FilmManuellAnlegenUndBearbeitenService filmManuellAnlegenUndBearbeitenService;


    @GetMapping("/getFilmByNameAndRegisseur")
    public ResponseEntity<FilmEntity> getFilmByNameAndRegisseur(@RequestParam String filmName, @RequestParam Long regisseurId) {
        return this.filmManuellAnlegenUndBearbeitenService.getFilmByNameAndRegisseur(filmName, regisseurId);
    }

    @PostMapping("/filmAnlegen")
    public ResponseEntity<FilmEntity> filmAnlegen (@RequestBody FilmEntity filmEntity) {
        int code = filmManuellAnlegenUndBearbeitenService.filmAnlegen(filmEntity).getStatusCode().value();
        if(code == 200) {
            return new ResponseEntity<>(filmEntity, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/filmBearbeiten")
    public ResponseEntity<FilmEntity> bearbeiteFilm(@RequestParam("filmId") Long filmId,
                                              @RequestParam("neuerFilmName") String name,
                                              @RequestParam("neuerFilmKategorie") String kategorie,
                                              @RequestParam("neuerFilmLaenge") String filmLaenge,
                                              @RequestParam("neuerFilmErscheinungsdatum") String erscheinungsdatum,
                                              @RequestParam("neuerFilmRegisseurId") Long regisseurId,
                                              @RequestParam("neuerFilmDrebuchautorID") Long drehbuchautorId,
                                              @RequestParam("neuerFilmFilmbanner") String filmbanner) {
        return this.filmManuellAnlegenUndBearbeitenService.bearbeiteFilm(filmId, name, kategorie, filmLaenge, erscheinungsdatum, regisseurId, drehbuchautorId, filmbanner);
    }

    @GetMapping("/getPerson/{personId}")
    ResponseEntity<PersonEntity> getPersonById(@PathVariable Long personId) {
        return new ResponseEntity<>(this.filmManuellAnlegenUndBearbeitenService.getPersonById(personId), HttpStatus.OK);
    }

    @GetMapping("/getCast")
    ResponseEntity<List<PersonEntity>> getPersonenByFilmId(@RequestParam Long filmId) {
        return new ResponseEntity<>(this.filmManuellAnlegenUndBearbeitenService.getPersonenByFilmId(filmId), HttpStatus.OK);
    }
}
