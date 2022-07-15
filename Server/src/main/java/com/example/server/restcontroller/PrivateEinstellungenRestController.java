package com.example.server.restcontroller;

import com.example.server.entities.PrivateEinstellungenEntity;
import com.example.server.service.PrivateEinstellungenService;
import org.h2.command.dml.Call;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/privateEinstellungen")
public class PrivateEinstellungenRestController {

    @Autowired
    private PrivateEinstellungenService privateEinstellungenService;

    @PostMapping("/nutzerPrivateEinstellungen")
    ResponseEntity<PrivateEinstellungenEntity> nutzerStandardeinstellungenAnlegen(@RequestBody PrivateEinstellungenEntity privateEinstellungen) {
        return privateEinstellungenService.nutzerStandardeinstellungenAnlegen(privateEinstellungen);
    }

    @GetMapping("/getPrivateEinstellungen/{nutzerId}")
    ResponseEntity<PrivateEinstellungenEntity> getPrivateEinstellungen(@PathVariable("nutzerId") Long nutzerId) {
        return privateEinstellungenService.getPrivateEinstellungen(nutzerId);
    }

    @PutMapping("/speichereEinstellungen")
    ResponseEntity<PrivateEinstellungenEntity> speichereEinstellungen(@RequestParam("nutzerId") Long nutzerId,
                                                                      @RequestParam("watchListEinstg") String watchListEinstg,
                                                                      @RequestParam("geseheneFilmeListeEinstg") String geseheneFilmeListeEinstg,
                                                                      @RequestParam("freundeListeEinstg") String freundeListeEinstg,
                                                                      @RequestParam("bewertungenEinstg") String bewertungenEinstg) {
        return privateEinstellungenService.speichereEinstellungen(nutzerId, watchListEinstg, geseheneFilmeListeEinstg, freundeListeEinstg, bewertungenEinstg);
    }
}
