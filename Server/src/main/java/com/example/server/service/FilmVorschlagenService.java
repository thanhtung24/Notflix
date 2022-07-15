package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.java.FilmVorschlag;
import com.example.server.repository.FilmRepository;
import com.example.server.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@Service
@Transactional
public class FilmVorschlagenService {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private PersonRepository personRepository;


    private final int MAXIMALE_ANZAHL_AN_VORGESCHLAGENEN_FILMEN = 15;


    public ResponseEntity<List<FilmVorschlag>> schlageFilmeVor(Long nutzerId) {
        System.out.println("Nach Filmvorschlägen werden gesucht");
        List<FilmVorschlag> suggestedFilms = new ArrayList<>();
        String mostWatchedCategory = null;
        PersonEntity mostWatchedActor = null;
        int numberOfSuggestedFilms = 0;

        // suche nach den 10 letzten gesehenen Filmen und die beliebteste Filmkategorie

            // Ids von allen geschauten Filmen
        List<Long> lastlastWatchedFilmIDs = filmRepository.getLastWatchedFilm(nutzerId);
            // Alle geschauten Filme
        List<FilmEntity> lastWatchedFilms = findeFilmeAnhandFilmIds(lastlastWatchedFilmIDs);
            // Die 10 zuletzt geschauten Filme
        List<FilmEntity> tenLastWatchedFilms = lastWatchedFilms.stream().limit(10).collect(Collectors.toList());


        // Alle geschauten Filmkategorien von den 10 zuletzt geschauten Filmen
        List<String> allWatchedCategories = tenLastWatchedFilms.stream().map(film -> film.getKategorie()).collect(Collectors.toList());

        // Die häufigst gesehene Kategorie
        mostWatchedCategory = getMostWatchedCategory(allWatchedCategories);
        System.out.println("Lieblingskategorie: " + mostWatchedCategory);

        // Der häufigst geschaute Schauspieler
        mostWatchedActor = getMostWatchedActor(nutzerId);


        // such anhand Kategorie, Schauspieler und Bewertung
        if(mostWatchedCategory != null && mostWatchedActor != null) {
            String finalMostWatchedCategory = mostWatchedCategory;
            List<FilmEntity> filmBasedOnCAR = findeFilmeAnhandFilmIds(filmRepository.getFilmsBasedOnActorAndRate(mostWatchedActor.getId()));
            filmBasedOnCAR = filmBasedOnCAR.stream().filter(film -> film.getKategorie().contains(finalMostWatchedCategory)).collect(Collectors.toList());
            filmBasedOnCAR = filmBasedOnCAR.stream().filter(film -> !lastWatchedFilms.contains(film)).collect(Collectors.toList());

            for(FilmEntity film: filmBasedOnCAR) {

                String kommentar = "Hier ist ein guter Film basiert auf deine Lieblingskategorie und deinen Lieblingsschauspieler";

                suggestedFilms.add(new FilmVorschlag(film, kommentar));
                numberOfSuggestedFilms++;

                if(numberOfSuggestedFilms == 5) {break;}
            }

        }


        // such anhand Kategorie, Schauspieler
        if(numberOfSuggestedFilms < MAXIMALE_ANZAHL_AN_VORGESCHLAGENEN_FILMEN && mostWatchedCategory != null && mostWatchedActor != null) {
            String finalMostWatchedCategory = mostWatchedCategory;
            List<FilmEntity> filmBasedOnCA = findeFilmeAnhandFilmIds(filmRepository.getFilmsBasedOnActor(mostWatchedActor.getId()));
            filmBasedOnCA = filmBasedOnCA.stream().filter(film -> film.getKategorie().contains(finalMostWatchedCategory)).collect(Collectors.toList());
            filmBasedOnCA = filmBasedOnCA.stream().filter(film -> !suggestedFilms.stream().map(filmVorschlag -> filmVorschlag.getVorgeschlagenerFilm()).toList().contains(film) && !lastWatchedFilms.contains(film)).collect(Collectors.toList());

            for(FilmEntity film: filmBasedOnCA) {

                String kommentar = "Hier ist ein zufälliger Film basiert auf deine Lieblingskategorie und deinen Lieblingsschauspieler";

                suggestedFilms.add(new FilmVorschlag(film, kommentar));
                numberOfSuggestedFilms++;

                if(numberOfSuggestedFilms == 5) {break;}
            }
        }


        // suche nach besten Filmen anhand der beliebtesten Filmkategorie
        if(numberOfSuggestedFilms < MAXIMALE_ANZAHL_AN_VORGESCHLAGENEN_FILMEN && mostWatchedCategory != null) {
            List<FilmEntity> bestFilmsInDB = findeFilmeAnhandFilmIds(filmRepository.getBestFilmsInDB());
            String finalMostWatchedCategory = mostWatchedCategory;
            List<FilmEntity> bestFilmsBasedOnCategory = bestFilmsInDB.stream().filter(filmEntity -> filmEntity.getKategorie().contains(finalMostWatchedCategory)).collect(Collectors.toList());
            bestFilmsBasedOnCategory = bestFilmsBasedOnCategory.stream().filter(film -> !suggestedFilms.stream().map(filmVorschlag -> filmVorschlag.getVorgeschlagenerFilm()).toList().contains(film) && !lastWatchedFilms.contains(film)).collect(Collectors.toList());

            for(FilmEntity filmEntity: bestFilmsBasedOnCategory) {

                String kommentar = "Hier ist einer der besten " + mostWatchedCategory + " Filme in unserer Filmdatenbank";

                suggestedFilms.add(new FilmVorschlag(filmEntity, kommentar));
                numberOfSuggestedFilms++;

                if(numberOfSuggestedFilms == 5) {break;}
            }
        }


        // suche nach besten Filmen anhand des beliebtesten Schauspielers
        if(numberOfSuggestedFilms < MAXIMALE_ANZAHL_AN_VORGESCHLAGENEN_FILMEN && mostWatchedActor != null) {
            List<FilmEntity> filmBasedOnAR = findeFilmeAnhandFilmIds(filmRepository.getFilmsBasedOnActorAndRate(mostWatchedActor.getId()));
            filmBasedOnAR = filmBasedOnAR.stream().filter(film -> !suggestedFilms.stream().map(filmVorschlag -> filmVorschlag.getVorgeschlagenerFilm()).toList().contains(film) && !lastWatchedFilms.contains(film)).collect(Collectors.toList());

            for(FilmEntity film: filmBasedOnAR) {

                String kommentar = "Hier ist ein guter Film basiert auf deinen Lieblingsschauspieler";

                suggestedFilms.add(new FilmVorschlag(film, kommentar));
                numberOfSuggestedFilms++;

                if(numberOfSuggestedFilms == 5) {break;}
            }

        }

        // suche für jeden geschauten Film einen relevanten Film
        if(numberOfSuggestedFilms < 10) {
            for(FilmEntity filmEntity: tenLastWatchedFilms) {
                List<FilmEntity> relevantFilms = findeFilmeAnhandFilmIds(filmRepository.getSuggestedFilmsBasedOnWatchedFilm(filmEntity.getId(), nutzerId));
                if(mostWatchedCategory != null) {
                    String finalMostWatchedCategory = mostWatchedCategory;
                    relevantFilms = relevantFilms.stream().filter(film -> film.getKategorie().contains(finalMostWatchedCategory)).collect(Collectors.toList());
                    relevantFilms = relevantFilms.stream().filter(film -> !suggestedFilms.stream().map(filmVorschlag -> filmVorschlag.getVorgeschlagenerFilm()).toList().contains(film) && !lastWatchedFilms.contains(film)).collect(Collectors.toList());
                }
                if(!relevantFilms.isEmpty()) {

                    String kommentar = "Nutzer, die " + filmEntity.getName() + " geschaut haben, finden diesen " + mostWatchedCategory + " Film auch gut";

                    FilmEntity vorgeschlagenerFilm = relevantFilms.get(0);

                    suggestedFilms.add(new FilmVorschlag(vorgeschlagenerFilm, kommentar));

                    numberOfSuggestedFilms++;

                }
                if(numberOfSuggestedFilms == 5) {break;}
            }
        }


        // suche anhand der meist geschauten Filmkategorie nach maximal 5 Filmen, die die Freunde vom Nutzer am besten finden
        if(mostWatchedCategory != null) {
            List<FilmEntity> bestFilmsFromFriends = findeFilmeAnhandFilmIds(filmRepository.getBestFilmsFromFriends(nutzerId));
            String finalMostWatchedCategory = mostWatchedCategory;
            bestFilmsFromFriends = bestFilmsFromFriends.stream().filter(film -> film.getKategorie().contains(finalMostWatchedCategory)).collect(Collectors.toList());
            bestFilmsFromFriends = bestFilmsFromFriends.stream().filter(film -> !suggestedFilms.stream().map(filmVorschlag -> filmVorschlag.getVorgeschlagenerFilm()).toList().contains(film) && !lastWatchedFilms.contains(film)).collect(Collectors.toList());

            for(FilmEntity filmEntity: bestFilmsFromFriends) {

                String kommentar = "Deine Freunde haben diesen " + mostWatchedCategory + " Film gerne geschaut";

                suggestedFilms.add(new FilmVorschlag(filmEntity, kommentar));
                numberOfSuggestedFilms++;

                if(numberOfSuggestedFilms == 5) {break;}
            }
        }


        // suche nach besten Filmen
        if(numberOfSuggestedFilms < MAXIMALE_ANZAHL_AN_VORGESCHLAGENEN_FILMEN) {
            List<FilmEntity> bestFilmsInDB = findeFilmeAnhandFilmIds(filmRepository.getBestFilmsInDB());
            bestFilmsInDB = bestFilmsInDB.stream().filter(film -> !suggestedFilms.stream().map(filmVorschlag -> filmVorschlag.getVorgeschlagenerFilm()).toList().contains(film) && !lastWatchedFilms.contains(film)).collect(Collectors.toList());

            for(FilmEntity filmEntity: bestFilmsInDB) {

                String kommentar = "Hier ist einer der besten Filme in unserer Filmdatenbank";

                suggestedFilms.add(new FilmVorschlag(filmEntity, kommentar));
                numberOfSuggestedFilms++;

                if(numberOfSuggestedFilms == 5) {break;}
            }
        }


        // such nach den am häufigsten gesehenen Filmen
        if(numberOfSuggestedFilms < MAXIMALE_ANZAHL_AN_VORGESCHLAGENEN_FILMEN) {
            List<FilmEntity> mostWatchedFilms = findeFilmeAnhandFilmIds(filmRepository.getMostWatchedFilms());
            mostWatchedFilms = mostWatchedFilms.stream().filter(film -> !suggestedFilms.stream().map(filmVorschlag -> filmVorschlag.getVorgeschlagenerFilm()).toList().contains(film) && !lastWatchedFilms.contains(film)).collect(Collectors.toList());

            for(FilmEntity filmEntity: mostWatchedFilms) {

                String kommentar = "Hier ist einer der meist gesehenen Filme in unserer Filmdatenbank";

                suggestedFilms.add(new FilmVorschlag(filmEntity, kommentar));
                numberOfSuggestedFilms++;

                if(numberOfSuggestedFilms == 5) {break;}
            }
        }


        // suche nach zufälligen Filmen
        if(numberOfSuggestedFilms < MAXIMALE_ANZAHL_AN_VORGESCHLAGENEN_FILMEN) {
            List<FilmEntity> allFilms = filmRepository.findAll();
            allFilms = allFilms.stream().filter(film -> !suggestedFilms.stream().map(filmVorschlag -> filmVorschlag.getVorgeschlagenerFilm()).toList().contains(film) && !lastWatchedFilms.contains(film)).collect(Collectors.toList());

            for(FilmEntity filmEntity: allFilms) {

                String kommentar = "Hier ist ein zufälliger Film für dich";

                suggestedFilms.add(new FilmVorschlag(filmEntity, kommentar));
                numberOfSuggestedFilms++;

                if(numberOfSuggestedFilms == MAXIMALE_ANZAHL_AN_VORGESCHLAGENEN_FILMEN) {break;}
            }
        }
        System.out.println(numberOfSuggestedFilms + " Filmvorschläge wurden gefunden");
        return new ResponseEntity<>(suggestedFilms, HttpStatus.OK);
    }

