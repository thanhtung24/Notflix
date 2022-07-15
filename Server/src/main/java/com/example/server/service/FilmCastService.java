package com.example.server.service;

import com.example.server.entities.FilmCastEntity;
import com.example.server.entities.FilmEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.repository.FilmCastRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@Service
@Transactional
public class FilmCastService {

    @Autowired
    private FilmCastRepository filmCastRepository;

    public ResponseEntity<FilmCastEntity> filmCastAnlegen(FilmCastEntity filmCastEntity) {
        this.filmCastRepository.save(filmCastEntity);
        return new ResponseEntity<>(filmCastEntity, HttpStatus.OK);

    }

    public ResponseEntity<Long> deleteFilmCastByFilmId(Long filmId) {
        this.filmCastRepository.deleteAllByFilmId(filmId);
        if(this.filmCastRepository.findAllByFilmId(filmId).isEmpty()) {
            return new ResponseEntity<>(filmId, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
