package com.example.server.service;

import com.example.server.entities.*;
import com.example.server.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
@Transactional
public class ProfilAndererNutzerService {

    @Autowired
    private FreundschaftRepository freundschaftRepository;

    @Autowired
    private NutzerRepository nutzerRepository;

    @Autowired
    private FreundschaftsanfrageRepository freundschaftsanfrageRepository;

    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private BereitsGesehenRepository bereitsGesehenRepository;

    public ResponseEntity<List<NutzerEntity>> getAlleFreunde(Long nutzerId) {
        List<Long> freundeIds = this.freundschaftRepository.getAllByNutzerId(nutzerId).stream().map(freundschaftEntity -> freundschaftEntity.getFreundId()).collect(Collectors.toList());
        List<NutzerEntity> freunde = this.nutzerRepository.findAllById(freundeIds);

        if(freunde != null) {
            return new ResponseEntity<>(freunde, HttpStatus.OK);
        }
        return new ResponseEntity<>(freunde, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<FreundschaftEntity>> akzeptiereAnfrage(List<FreundschaftEntity> freundschaftEntities) {
        boolean goodResponse = true;

        // for Schleife verhindert doppelte Datensätze beim gleichzeitigen Akzeptieren von Freundschaftsanfragen von beiden Nutzern
        for (FreundschaftEntity freundschaft: freundschaftEntities) {
            if(this.freundschaftRepository.findAllByNutzerIdAndFreundId(freundschaft.getNutzerId(), freundschaft.getFreundId()).isEmpty()) {
                this.freundschaftRepository.save(freundschaft);
            }
        }

        for(FreundschaftEntity freundschaft: freundschaftEntities) {
            // lösche Freundschaftsanfrage
            List<FreundschaftsanfrageEntity> freundschaftsanfrage = this.freundschaftsanfrageRepository.findAllByAnfrageSenderIdAndNutzerId(freundschaft.getNutzerId(), freundschaft.getFreundId());
            if(!freundschaftsanfrage.isEmpty()) {
                this.freundschaftsanfrageRepository.deleteAll(freundschaftsanfrage);
            }

            //lösche Freundschaftsanfrage vom anderen Nutzer
            List<FreundschaftsanfrageEntity> freundschaftsanfrageVomAnderenNutzer = this.freundschaftsanfrageRepository.findAllByAnfrageSenderIdAndNutzerId(freundschaft.getFreundId(), freundschaft.getNutzerId());
            if(!freundschaftsanfrageVomAnderenNutzer.isEmpty()) {
                this.freundschaftsanfrageRepository.deleteAll(freundschaftsanfrageVomAnderenNutzer);
            }

            // überprüft, ob die Freundschaftsanfrage gelöscht wurde
            if(!this.freundschaftsanfrageRepository.findAllByAnfrageSenderIdAndNutzerId(freundschaft.getNutzerId(), freundschaft.getFreundId()).isEmpty()
            || !this.freundschaftsanfrageRepository.findAllByAnfrageSenderIdAndNutzerId(freundschaft.getFreundId(), freundschaft.getNutzerId()).isEmpty()) {
                goodResponse = false;
            }

            // überprüft, ob die Freundschaft existiert
            if(this.freundschaftRepository.findAllByNutzerIdAndFreundId(freundschaft.getNutzerId(), freundschaft.getFreundId()).isEmpty()
              || this.freundschaftRepository.findAllByNutzerIdAndFreundId(freundschaft.getFreundId(), freundschaft.getNutzerId()).isEmpty()) {
                goodResponse = false;
            }
        }

        if(goodResponse) {
            return new ResponseEntity<>(freundschaftEntities, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Boolean> freundschaftExistiert(Long nutzerId, Long freundId) {
        if(this.freundschaftRepository.findAllByNutzerIdAndFreundId(nutzerId, freundId).isEmpty()) {
            return new ResponseEntity<>(false, HttpStatus.OK);
        }
        return new ResponseEntity<>(true, HttpStatus.OK);
    }

    public ResponseEntity<List<FilmEntity>> getWatchlist(Long nutzerId) {
        List<Long> filmIds = this.watchListRepository.findAllByNutzerId(nutzerId).stream().map(watchListEntity -> watchListEntity.getFilmId()).collect(Collectors.toList());
        List<FilmEntity> watchlist = this.filmRepository.findAllById(filmIds);

        if(watchlist != null) {
            return new ResponseEntity<>(watchlist, HttpStatus.OK);
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<List<FilmEntity>> getlisteBereitsGesehenerFilme(Long nutzerId) {
        List<Long> filmIds = this.bereitsGesehenRepository.findAllByNutzerId(nutzerId).stream().map(watchListEntity -> watchListEntity.getFilmId()).collect(Collectors.toList());
        List<FilmEntity> listeBereitsGesehenerFilme = this.filmRepository.findAllById(filmIds);

        if(listeBereitsGesehenerFilme != null) {
            return new ResponseEntity<>(listeBereitsGesehenerFilme, HttpStatus.OK);
        } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
