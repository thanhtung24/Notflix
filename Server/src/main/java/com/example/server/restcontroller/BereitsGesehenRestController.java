package com.example.server.restcontroller;

import com.example.server.entities.BereitsGesehenEntity;
import com.example.server.service.BereitsGesehenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Array;
import java.util.List;

@RestController
@RequestMapping("/bereitsGesehen")
public class BereitsGesehenRestController {

    @Autowired
    private BereitsGesehenService bereitsGesehenService;

    @PostMapping("/filmZuBereitsGesehenAnlegen")
    public ResponseEntity<BereitsGesehenEntity> filmZuBereitsGesehenAnlegen(@RequestBody BereitsGesehenEntity bereitsGesehenEntity){
        return this.bereitsGesehenService.filmZuBereitsGesehenAnlegen(bereitsGesehenEntity);
    }

    @GetMapping("/bereitsGesehenAbrufen/{filmId}/{nutzerId}")
    public ResponseEntity<BereitsGesehenEntity> getBereitsGesehenItemByFilmIdAndNutzerId(@PathVariable Long filmId, @PathVariable Long nutzerId){
        return this.bereitsGesehenService.getBereitsGesehenItemByFilmIdAndNutzerId(filmId, nutzerId);
    }




}
