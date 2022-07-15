package com.example.server.restcontroller;

import com.example.server.entities.FilmbewertungEntity;
import com.example.server.service.FilmbewertungService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Filmbewertungen")
public class FilmbewertungRestController {

    @Autowired
    FilmbewertungService filmbewertungService;

    @PostMapping("/absenden")
    public ResponseEntity<FilmbewertungEntity> absenden(@RequestBody FilmbewertungEntity filmbewertungEntity){
        int code = filmbewertungService.absenden(filmbewertungEntity).getStatusCode().value();
        if(code == 200){
            return new ResponseEntity<>(filmbewertungEntity, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/bewertungBearbeiten")
    public ResponseEntity<FilmbewertungEntity> bewertungBearbeiten(@RequestParam("bewertungId") Long bewertungId,
                                                                   @RequestParam("filmId") Long filmId,
                                                                   @RequestParam("nutzerId") Long nutzerId,
                                                                   @RequestParam("vorname") String vorname,
                                                                   @RequestParam("nachname") String nachname,
                                                                   @RequestParam("neueSterne") String sterne,
                                                                   @RequestParam("neuerKommentar") String kommentar)
                                                                   {
        return this.filmbewertungService.bewertungBearbeiten(bewertungId, filmId, nutzerId, vorname, nachname, sterne, kommentar);
    }

    @GetMapping("/getInformationToTable")
    ResponseEntity<List<FilmbewertungEntity>> getBewertungenByFilmId(@RequestParam("filmId") Long filmId){
        return new ResponseEntity<>(this.filmbewertungService.getBewertungenByFilmId(filmId), HttpStatus.OK);
    }

    @GetMapping("/getBewertungByFilmIdAndNutzerId")
    ResponseEntity<FilmbewertungEntity> getBewertungByFilmIdAndNutzerId(@RequestParam("filmId") Long filmId, @RequestParam("nutzerId") Long nutzerId){
        if(this.filmbewertungService.getBewertungByFilmIdAndNutzerId(filmId, nutzerId) != null) {
            return new ResponseEntity<>(this.filmbewertungService.getBewertungByFilmIdAndNutzerId(filmId, nutzerId), HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    @GetMapping("/freundschaftExistiert")
    ResponseEntity<Boolean> freundschaftExistiert(@RequestParam("nutzerId") Long nutzerId, @RequestParam("freundId") Long freundId) {
        return this.filmbewertungService.freundschaftExistiert(nutzerId, freundId);
    }





}

