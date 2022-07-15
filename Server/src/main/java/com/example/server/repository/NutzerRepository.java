package com.example.server.repository;

import com.example.server.entities.NutzerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NutzerRepository extends JpaRepository <NutzerEntity, Long> {

    List<NutzerEntity> findNutzerByEmail(String email);

    NutzerEntity findNutzerEntityById(Long id);


}
