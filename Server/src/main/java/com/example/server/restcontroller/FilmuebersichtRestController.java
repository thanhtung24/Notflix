package com.example.server.restcontroller;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.repository.FilmRepository;
import com.example.server.service.FilmuebersichtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/filmUebersicht")
public class FilmuebersichtRestController {

    @Autowired
    FilmuebersichtService filmuebersichtService;

    @GetMapping("/getCast")
    ResponseEntity<List<PersonEntity>> getPersonenByFilmId(@RequestParam Long filmId) {
        return new ResponseEntity<>(this.filmuebersichtService.getPersonenByFilmId(filmId), HttpStatus.OK);
    }

    @GetMapping("/getPerson/{personId}")
    ResponseEntity<PersonEntity> getPersonById(@PathVariable Long personId) {
        return new ResponseEntity<>(this.filmuebersichtService.getPersonById(personId), HttpStatus.OK);
    }
}
