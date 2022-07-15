package com.example.server.repository;

import com.example.server.entities.FilmStatistikEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmStatistikRepository extends JpaRepository<FilmStatistikEntity, Long> {

    FilmStatistikEntity findByfilmId(Long filmId);
}
