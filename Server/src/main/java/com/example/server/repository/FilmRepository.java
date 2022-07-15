package com.example.server.repository;

import com.example.server.entities.FilmEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<FilmEntity, Long> {

    List<FilmEntity> findAllByNameAndRegisseurId(String name, Long regisseurId);

    void deleteById(Long id);

    /**
     * Sucht nach den geschauten Filmen
     * */
    @Query(value = "SELECT DISTINCT bg.FILM_ID FROM BEREITS_GESEHEN_ENTITY bg " +
            "WHERE bg.NUTZER_ID = :nutzerId " +
            "ORDER BY bg.ID DESC",
            nativeQuery = true)
    List<Long> getLastWatchedFilm(Long nutzerId);

    /**
     * Sucht anhand des gesehenen Films nach relevanten Filmen
     * Filmkategorie wird nicht mit berücksichtigt
     * @param filmId
     * */
    @Query(value = "SELECT DISTINCT fb.FILM_ID FROM FILMBEWERTUNG_ENTITY fb INNER JOIN FILM_ENTITY f " +
                                        "ON fb.FILM_ID = f.ID  " +
                                        "WHERE fb.NUTZER_ID IN (SELECT bg.NUTZER_ID FROM BEREITS_GESEHEN_ENTITY bg INNER JOIN FILMBEWERTUNG_ENTITY fbe " +
                                                                "ON (bg.FILM_ID = fbe.FILM_ID AND bg.NUTZER_ID = fbe.NUTZER_ID) " +
                                                                "WHERE bg.FILM_ID = :filmId AND bg.NUTZER_ID != :nutzerId  " +
                                                                "AND (fbe.STERNE = '5 Sterne' OR fbe.STERNE = '4 Sterne') " +
                                                                ") " +
                                        "AND (fb.STERNE = '5 Sterne' OR fb.STERNE = '4 Sterne') " +
                                        "ORDER BY fb.STERNE DESC  ",
    nativeQuery = true)
    List<Long> getSuggestedFilmsBasedOnWatchedFilm(@Param("filmId") Long filmId, @Param("nutzerId") Long nutzerId);

    /**
     * Sucht nach Filmen, die die Freunde von dem Nutzer am besten finden
     * Filmkategorie wird nicht mit berücksichtigt
     * */
    @Query(value = "SELECT DISTINCT f.ID FROM FILMBEWERTUNG_ENTITY fb INNER JOIN FILM_ENTITY f " +
                                    "ON fb.FILM_ID = f.ID  " +
                                    "WHERE fb.NUTZER_ID IN (SELECT fs.FREUND_ID FROM FREUNDSCHAFT_ENTITY fs " +
                                    "WHERE fs.NUTZER_ID = :nutzerId " +
                                    ") " +
                    "AND (fb.STERNE = '5 Sterne' OR fb.STERNE = '4 Sterne') " +
                    "ORDER BY fb.STERNE DESC",
    nativeQuery = true)
    List<Long> getBestFilmsFromFriends(@Param("nutzerId") Long nutzerId);

    /**
    * Sucht nach besten Filmen in der Datenbank
    * */
    @Query(value = "SELECT f.ID FROM FILM_ENTITY f INNER JOIN FILMBEWERTUNG_ENTITY fb " +
                                    "ON f.ID = fb.FILM_ID  " +
                                    "WHERE fb.STERNE = '5 Sterne' OR fb.STERNE = '4 Sterne' " +
                                    "ORDER BY fb.STERNE DESC",
    nativeQuery = true)
    List<Long> getBestFilmsInDB();

    /**
     * Sucht nach am häufigsten gesehenen Filmen
     * */
    @Query(value = "SELECT DISTINCT film_and_number_of_watches.FILM_ID FROM (SELECT bg.FILM_ID, COUNT(bg.ID) AS number_of_watches " +
                                                                            "FROM BEREITS_GESEHEN_ENTITY bg " +
                                                                            "GROUP BY bg.FILM_ID  " +
                                                                            "ORDER BY number_of_watches DESC " +
                                                                            "LIMIT 1) AS film_and_number_of_watches",
    nativeQuery = true)
    List<Long> getMostWatchedFilms();

    /**
     * Sucht nach dem häufigst gesehenen Schauspieler
     * */
   @Query(value = "SELECT mostWatchedActor.PERSON_ID FROM (SELECT gesehen.PERSON_ID, COUNT(*) AS anzahl " +
           "FROM (SELECT fc.PERSON_ID, bg.FILM_ID FROM BEREITS_GESEHEN_ENTITY bg  " +
           "JOIN FILM_CAST_ENTITY fc  " +
           "ON (bg.FILM_ID = fc.FILM_ID) " +
           "WHERE bg.NUTZER_ID = :nutzerid) AS gesehen " +
           "GROUP BY gesehen.PERSON_ID " +
           "ORDER BY anzahl DESC  " +
           "LIMIT 1) AS mostWatchedActor " +
           "WHERE mostWatchedActor.anzahl > 1",
   nativeQuery = true)
    Long getMostWatchedActor(@Param("nutzerid") Long nutzerId);

   @Query(value = "SELECT DISTINCT fe.ID FROM FILM_ENTITY fe INNER JOIN FILM_CAST_ENTITY fce  " +
           "ON(fe.ID = fce.FILM_ID) " +
           "INNER JOIN FILMBEWERTUNG_ENTITY fbe " +
           "ON(fe.ID = fbe.FILM_ID) " +
           "WHERE (fbe.STERNE = '5 Sterne' OR fbe.STERNE = '4 Sterne') " +
           "AND fce.PERSON_ID = :PERSON_ID " +
           "ORDER BY fbe.STERNE DESC ",
   nativeQuery = true)
    List<Long> getFilmsBasedOnActorAndRate(@Param("PERSON_ID") Long id);


    @Query(value = "SELECT DISTINCT fe.ID FROM FILM_ENTITY fe INNER JOIN FILM_CAST_ENTITY fce  " +
            "ON(fe.ID = fce.FILM_ID) " +
            "WHERE fce.PERSON_ID = :PERSON_ID ",
    nativeQuery = true)
    List<Long> getFilmsBasedOnActor(@Param("PERSON_ID") Long id);
}
