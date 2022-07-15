package com.example.server.repository;

import com.example.server.entities.DiskussionsGruppenMitgliedEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiskussionsGruppenMitgliedRepository extends JpaRepository<DiskussionsGruppenMitgliedEntity, Long> {
    DiskussionsGruppenMitgliedEntity findByNutzerIdAndGruppenId(Long nutzerId, Long gruppenId);

    List<DiskussionsGruppenMitgliedEntity> findAllByNutzerId(Long nutzerId);

    void deleteDiskussionsGruppenMitgliedEntityByGruppenIdAndNutzerId(Long gruppenId, Long nutzerId);

    List<DiskussionsGruppenMitgliedEntity> findAllByGruppenId(Long gruppenId);
}
