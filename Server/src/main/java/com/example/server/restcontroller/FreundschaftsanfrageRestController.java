package com.example.server.restcontroller;

import com.example.server.entities.FreundschaftsanfrageEntity;
import com.example.server.entities.NutzerEntity;
import com.example.server.service.FreundschaftsanfrageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/freundschaftsanfragen")
public class FreundschaftsanfrageRestController {

    @Autowired
    private FreundschaftsanfrageService freundschaftsanfrageService;

    @GetMapping("/alleFreundschaftsanfragen/{nutzerId}")
    ResponseEntity<List<NutzerEntity>> getAlleFreundschaftsanfragen(@PathVariable Long nutzerId) {
        return this.freundschaftsanfrageService.getAlleFreundschaftsanfragen(nutzerId);
    }

    @PostMapping("/sendeFreundschaftsanfrage")
    ResponseEntity<FreundschaftsanfrageEntity> sendeFreundschaftsanfrage(@RequestBody FreundschaftsanfrageEntity anfrage) {
        return this.freundschaftsanfrageService.sendeFreundschaftsanfrage(anfrage);
    }

    @GetMapping("/anfrageExistiert")
    ResponseEntity<Boolean> anfrageExistiert(@RequestParam Long anfrageSenderId, @RequestParam Long nutzerId) {
        return this.freundschaftsanfrageService.anfrageExistiert(anfrageSenderId, nutzerId);
    }
}
