package com.example.server.repository;

import com.example.server.entities.DiskussionsGruppeChatNachrichtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiskussionsGruppeChatNachrichtRepository extends JpaRepository<DiskussionsGruppeChatNachrichtEntity, Long> {

    List<DiskussionsGruppeChatNachrichtEntity> findAllByGruppenId(Long gruppenId);

}
