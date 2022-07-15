package com.example.server.restcontroller;

import com.example.server.entities.PersonEntity;
import com.example.server.service.PersonAnlegenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/person")
public class PersonAnlegenRestController {

    @Autowired
    PersonAnlegenService personAnlegenService;

    @GetMapping("/allePersonen")
    public List<PersonEntity> getAllePersonen() { return this.personAnlegenService.getAllePersonen(); }

    @PostMapping("/personAnlegen")
    public ResponseEntity<PersonEntity> personAnlegen(@RequestBody PersonEntity personEntity) {
        return this.personAnlegenService.personAnlegen(personEntity);
    }
}
