package com.example.server.repository;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.PersonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface PersonRepository extends JpaRepository<PersonEntity, Long> {
    List<PersonEntity> findAllByNachnameAndVorname(String nachname, String vorname);

    @Query("from PersonEntity per join FilmCastEntity ca ON(per.id = ca.personId) where ca.filmId = ?1")
    List<PersonEntity> findAllByFilmId(Long filmId);

    List<PersonEntity> findAllById(Long id);
}
