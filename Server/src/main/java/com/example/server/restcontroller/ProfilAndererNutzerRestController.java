package com.example.server.restcontroller;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.FreundschaftEntity;
import com.example.server.entities.NutzerEntity;
import com.example.server.service.ProfilAndererNutzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profilAndererNutzer")
public class ProfilAndererNutzerRestController {

    @Autowired
    private ProfilAndererNutzerService profilAndererNutzerService;

    @GetMapping("/alleFreunde/{nutzerId}")
    ResponseEntity<List<NutzerEntity>> getAlleFreunde(@PathVariable Long nutzerId) {
        return this.profilAndererNutzerService.getAlleFreunde(nutzerId);
    }

    @PostMapping("/akzeptiereAnfrage")
    ResponseEntity<List<FreundschaftEntity>> akzeptiereAnfrage(@RequestBody List<FreundschaftEntity> freundschaftEntities) {
        return this.profilAndererNutzerService.akzeptiereAnfrage(freundschaftEntities);
    }

    @GetMapping("/freundschaftExistiert")
    ResponseEntity<Boolean> freundschaftExistiert(@RequestParam("nutzerId") Long nutzerId, @RequestParam("freundId") Long freundId) {
        return this.profilAndererNutzerService.freundschaftExistiert(nutzerId, freundId);
    }

    @GetMapping("/getWatchlist/{nutzerId}")
    ResponseEntity<List<FilmEntity>> getWatchlist(@PathVariable("nutzerId") Long nutzerId) {
        return this.profilAndererNutzerService.getWatchlist(nutzerId);
    }

    @GetMapping("/getlisteBereitsGesehenerFilme/{nutzerId}")
    ResponseEntity<List<FilmEntity>> getlisteBereitsGesehenerFilme(@PathVariable("nutzerId") Long nutzerId) {
        return this.profilAndererNutzerService.getlisteBereitsGesehenerFilme(nutzerId);
    }
}
