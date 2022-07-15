package com.example.server.restcontroller;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.service.FilmStatistikService;
import com.example.server.service.NutzerverhaltenStatistikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/nutzerverhaltenstatistik")

public class NutzerverhaltenStatistikRestController {

    @Autowired
    private NutzerverhaltenStatistikService nutzerverhaltenStatistikService;

    @GetMapping("/getAnzahlKategorie/{nutzerId}/{startDatum}/{endDatum}")
    public ResponseEntity<HashMap<String, Integer>> getAnzahlKategorie(@PathVariable("nutzerId") Long nutzerId, @PathVariable("startDatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date  startDatum, @PathVariable("endDatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDatum){
        return this.nutzerverhaltenStatistikService.getAnzahlKategorie(nutzerId, startDatum, endDatum);
    }

    @GetMapping("/getGeschauteSchauspieler/{nutzerId}/{startDatum}/{endDatum}")
    public ResponseEntity<List<PersonEntity>> getGeschauteSchauspieler(@PathVariable("nutzerId") Long nutzerId, @PathVariable ("startDatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDatum, @PathVariable ("endDatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDatum){
        return this.nutzerverhaltenStatistikService.getGeschauteSchauspieler(nutzerId, startDatum, endDatum);
    }

    @GetMapping("/getLieblingsfilme/{nutzerId}/{startDatum}/{endDatum}")
    public ResponseEntity<List<FilmEntity>> getLieblingsfilme(@PathVariable("nutzerId") Long nutzerId, @PathVariable("startDatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDatum, @PathVariable("endDatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date endDatum){
        return this.nutzerverhaltenStatistikService.getLieblingsfilme(nutzerId, startDatum, endDatum);
    }

    @GetMapping("/getGesamtzeitGeschauteFilme/{nutzerId}/{startDatum}/{endDatum}")
    ResponseEntity<Integer> getGesamtzeitGeschauteFilme(@PathVariable("nutzerId") Long nutzerId, @PathVariable("startDatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date startDatum, @PathVariable("endDatum") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)  Date endDatum){
        return this.nutzerverhaltenStatistikService.getGesamtzeitGeschauteFilme(nutzerId, startDatum, endDatum);
    }

}
