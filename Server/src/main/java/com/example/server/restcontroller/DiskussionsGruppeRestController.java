package com.example.server.restcontroller;

import com.example.server.entities.*;
import com.example.server.service.DiskussionsGruppeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/diskussionsgruppen")
public class DiskussionsGruppeRestController {

    @Autowired
    private DiskussionsGruppeService diskussionsGruppeService;

    @PostMapping("/erstellen")
    public ResponseEntity<DiskussionsGruppeEntity> diskussionsGruppeErstellen(@RequestBody DiskussionsGruppeEntity diskussionsGruppeEntity){
        diskussionsGruppeService.gruppeAnlegen(diskussionsGruppeEntity);
        return new ResponseEntity<>(diskussionsGruppeEntity, HttpStatus.OK);
    }

    @GetMapping("/alleGruppen")
    public ResponseEntity<List<DiskussionsGruppeEntity>> alleDiskussionsGruppe(){
        List<DiskussionsGruppeEntity> gruppen = diskussionsGruppeService.alleGruppen();
        return new ResponseEntity<>(gruppen, HttpStatus.OK);
    }

    @PostMapping("/nachrichtSpeichern")
    public ResponseEntity<DiskussionsGruppeChatNachrichtEntity> nachrichtSpeichern(@RequestBody DiskussionsGruppeChatNachrichtEntity nachricht){
        diskussionsGruppeService.nachrichtSpeichern(nachricht);
        return new ResponseEntity<>(nachricht, HttpStatus.OK);
    }

    @GetMapping("/chatVerlauf/{id}")
    public ResponseEntity<List<DiskussionsGruppeChatNachrichtEntity>> chatVerlaufVonGruppe(@PathVariable(value = "id") Long id){
        List<DiskussionsGruppeChatNachrichtEntity> liste = diskussionsGruppeService.chatVerlauf(id);
        return new ResponseEntity<>(liste, HttpStatus.OK);
    }

    @PostMapping("/gruppeBeitreten")
    public ResponseEntity<DiskussionsGruppenMitgliedEntity> gruppenMitgliedHinzufuegen(@RequestBody DiskussionsGruppenMitgliedEntity mitglied){
        diskussionsGruppeService.gruppenMitgliedSpeichern(mitglied);
        return new ResponseEntity<>(mitglied, HttpStatus.OK);
    }

    @PutMapping("/mitgliedStatus/{nutzerid}/{gruppenid}")
    public ResponseEntity<Boolean> mitgliedStatus(@PathVariable (value = "nutzerid") Long nutzerid, @PathVariable(value = "gruppenid") Long gruppenid){
        boolean status = diskussionsGruppeService.mitgliedStatus(nutzerid, gruppenid);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @GetMapping("/meineGruppen/{id}")
    public ResponseEntity<List<DiskussionsGruppeEntity>> meineGruppen(@PathVariable(value = "id") Long id){
        List<DiskussionsGruppeEntity> meineGruppen = diskussionsGruppeService.meineGruppen(id);
        return new ResponseEntity<>(meineGruppen, HttpStatus.OK);
    }

    @PutMapping("/gruppeVerlassen/{gruppenId}/{nutzerId}")
    public ResponseEntity<Long> gruppeVerlassen(@PathVariable(value = "gruppenId") Long gruppenId, @PathVariable(value = "nutzerId") Long nutzerId){
        diskussionsGruppeService.gruppeVerlassen(gruppenId,nutzerId);
        return new ResponseEntity<>(gruppenId, HttpStatus.OK);
    }

    @GetMapping("/gruppenPrivacyEinstellung/{gruppenId}")
    public ResponseEntity<Boolean> gruppenPrivacyEinstellung(@PathVariable(value = "gruppenId") Long gruppenId){
        boolean istPrivat = diskussionsGruppeService.privacyStatus(gruppenId);
        return new ResponseEntity<>(istPrivat, HttpStatus.OK);
    }

    @PutMapping("/gruppeBearbeiten")
    public ResponseEntity<DiskussionsGruppeEntity> gruppeBearbeiten(@RequestParam("gruppenId") Long gruppenId, @RequestParam("istPrivat") Boolean istPrivat) {
        return new ResponseEntity<>(this.diskussionsGruppeService.gruppeBearbeiten(gruppenId, istPrivat), HttpStatus.OK);
    }

    @GetMapping("/gruppenMitglieder/{gruppenId}")
    public ResponseEntity<List<NutzerEntity>> alleGruppenMitglieder(@PathVariable(value = "gruppenId") Long gruppenId){
        return new ResponseEntity<>(this.diskussionsGruppeService.alleNutzerEinerGruppe(gruppenId), HttpStatus.OK);
    }
}

