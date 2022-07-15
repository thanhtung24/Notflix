package com.example.server.service;

import com.example.server.entities.NutzerEntity;
import com.example.server.repository.FreundschaftRepository;
import com.example.server.repository.NutzerRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
@AllArgsConstructor
public class NutzersucheService {

    @Autowired
    private NutzerRepository nutzerRepository;

    @Autowired
    private FreundschaftRepository freundschaftRepository;

    public ResponseEntity<List<NutzerEntity>> getAlleNutzer() {
        List<NutzerEntity> alleNutzer = this.nutzerRepository.findAll();
        return new ResponseEntity<>(alleNutzer, HttpStatus.OK);
    }

    public ResponseEntity<List<NutzerEntity>> getAlleFreunde(Long nutzerId) {
        List<Long> freundeIds = this.freundschaftRepository.getAllByNutzerId(nutzerId).stream().map(freundEntity -> freundEntity.getFreundId()).collect(Collectors.toList());
        List<NutzerEntity> freunde = this.nutzerRepository.findAllById(freundeIds);

        if(freunde != null) {
            return new ResponseEntity<>(freunde, HttpStatus.OK);
        }
        return new ResponseEntity<>(freunde, HttpStatus.BAD_REQUEST);
    }
}
