package com.example.server.repository;

import com.example.server.entities.FreundschaftEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FreundschaftRepository extends JpaRepository<FreundschaftEntity, Long> {
    List<FreundschaftEntity> getAllByNutzerId(Long nutzerId);

    List<FreundschaftEntity> findAllByNutzerIdAndFreundId(Long nutzerId, Long freundId);

    List<FreundschaftEntity> findAllByNutzerId(Long nutzerId);


}
