package com.example.server.service;

import com.example.server.entities.BereitsGesehenEntity;
import com.example.server.entities.FilmStatistikEntity;
import com.example.server.entities.FilmbewertungEntity;
import com.example.server.repository.BereitsGesehenRepository;
import com.example.server.repository.FilmStatistikRepository;
import com.example.server.repository.FilmbewertungRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
//@AllArgsConstructor

public class FilmStatistikService {

    @Autowired
    private FilmbewertungRepository filmbewertungRepository;

    @Autowired
    private BereitsGesehenRepository bereitsGesehenRepository;

    @Autowired
    private FilmStatistikRepository filmStatistikRepository;


    int anzahlBewertung=0;
    int anzahlGesehen=0;

    int einStern=0;
    int zweiSterne=0;
    int dreiSterne=0;
    int vierSterne=0;
    int fuenfSterne=0;

    public ResponseEntity<HashMap<String, Integer>> getAnzahl(Long filmId){

        List<FilmbewertungEntity> filmbewertungEntityList= this.filmbewertungRepository.findAllByFilmId(filmId);
        List<BereitsGesehenEntity> bereitsGesehenEntityList= this.bereitsGesehenRepository.findByFilmId(filmId);

        FilmStatistikEntity filmStatistik= this.filmStatistikRepository.findByfilmId(filmId);
        Date datumFilmStatistik= filmStatistik.getDatum();


       anzahlBewertung = filmbewertungEntityList.stream().filter(FilmbewertungEntity -> FilmbewertungEntity.getDatum().after(datumFilmStatistik)).collect(Collectors.toList()).size();

       anzahlGesehen= bereitsGesehenEntityList.stream().filter(BereitsGesehenEntity-> BereitsGesehenEntity.getDatum().after(datumFilmStatistik)).collect(Collectors.toList()).size();


        einStern=this.getAnzahlSterne(filmId, "1 Stern");
        zweiSterne=this.getAnzahlSterne(filmId, "2 Sterne");
        dreiSterne= this.getAnzahlSterne(filmId, "3 Sterne");
        vierSterne=this.getAnzahlSterne(filmId, "4 Sterne");
        fuenfSterne=this.getAnzahlSterne(filmId, "5 Sterne");


        HashMap<String, Integer> hashMap= new HashMap<>();

        hashMap.put("Anzahl Gesehen", anzahlGesehen);
        hashMap.put("Anzahl Bewertung", anzahlBewertung);
        hashMap.put("Anzahl 1 Stern", einStern);
        hashMap.put("Anzahl 2 Sterne", zweiSterne);
        hashMap.put("Anzahl 3 Sterne", dreiSterne);
        hashMap.put("Anzahl 4 Sterne", vierSterne);
        hashMap.put("Anzahl 5 Sterne", fuenfSterne);

        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public int getAnzahlSterne(Long filmId, String string){
        List<FilmbewertungEntity> listeFilmbewertungsEntity= this.filmbewertungRepository.findAllByFilmId(filmId);

        FilmStatistikEntity filmStatistik=this.filmStatistikRepository.findByfilmId(filmId);
        Date datumFilmStatistik=filmStatistik.getDatum();


           int sterne = listeFilmbewertungsEntity.stream().filter(FilmbewertungEntity -> FilmbewertungEntity.getSterne().equals(string) && FilmbewertungEntity.getDatum().after(datumFilmStatistik)).collect(Collectors.toList()).size();


        return sterne;
    }


    public ResponseEntity<Long> zuruecksetzen(Long filmId){

        FilmStatistikEntity filmStatistikEntity= this.filmStatistikRepository.findByfilmId(filmId);

        filmStatistikEntity.setDatum(new Date());
        this.filmStatistikRepository.save(filmStatistikEntity);


        return new ResponseEntity<>(filmStatistikEntity.getFilmId(), HttpStatus.OK);
    }

    public ResponseEntity<String> getDurchschnitt(){

        String ergebnis;

        double zwischenergebnis=0;

        double anzahlBewertung=this.anzahlBewertung;

        zwischenergebnis= ((einStern*1)+ (zweiSterne*2) + (dreiSterne*3)+ (vierSterne*4)+ (fuenfSterne*5)) / anzahlBewertung;


        zwischenergebnis= Math.round((zwischenergebnis*10)) / 10.00;


        ergebnis = zwischenergebnis +"";

        return new ResponseEntity<>(ergebnis, HttpStatus.OK);

    }

}
