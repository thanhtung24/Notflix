package com.example.server.restcontroller;

import com.example.server.entities.NutzerEntity;
import com.example.server.service.NutzersucheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/nutzersuche")
public class NutzersucheRestcontroller {

    @Autowired
    private NutzersucheService nutzersucheService;

    @GetMapping("/alleNutzer")
    ResponseEntity<List<NutzerEntity>> getAlleNutzer() {
        return this.nutzersucheService.getAlleNutzer();
    }

    @GetMapping("/alleFreunde/{nutzerId}")
    ResponseEntity<List<NutzerEntity>> getAlleFreunde(@PathVariable Long nutzerId) {
        return this.nutzersucheService.getAlleFreunde(nutzerId);
    }
}
