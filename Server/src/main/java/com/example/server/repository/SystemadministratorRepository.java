package com.example.server.repository;

import com.example.server.entities.SystemadministratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemadministratorRepository extends JpaRepository<SystemadministratorEntity, Long> {

    List<SystemadministratorEntity> findSystemadministratorByEmail(String email);

}
