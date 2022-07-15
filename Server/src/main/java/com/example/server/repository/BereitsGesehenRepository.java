package com.example.server.repository;

import com.example.server.entities.BereitsGesehenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BereitsGesehenRepository extends JpaRepository<BereitsGesehenEntity, Long> {

    List<BereitsGesehenEntity> findAllByFilmIdAndNutzerId(Long filmId, Long nutzerId);

    List<BereitsGesehenEntity> findAllByNutzerId(Long nutzerId);



    List<BereitsGesehenEntity> findByFilmId(Long filmId);
}
