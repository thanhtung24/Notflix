package com.example.server.repository;

import com.example.server.entities.FilmbewertungEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FilmbewertungRepository extends JpaRepository<FilmbewertungEntity, Long> {

    //@Query("from FilmEntity fil join FilmbewertungEntity bew ON(fil.id = bew.filmId)")
    List<FilmbewertungEntity> findAllByFilmId(Long filmId);

    List<FilmbewertungEntity> findAllByNutzerId(Long nutzerId);

    //@Query("from FilmEntity fil join FilmbewertungEntity bew ON(fil.id = bew.filmId) join NutzerEntity nut ON(bew.nutzerId = nut.id)")
    FilmbewertungEntity findByFilmIdAndNutzerId(Long filmId, Long nutzerId);

}
