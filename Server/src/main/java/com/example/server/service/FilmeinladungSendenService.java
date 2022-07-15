package com.example.server.service;

import com.example.server.entities.FilmeinladungEntity;
import com.example.server.entities.FreundschaftsanfrageEntity;
import com.example.server.entities.NutzerEntity;
import com.example.server.repository.FilmeinladungRepository;
import com.example.server.repository.NutzerRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
@Transactional

public class FilmeinladungSendenService {


    @Autowired
    private FilmeinladungRepository filmeinladungRepository;

    @Autowired
    private NutzerRepository nutzerRepository;


    public ResponseEntity <FilmeinladungEntity> saveFilmeinladung(FilmeinladungEntity filmeinladung) {
        this.filmeinladungRepository.save(filmeinladung);
        if (this.filmeinladungRepository.getById(filmeinladung.getId()) != null) {
            return new ResponseEntity<>(filmeinladung, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity <List<FilmeinladungEntity>> getAlleFilmeinladungenWhereAkzeptiert (Long einladungsenderId){

        List <FilmeinladungEntity> alle = this.filmeinladungRepository.findAll();
        List <FilmeinladungEntity> gefiltert = new ArrayList<>();

        for (FilmeinladungEntity filmeinladungsEntity : alle){
            if (filmeinladungsEntity.getEinladungssenderId().equals(einladungsenderId) && filmeinladungsEntity.isAkzeptiert()){
                gefiltert.add(filmeinladungsEntity);
            }
        }

        if (gefiltert!=null){
            return new ResponseEntity<>(gefiltert, HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity <List<FilmeinladungEntity>> getAlleFilmeinladungenFromPerson (Long einladungsempfaengerId){
        List <FilmeinladungEntity> alle =this.filmeinladungRepository.findAll();
        List <FilmeinladungEntity> gefiltert=new ArrayList<>();

        for (FilmeinladungEntity filmeinladungEntity:alle){
            if (filmeinladungEntity.getEinladungsempfaengerId().equals(einladungsempfaengerId)){
                gefiltert.add(filmeinladungEntity);
            }
        }
        if(gefiltert != null) {
            return new ResponseEntity<>(gefiltert, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }


    public ResponseEntity <FilmeinladungEntity> bearbeiteReportAkzeptiert (Long filmeinladungsId){
        FilmeinladungEntity filmeinladung = this.filmeinladungRepository.getById(filmeinladungsId);

        try {
            filmeinladung.setAkzeptiert(true);
            this.filmeinladungRepository.save(filmeinladung);
            return new ResponseEntity <> (filmeinladung, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity <FilmeinladungEntity> bearbeiteReportGesehen(Long filmeinladungsId){
        FilmeinladungEntity filmeinladung = this.filmeinladungRepository.getById(filmeinladungsId);

        try {
            filmeinladung.setGesehen(true);
            this.filmeinladungRepository.save(filmeinladung);
            return new ResponseEntity <> (filmeinladung, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity <Long> loescheFilmeinladung (Long id) {
        this.filmeinladungRepository.deleteFilmeinladungEntityById(id);

        if (this.filmeinladungRepository.findById(id).isEmpty()){
            return new ResponseEntity<>(id, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

}
