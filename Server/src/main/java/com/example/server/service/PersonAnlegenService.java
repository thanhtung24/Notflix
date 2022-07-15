package com.example.server.service;

import com.example.server.entities.PersonEntity;
import com.example.server.repository.PersonRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
@Transactional
public class PersonAnlegenService {

    @Autowired
    private PersonRepository personRepository;

    public List<PersonEntity> getAllePersonen() { return this.personRepository.findAll(); }

    public ResponseEntity<PersonEntity> personAnlegen (PersonEntity personEntity) {
        if(this.personRepository.findAllByNachnameAndVorname(personEntity.getNachname(), personEntity.getVorname()).isEmpty()) {
            this.personRepository.save(personEntity);
            return new ResponseEntity<>(personEntity, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
