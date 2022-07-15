package com.example.server.repository;

import com.example.server.entities.DiskussionsGruppeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DiskussionsGruppeRepository extends JpaRepository<DiskussionsGruppeEntity, Long> {

    DiskussionsGruppeEntity getDiskussionsGruppeEntityById(Long gruppenId);
}
