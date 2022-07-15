package com.example.server.service;

import com.example.server.entities.FilmCastEntity;
import com.example.server.entities.FilmEntity;
import com.example.server.entities.WatchListEntity;
import com.example.server.repository.FilmCastRepository;
import com.example.server.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Component
@Service
@Transactional
public class WatchListService {
    @Autowired
    private WatchListRepository watchListRepository;

    public ResponseEntity<WatchListEntity> getWatchListItemByFilmIdAndNutzerId(Long filmId, Long nutzerId) {
        try {
            return new ResponseEntity<>(watchListRepository.findAllByFilmIdAndNutzerId(filmId, nutzerId).get(0), HttpStatus.OK);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Kein WatchlistItem exisitiert mit den Ids");
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<WatchListEntity> filmZuWatchListAnlegen(WatchListEntity watchListEntity) {
        ResponseEntity<WatchListEntity> watchListResponse = getWatchListItemByFilmIdAndNutzerId(watchListEntity.getFilmId(), watchListEntity.getNutzerId());
        if(!watchListResponse.hasBody()){
            System.out.println("WatchListEntity angelegt");
            this.watchListRepository.save(watchListEntity);
        }
        return new ResponseEntity<>(watchListEntity, HttpStatus.OK);
    }

    public ResponseEntity<Long> filmVonWatchListEntfernen(Long id){
        this.watchListRepository.deleteWatchListEntityByFilmId(id);
        return new ResponseEntity<>(id, HttpStatus.OK);
        /*ResponseEntity<WatchListEntity> watchListResponse = getWatchListItemByFilmIdAndNutzerId(watchListEntity.getFilmId(), watchListEntity.getNutzerId());
        if(watchListResponse.hasBody()){
            this.watchListRepository.delete(watchListEntity);
        }
        return new ResponseEntity<>(watchListEntity, HttpStatus.OK);*/
    }
}
