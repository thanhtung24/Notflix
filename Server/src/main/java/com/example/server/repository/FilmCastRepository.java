package com.example.server.repository;

import com.example.server.entities.FilmCastEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface FilmCastRepository extends JpaRepository<FilmCastEntity, Long> {

    void deleteAllByFilmId(Long filmId);

    List<FilmCastEntity> findAllByFilmId(Long filmId);

    @Query(value = "select fc.personId from FilmCastEntity fc where fc.filmId in :filmIds", nativeQuery = true)
    List<Long> getAllePersonen(List<Long> filmIds);
}
