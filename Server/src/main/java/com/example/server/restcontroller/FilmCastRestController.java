package com.example.server.restcontroller;

import com.example.server.entities.FilmCastEntity;
import com.example.server.service.FilmCastService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/filmCast")
public class FilmCastRestController {

    @Autowired
    FilmCastService filmCastService;

    @PostMapping("/filmCastEntitesAnlegen")
    ResponseEntity<FilmCastEntity> filmCastAnlegen(@RequestBody FilmCastEntity filmCastEntity) {
        return this.filmCastService.filmCastAnlegen(filmCastEntity);
    }

    @DeleteMapping("/filmCastLoeschen")
    ResponseEntity<Long> deleteFilmCastByFilmId(@RequestParam Long filmId) {
        return this.filmCastService.deleteFilmCastByFilmId(filmId);
    }
}
