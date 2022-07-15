package com.example.server.restcontroller;

import com.example.server.entities.WatchListEntity;
import com.example.server.repository.WatchListRepository;
import com.example.server.service.WatchListService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/watchList")
public class WatchListRestController {

    @Autowired
    private WatchListService watchListService;

    @Autowired
    private WatchListRepository watchListRepository;

    @PostMapping("/filmZuWatchListAnlegen")
    public ResponseEntity<WatchListEntity> filmZuWatchListAnlegen(@RequestBody WatchListEntity watchListEntity){
        return this.watchListService.filmZuWatchListAnlegen(watchListEntity);
    }

    @DeleteMapping("/filmVonWatchListEntfernen/{id}")
    public ResponseEntity<Long> filmVonWatchListEntfernen(@PathVariable Long id){
        System.out.println("Film gelöscht");
        return this.watchListService.filmVonWatchListEntfernen(id);
    }

    @GetMapping("/watchlistAbrufen/{filmId}/{nutzerId}")
    public ResponseEntity<WatchListEntity> getWatchListItemByFilmIdAndNutzerId(@PathVariable Long filmId, @PathVariable Long nutzerId){
        return this.watchListService.getWatchListItemByFilmIdAndNutzerId(filmId, nutzerId);
    }
}
