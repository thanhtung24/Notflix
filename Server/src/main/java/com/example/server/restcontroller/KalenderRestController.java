package com.example.server.restcontroller;

import com.example.server.entities.BereitsGesehenEntity;
import com.example.server.entities.FilmbewertungEntity;
import com.example.server.entities.FilmeinladungEntity;
import com.example.server.entities.WatchListEntity;
import com.example.server.service.KalenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/Kalender")
public class KalenderRestController {

    @Autowired
    KalenderService kalenderService;

    @GetMapping("/getBewertungenByNutzerId")
    ResponseEntity<List<FilmbewertungEntity>> getBewertungenByNutzerId(@RequestParam("nutzerId") Long nutzerId){
        return new ResponseEntity<>(this.kalenderService.getBewertungenByNutzerId(nutzerId), HttpStatus.OK);
    }

    @GetMapping("/getWatchlistByNutzerId")
    ResponseEntity<List<WatchListEntity>> getWatchlistByNutzerId(@RequestParam("nutzerId") Long nutzerId){
        return new ResponseEntity<>(this.kalenderService.getWatchlistByNutzerId(nutzerId), HttpStatus.OK);
    }

    @GetMapping("/getBereitsGesehenByNutzerId")
    ResponseEntity<List<BereitsGesehenEntity>> getBereitsGesehenByNutzerId(@RequestParam("nutzerId") Long nutzerId){
        return new ResponseEntity<>(this.kalenderService.getBereitsGesehenByNutzerId(nutzerId), HttpStatus.OK);
    }

    @GetMapping("/getFilmeinladungenByNutzerId")
    ResponseEntity<List<FilmeinladungEntity>> getFilmeinladungenByNutzerId(@RequestParam("einladungsempfaengerId") Long einladungsempfaengerId){
        return new ResponseEntity<>(this.kalenderService.getFilmeinladungenByNutzerId(einladungsempfaengerId), HttpStatus.OK);
    }
}
