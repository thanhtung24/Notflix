package com.example.server.service;

import com.example.server.entities.BereitsGesehenEntity;
import com.example.server.repository.BereitsGesehenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@Service
@Transactional
public class BereitsGesehenService {
    @Autowired
    private BereitsGesehenRepository bereitsGesehenRepository;

    public ResponseEntity<BereitsGesehenEntity> getBereitsGesehenItemByFilmIdAndNutzerId(Long filmId, Long nutzerId){
        try{
            return new ResponseEntity<>(bereitsGesehenRepository.findAllByFilmIdAndNutzerId(filmId, nutzerId).get(0), HttpStatus.OK);
        } catch(IndexOutOfBoundsException e){
            System.out.println("Kein BereitsGesehenItem existiert mit den Ids");
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<BereitsGesehenEntity> filmZuBereitsGesehenAnlegen(BereitsGesehenEntity bereitsGesehenEntity){
        ResponseEntity<BereitsGesehenEntity> bereitsGesehenResponse = getBereitsGesehenItemByFilmIdAndNutzerId(bereitsGesehenEntity.getFilmId(), bereitsGesehenEntity.getNutzerId());
        if(!bereitsGesehenResponse.hasBody()){
            this.bereitsGesehenRepository.save(bereitsGesehenEntity);
        }
        return new ResponseEntity<>(bereitsGesehenEntity, HttpStatus.OK);
    }





}
