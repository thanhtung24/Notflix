package com.example.server.service;

import com.example.server.entities.FilmbewertungEntity;
import com.example.server.repository.FilmbewertungRepository;
import com.example.server.repository.FreundschaftRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Component
@Service
@Transactional
public class FilmbewertungService {

    @Autowired
    private FilmbewertungRepository filmbewertungRepository;

    @Autowired
    private FreundschaftRepository freundschaftRepository;



    public ResponseEntity<FilmbewertungEntity> absenden(FilmbewertungEntity filmbewertungEntity){
        filmbewertungRepository.save(filmbewertungEntity);
        return new ResponseEntity<>(filmbewertungEntity, HttpStatus.OK);
    }

    private Date datum;
    public ResponseEntity<FilmbewertungEntity> bewertungBearbeiten(Long id, Long filmId, Long nutzerId, String vorname, String nachname, String sterne, String kommentar){
        FilmbewertungEntity filmbewertungEntity = filmbewertungRepository.getById(id);
        try{
            filmbewertungEntity.setSterne(sterne);
            filmbewertungEntity.setKommentar(kommentar);

            datum= new Date();
            filmbewertungEntity.setDatum(datum);

            this.filmbewertungRepository.save(filmbewertungEntity);
            return new ResponseEntity<>(filmbewertungEntity, HttpStatus.OK);

        } catch(Exception e){
            System.out.println("Fehler beim Bearbeiten der Bewertung.");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    public List<FilmbewertungEntity> getBewertungenByFilmId(Long filmId){ return this.filmbewertungRepository.findAllByFilmId(filmId);}

    public FilmbewertungEntity getBewertungByFilmIdAndNutzerId(Long filmId, Long nutzerId){ return this.filmbewertungRepository.findByFilmIdAndNutzerId(filmId, nutzerId);}

    public ResponseEntity<Boolean> freundschaftExistiert(Long nutzerId, Long freundId) {
        if(this.freundschaftRepository.findAllByNutzerIdAndFreundId(nutzerId, freundId).isEmpty()) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }




}
