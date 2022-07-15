package com.example.server.restcontroller;

import com.example.server.entities.FilmEntity;
import com.example.server.service.FilmlisteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/filmliste")
public class FilmlisteRestController {

    @Autowired
    FilmlisteService filmlisteService;

    @GetMapping("/alleFilmNamen")
    public List<FilmEntity> alleFilmNamen() {
        return filmlisteService.getAlleFilmNamen();
    }

}
