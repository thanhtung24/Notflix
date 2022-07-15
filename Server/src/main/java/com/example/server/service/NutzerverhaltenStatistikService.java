package com.example.server.service;

import com.example.server.entities.*;
import com.example.server.repository.*;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Service
@AllArgsConstructor
public class NutzerverhaltenStatistikService {
    @Autowired
    private BereitsGesehenRepository bereitsGesehenRepository;

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private FilmCastRepository filmCastRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FilmbewertungRepository filmbewertungRepository;

    @Autowired
    private NutzerService nutzerService;
    public ResponseEntity<HashMap<String, Integer>> getAnzahlKategorie(Long nutzerId, Date startDatum, Date endDatum){
        int kategorieAction = this.getAnzahlKategorie(nutzerId, "Action", startDatum, endDatum);
        int kategorieComedy = this.getAnzahlKategorie(nutzerId, "Comedy", startDatum, endDatum);
        int kategorieCrime = this.getAnzahlKategorie(nutzerId, "Crime", startDatum, endDatum);
        int kategorieDrama = this.getAnzahlKategorie(nutzerId, "Drama", startDatum, endDatum);
        int kategorieFantasy = this.getAnzahlKategorie(nutzerId, "Fantasy", startDatum, endDatum);
        int kategorieHorror = this.getAnzahlKategorie(nutzerId, "Horror", startDatum, endDatum);
        int kategorieThriller = this.getAnzahlKategorie(nutzerId, "Thriller", startDatum, endDatum);

        HashMap<String, Integer> hashMap= new HashMap<>();

        hashMap.put("Anzahl Action", kategorieAction);
        hashMap.put("Anzahl Comedy", kategorieComedy);
        hashMap.put("Anzahl Crime", kategorieCrime);
        hashMap.put("Anzahl Drama", kategorieDrama);
        hashMap.put("Anzahl Fantasy", kategorieFantasy);
        hashMap.put("Anzahl Horror", kategorieHorror);
        hashMap.put("Anzahl Thriller", kategorieThriller);

        return new ResponseEntity<>(hashMap, HttpStatus.OK);
    }

    public int getAnzahlKategorie(Long nutzerId, String kategorieArt, Date startDatum, Date endDatum){
        ResponseEntity<List<FilmEntity>> filmEntityListe = nutzerService.bereitsGesehenAbrufen(nutzerId);
        List<FilmEntity> filmliste = filmEntityListe.getBody();
        List<BereitsGesehenEntity> gesehenerFilm = this.bereitsGesehenRepository.findAllByNutzerId(nutzerId);
        List<Long> filmIds = new LinkedList<Long>();
        for (BereitsGesehenEntity b: gesehenerFilm) {
            if(b.getDatum().before(endDatum) && b.getDatum().after(startDatum)){
                filmIds.add(b.getFilmId());
            }
        }
//        List<Long> filmIds = this.bereitsGesehenRepository.findByFilmId(filmId).stream().map(bereitsGesehenEntity -> bereitsGesehenEntity.getFilmId()).collect(Collectors.toList());
//        List<FilmEntity> filmliste = this.filmRepository.findAllById(filmIds);
//        List<BereitsGesehenEntity> bereitsGesehen = this.bereitsGesehenRepository.findByFilmId(filmId);
//        int kategorie = filmliste.stream().filter(FilmEntity -> FilmEntity.getKategorie().equals(kategorieArt)).collect(Collectors.toList()).size();
        int kategorie = filmliste.stream().filter(FilmEntity -> FilmEntity.getKategorie().contains(kategorieArt) && filmIds.contains(FilmEntity.getId())).collect(Collectors.toList()).size();
        return kategorie;
    }

