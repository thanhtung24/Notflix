package com.example.server.repository;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.WatchListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WatchListRepository extends JpaRepository<WatchListEntity, Long> {

    List<WatchListEntity> findAllByFilmIdAndNutzerId(Long filmId, Long nutzerId);

    List<WatchListEntity> findAllByNutzerId(Long nutzerId);

    void deleteWatchListEntityByFilmId(Long filmId);
}
