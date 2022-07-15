package com.example.server.restcontroller;

import com.example.server.entities.FilmbewertungEntity;
import com.example.server.entities.NotizEntity;
import com.example.server.service.NotizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/Kalender")
public class NotizRestController {

    @Autowired
    NotizService notizService;

    @PostMapping("/notizSpeichern")
    public ResponseEntity<NotizEntity> speichern(@RequestBody NotizEntity notizEntity){
        int code = notizService.speichern(notizEntity).getStatusCode().value();
        if(code == 200){
            return new ResponseEntity<>(notizEntity, HttpStatus.OK);
        } else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/getNotizenByNutzerId")
    ResponseEntity<List<NotizEntity>> getNotizenByNutzerId(@RequestParam("nutzerId") Long nutzerId){
        return new ResponseEntity<>(this.notizService.getNotizenByNutzerId(nutzerId), HttpStatus.OK);
    }
}