    // such nach am häufigsten gesehener Filmkategorie
    protected String getMostWatchedCategory(List<String> watchedFilmGenre) {
        if(watchedFilmGenre.isEmpty()) {
            return null;
        }

        Map<String, Integer> map = new HashMap<>();
        String mostWatchedGenre = null;
        Integer filmCounter = 0;

        for (String kategorie: watchedFilmGenre) {
            String[] genres = kategorie.split(", ");
            for(String genre: genres) {
                if(mostWatchedGenre == null) {
                    mostWatchedGenre = genre;
                    filmCounter++;
                }
                if(map.containsKey(genre)) {
                    Integer filmAnzahl = map.get(genre);
                    map.put(genre, ++filmAnzahl);
                    if(filmAnzahl > filmCounter) {
                        mostWatchedGenre = genre;
                        filmCounter = filmAnzahl;
                    }
                } else {
                    map.put(genre, 1);
                }
            }
        }
        return mostWatchedGenre;
    }

    private PersonEntity getMostWatchedActor(Long nutzerId) {
        Long mostWatchedActorID = filmRepository.getMostWatchedActor(nutzerId);
        if(mostWatchedActorID != null) {
            PersonEntity mostWatchedActor = personRepository.getById(mostWatchedActorID);
            System.out.println("Lieblingsschauspieler: " + mostWatchedActor.getVorname() + " " + mostWatchedActor.getNachname());
            return mostWatchedActor;
        }
        return null;
    }

    protected List<FilmEntity> findeFilmeAnhandFilmIds(List<Long> filmids) {
        List<FilmEntity> result = new ArrayList<>();
        for(Long filmid: filmids) {
            result.add(filmRepository.getById(filmid));
        }
        return result;
    }

}
