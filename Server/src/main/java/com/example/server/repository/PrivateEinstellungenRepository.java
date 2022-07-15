package com.example.server.repository;

import com.example.server.entities.PrivateEinstellungenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PrivateEinstellungenRepository extends JpaRepository<PrivateEinstellungenEntity, Long> {
    List<PrivateEinstellungenEntity> findAllByNutzerId(Long nutzerId);
}
