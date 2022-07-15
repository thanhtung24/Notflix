package com.example.server.restcontroller;

import com.example.server.entities.FilmEntity;
import com.example.server.java.FilmVorschlag;
import com.example.server.service.FilmVorschlagenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/filmVorschlagen")
public class FilmVorschlagenRestController {

    @Autowired
    private FilmVorschlagenService filmVorschlagenService;

    @GetMapping("/getFilme/{nutzerId}")
    ResponseEntity<List<FilmVorschlag>> getFilmVorschlaege(@PathVariable("nutzerId") Long nutzerId) {
        return this.filmVorschlagenService.schlageFilmeVor(nutzerId);
    }
}
