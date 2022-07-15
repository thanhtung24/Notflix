package com.example.server.service;

import com.example.server.entities.FilmbewertungEntity;
import com.example.server.entities.NotizEntity;
import com.example.server.repository.NotizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Service
@Transactional
public class NotizService {
    @Autowired
    private NotizRepository notizRepository;

    public ResponseEntity<NotizEntity> speichern(NotizEntity notizEntity){
        notizRepository.save(notizEntity);
        return new ResponseEntity<>(notizEntity, HttpStatus.OK);
    }

    public List<NotizEntity> getNotizenByNutzerId(Long nutzerId){ return this.notizRepository.findAllByNutzerId(nutzerId);}
}
