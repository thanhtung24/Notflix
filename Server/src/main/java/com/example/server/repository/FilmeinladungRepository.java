package com.example.server.repository;

import com.example.server.entities.FilmeinladungEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmeinladungRepository extends JpaRepository<FilmeinladungEntity, Long> {

  //  List <FilmeinladungEntity> getAllByNutzerId (Long einladungsempfaengerId);

    void deleteFilmeinladungEntityById(Long filmeinladungsId);

    List<FilmeinladungEntity> findAllByEinladungsempfaengerId(Long nutzerId);
}
