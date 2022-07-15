package com.example.server.repository;

import com.example.server.entities.NotizEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotizRepository extends JpaRepository<NotizEntity, Long> {

    List<NotizEntity> findAllByNutzerId(Long nutzerId);
}
