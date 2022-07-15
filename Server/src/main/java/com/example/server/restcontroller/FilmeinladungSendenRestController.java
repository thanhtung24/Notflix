package com.example.server.restcontroller;

import com.example.server.entities.FilmeinladungEntity;
import com.example.server.entities.FreundschaftsanfrageEntity;
import com.example.server.service.FilmeinladungSendenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filmeinladung")

public class FilmeinladungSendenRestController {

    @Autowired
    private FilmeinladungSendenService filmeinladungSendenService;

    @PostMapping("/sendeFilmeinladung")
    ResponseEntity <FilmeinladungEntity> saveFilmeinladung(@RequestBody FilmeinladungEntity filmeinladung) {
        return this.filmeinladungSendenService.saveFilmeinladung(filmeinladung);
    }

    @GetMapping ("/alleFilmeinladungen/{einladungsempfaengerId}")
    ResponseEntity <List<FilmeinladungEntity>> getAlleFilmeinladungenFromPerson (@PathVariable Long einladungsempfaengerId) {
        return this.filmeinladungSendenService.getAlleFilmeinladungenFromPerson(einladungsempfaengerId);
    }

    @GetMapping ("/getAlleFilmeinladungenWhereAkzeptiert/{einladungsenderId}")
    ResponseEntity <List<FilmeinladungEntity>> getAlleFilmeinladungenWhereAkzeptiert (@PathVariable Long einladungsenderId) {
        return this.filmeinladungSendenService.getAlleFilmeinladungenWhereAkzeptiert(einladungsenderId);
    }

    @PutMapping("/akzeptiert")
    public ResponseEntity <FilmeinladungEntity> akzeptiereFilmeinladnung (@RequestParam("filmeinladungsId") Long filmeinladungsId) {
        return this.filmeinladungSendenService.bearbeiteReportAkzeptiert(filmeinladungsId);
    }

    @PutMapping("/gesehen")
    public ResponseEntity <FilmeinladungEntity> geseheneAkzeptierteFilmeinladnung (@RequestParam("filmeinladungsId") Long filmeinladungsId) {
        return this.filmeinladungSendenService.bearbeiteReportGesehen(filmeinladungsId);
    }

    @DeleteMapping ("/abgelehnt/{id}")
    public ResponseEntity <Long> abgelehnteFilmeinladung (@PathVariable Long id) {
        return this.filmeinladungSendenService.loescheFilmeinladung(id);
    }

}
