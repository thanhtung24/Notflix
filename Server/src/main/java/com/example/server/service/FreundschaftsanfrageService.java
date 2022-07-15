package com.example.server.service;

import com.example.server.entities.FreundschaftsanfrageEntity;
import com.example.server.entities.NutzerEntity;
import com.example.server.repository.FreundschaftsanfrageRepository;
import com.example.server.repository.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Service
@Transactional
public class FreundschaftsanfrageService {

    @Autowired
    private FreundschaftsanfrageRepository freundschaftsanfrageRepository;

    @Autowired
    private NutzerRepository nutzerRepository;

    public ResponseEntity<List<NutzerEntity>> getAlleFreundschaftsanfragen(Long nutzerId) {
        List<Long> andereNutzerIds = this.freundschaftsanfrageRepository.getAllByNutzerId(nutzerId)
                .stream().map(entity -> entity.getAnfrageSenderId()).collect(Collectors.toList());

        List<NutzerEntity> result = nutzerRepository.findAllById(andereNutzerIds);

        if(result != null) {
            return new ResponseEntity<>(result, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<FreundschaftsanfrageEntity> sendeFreundschaftsanfrage(FreundschaftsanfrageEntity anfrage) {
        this.freundschaftsanfrageRepository.save(anfrage);
        if(this.freundschaftsanfrageRepository.getById(anfrage.getId()) != null) {
            return new ResponseEntity<>(anfrage, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<Boolean> anfrageExistiert(Long anfrageSenderId, Long nutzerId) {
        if(!this.freundschaftsanfrageRepository.findAllByAnfrageSenderIdAndNutzerId(anfrageSenderId, nutzerId).isEmpty()) {
            return new ResponseEntity<>(true, HttpStatus.OK);
        }
        return new ResponseEntity<>(false, HttpStatus.OK);
    }
}