    public ResponseEntity<List<PersonEntity>> getGeschauteSchauspieler(Long nutzerId, Date startDatum, Date endDatum) {
        List<BereitsGesehenEntity> gesehenerFilm = this.bereitsGesehenRepository.findAllByNutzerId(nutzerId);
        List<Long> filmIds = new LinkedList<Long>();
        for (BereitsGesehenEntity b: gesehenerFilm) {
            if(b.getDatum().before(endDatum) && b.getDatum().after(startDatum)){
                filmIds.add(b.getFilmId());
            }
        }
        List<Long> castIds = new LinkedList<Long>();
        List<PersonEntity> schauspieler = new LinkedList<PersonEntity>();
        for (Long filmId: filmIds) {
//            List<Long> temp = new LinkedList<Long>();
//            temp.add(filmId);
            List<Long> castIdsTemp = this.filmCastRepository.findAllByFilmId(filmId).stream().map(filmCastEntity -> filmCastEntity.getPersonId()).collect(Collectors.toList());
            castIds.addAll(castIdsTemp);
        }
        for (Long id: castIds) {
            List<PersonEntity> schauspielertemp = this.personRepository.findAllById(id);
            schauspieler.addAll(schauspielertemp);
        }
        return new ResponseEntity<>(schauspieler, HttpStatus.OK);
    }

    public ResponseEntity<List<FilmEntity>> getLieblingsfilme(Long nutzerId, Date startDatum, Date endDatum) {
        List<BereitsGesehenEntity> gesehenerFilm = this.bereitsGesehenRepository.findAllByNutzerId(nutzerId);
        List<Long> filmIds = new LinkedList<Long>();
        for (BereitsGesehenEntity b: gesehenerFilm) {
            if(b.getDatum().before(endDatum) && b.getDatum().after(startDatum)){
                filmIds.add(b.getFilmId());
            }
        }
        List<FilmbewertungEntity> filmbewertungEntityList = this.filmbewertungRepository.findAllByNutzerId(nutzerId).stream().filter(Bewertung -> filmIds.contains(Bewertung.getFilmId())).collect(Collectors.toList());
        List<FilmbewertungEntity> lieblingsfilme = filmbewertungEntityList.stream().filter(FilmbewertungEntity -> FilmbewertungEntity.getSterne().equals("5 Sterne")).collect(Collectors.toList());
        List<Long> filmIdListe = lieblingsfilme.stream().map(FilmbewertungsEntity -> FilmbewertungsEntity.getFilmId()).collect(Collectors.toList());
        List<FilmEntity> lieblingsfilmeNamenList = filmRepository.findAllById(filmIdListe);
        return new ResponseEntity<>(lieblingsfilmeNamenList, HttpStatus.OK);
    }

    public ResponseEntity<Integer> getGesamtzeitGeschauteFilme(Long nutzerId, Date startDatum, Date endDatum) {
        List<BereitsGesehenEntity> gesehenerFilm = this.bereitsGesehenRepository.findAllByNutzerId(nutzerId);
        List<Long> filmIds = new LinkedList<Long>();
        for (BereitsGesehenEntity b: gesehenerFilm) {
            if(b.getDatum().before(endDatum) && b.getDatum().after(startDatum)){
                filmIds.add(b.getFilmId());
            }
        }
//        List<Long> filmIdListe = this.bereitsGesehenRepository.findAllByNutzerId(nutzerId).stream().map(bereitsGesehenEntity -> bereitsGesehenEntity.getFilmId()).collect(Collectors.toList());
        List<String> filmLaengeListe = this.filmRepository.findAllById(filmIds).stream().map(filmEntity -> filmEntity.getFilmLaenge()).collect(Collectors.toList()); // Liste von Filmlänge in String "130min, 120min..."
        int sum = 0;
        for (String z : filmLaengeListe) {
            z =  z.substring(0, z.indexOf(" ")); // "100 min" -> Integer: 100
            sum = sum + Integer.parseInt(z); // aufaddieren
        }
        return new ResponseEntity<>(sum, HttpStatus.OK);
    }
}
