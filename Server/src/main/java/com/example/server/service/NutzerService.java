package com.example.server.service;

import com.example.server.entities.*;
import com.example.server.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
@AllArgsConstructor

public class NutzerService {

    private NutzerRepository nutzerRepository;

    private PersonRepository personRepository;

    private FreundschaftRepository freundschaftRepository;

    private WatchListRepository watchListRepository;

    private FilmRepository filmRepository;

    private BereitsGesehenRepository bereitsGesehenRepository;

    public List<NutzerEntity> getNutzer(){

        return nutzerRepository.findAll();
    }

    public List<NutzerEntity> findeNutzerMitEmail(String email){
        return nutzerRepository.findNutzerByEmail(email);
    }

    public NutzerEntity findeNutzerAnhandId(Long id){
        return nutzerRepository.findNutzerEntityById(id);
    }


   public ResponseEntity<NutzerEntity> registriereNutzer(NutzerEntity nutzer){


        List<NutzerEntity> nutzerliste = findeNutzerMitEmail(nutzer.getEmail());

        if(nutzerliste.isEmpty()){
            nutzerRepository.save(nutzer);
            System.out.println("Nutzerliste ist leer");
            return new ResponseEntity<>(nutzer, HttpStatus.OK);
        }
        else{
            System.out.println("Nutzerliste ist nicht leer");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

        }

   }

   public ResponseEntity<NutzerEntity> loginNutzer(NutzerEntity nutzer){
        List<NutzerEntity> nutzerListe= findeNutzerMitEmail(nutzer.getEmail());
        if(nutzerListe.isEmpty()){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        NutzerEntity nutzerReturn= nutzerListe.get(0);

        if(nutzer.getPasswort().equals(nutzerReturn.getPasswort())){
            return new ResponseEntity<>(nutzerReturn, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }

   }

   public List<NutzerEntity> listeVonFreunden(Long id){
        List<FreundschaftEntity> freundeVon = freundschaftRepository.findAllByNutzerId(id);
        List<NutzerEntity> returnWert = new ArrayList<>();
        for(FreundschaftEntity freunde: freundeVon){
            NutzerEntity nutzer = nutzerRepository.findNutzerEntityById(freunde.getFreundId());
            returnWert.add(nutzer);
        }
        return returnWert;
   }

   public ResponseEntity<List<FilmEntity>> watchlistAbrufen(Long nutzerId){

       List<WatchListEntity> listeWatchlistentity= this.watchListRepository.findAllByNutzerId(nutzerId);
       List<Long> filmids = new ArrayList<>();
       List <FilmEntity> watchlist;

       for (WatchListEntity watchListEntity: listeWatchlistentity){
           filmids.add(watchListEntity.getFilmId());
       }
       watchlist= this.filmRepository.findAllById(filmids);

       if(watchlist != null) {
           return new ResponseEntity<>(watchlist, HttpStatus.OK);
       } else return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);

   }

   public ResponseEntity<List<FilmEntity>> bereitsGesehenAbrufen(Long nutzerId){
        List<Long> filmIds = this.bereitsGesehenRepository.findAllByNutzerId(nutzerId).stream().map(bereitsGesehenEntity -> bereitsGesehenEntity.getFilmId()).collect(Collectors.toList());
        List<FilmEntity> bereitsGesehen = this.filmRepository.findAllById(filmIds);

        if(bereitsGesehen != null){
            return new ResponseEntity<>(bereitsGesehen, HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
   }

   public ResponseEntity<NutzerEntity> profilBearbeiten(Long nutzerId, String vorname, String nachname, String geburtsdatum, String email){

        NutzerEntity nutzer = this.nutzerRepository.findNutzerEntityById(nutzerId);

        try {
            nutzer.setVorname(vorname);
            nutzer.setNachname(nachname);
            nutzer.setGeburtsdatum(geburtsdatum);
            nutzer.setEmail(email);

            this.nutzerRepository.save(nutzer);

            return new ResponseEntity<>(nutzer, HttpStatus.OK);
        }
        catch(Exception e){
            System.out.println("Es ist ein Fehler bei der Bearbeitung des Profils des Nutzers " + nutzer.getVorname() + " " + nutzer.getNachname()+ " aufgetreten");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }


   }
   public ResponseEntity<NutzerEntity> profildatenUndProfilbildBearbeiten(Long nutzerId, String vorname, String nachname, String geburtsdatum, String email, String profilbild){
        NutzerEntity nutzer= nutzerRepository.findNutzerEntityById(nutzerId);

        try{
            nutzer.setVorname(vorname);
            nutzer.setNachname(nachname);
            nutzer.setEmail(email);
            nutzer.setGeburtsdatum(geburtsdatum);
            nutzer.setProfilbild(Base64.getDecoder().decode(profilbild));


            this.nutzerRepository.save(nutzer);

            return new ResponseEntity<>(nutzer, HttpStatus.OK);
        } catch (Exception e){
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
   }





}
