package com.example.server.restcontroller;


import com.example.server.service.FilmStatistikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/filmstatistik")

public class FilmStatistikRestController {

    @Autowired
    private FilmStatistikService filmStatistikService;

    @GetMapping("/getAnzahl/{filmId}")
    public ResponseEntity<HashMap<String, Integer>> getAnzahl(@PathVariable Long filmId){
        return this.filmStatistikService.getAnzahl(filmId);
    }

    @PutMapping ("/zuruecksetzen/{filmId}")
    public ResponseEntity<Long> zuruecksetzen(@RequestParam("filmId") Long filmId){
        return this.filmStatistikService.zuruecksetzen(filmId);
    }

    @GetMapping("/getDurchschnitt")
    public ResponseEntity<String> getDurchschnitt(){
        return this.filmStatistikService.getDurchschnitt();
    }
}
