package com.example.server.repository;

import com.example.server.entities.FreundschaftsanfrageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreundschaftsanfrageRepository extends JpaRepository<FreundschaftsanfrageEntity, Long> {
    List<FreundschaftsanfrageEntity> getAllByNutzerId(Long nutzerId);

    List<FreundschaftsanfrageEntity> findAllByAnfrageSenderIdAndNutzerId(Long anfrageSenderId, Long nutzerId);
}
