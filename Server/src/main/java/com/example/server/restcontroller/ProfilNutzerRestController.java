package com.example.server.restcontroller;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.NutzerEntity;
import com.example.server.service.NutzerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/profil")
public class ProfilNutzerRestController {

    @Autowired
    private NutzerService nutzerService;

    @GetMapping("/meineFreunde{id}")
    public ResponseEntity<List<NutzerEntity>> meineFreunde(@PathVariable(value = "id") Long id){
        return new ResponseEntity<>(nutzerService.listeVonFreunden(id), HttpStatus.OK);
    }


   @GetMapping("/bereitsGesehenAbrufen/{nutzerId}")
   public ResponseEntity<List<FilmEntity>> bereitsGesehenAbrufen(@PathVariable("nutzerId") Long nutzerId){
        return this.nutzerService.bereitsGesehenAbrufen(nutzerId);
   }

    @GetMapping("/watchlistAbrufen/{nutzerId}")
    public ResponseEntity<List<FilmEntity>> watchlistAbrufen(@PathVariable("nutzerId") Long nutzerId){
        return this.nutzerService.watchlistAbrufen(nutzerId);
    }

    @PutMapping("/profildatenBearbeiten")
    public ResponseEntity<NutzerEntity> profilBearbeiten (@RequestParam ("nutzerId") Long nutzerId, @RequestParam ("vorname") String vorname,
    @RequestParam("nachname") String nachname, @RequestParam ("geburtsdatum") String geburtsdatum,
    @RequestParam("email") String email){
        return this.nutzerService.profilBearbeiten(nutzerId,vorname,nachname,geburtsdatum,email);
    }


    @PutMapping("/profildatenUndProfilbildBearbeiten")
    public ResponseEntity<NutzerEntity> profildatenUndProfilbildBearbeiten (@RequestParam ("nutzerId") Long nutzerId,
                                                                            @RequestParam ("vorname") String vorname,
                                                                            @RequestParam("nachname") String nachname,
                                                                            @RequestParam("geburtsdatum") String geburtsdatum,
                                                                            @RequestParam("email") String email,
                                                                            @RequestParam("profilbild") String profilbild){
    return this.nutzerService.profildatenUndProfilbildBearbeiten(nutzerId,vorname, nachname, geburtsdatum, email, profilbild);
    }


}
