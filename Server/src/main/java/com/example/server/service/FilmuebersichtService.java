package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.PersonEntity;
import com.example.server.repository.FilmRepository;
import com.example.server.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Component
@Service
@Transactional
public class FilmuebersichtService {

    @Autowired
    private PersonRepository personRepository;

    public List<PersonEntity> getPersonenByFilmId(Long filmId) {
        return this.personRepository.findAllByFilmId(filmId);
    }

    public PersonEntity getPersonById(Long personId) {
        return this.personRepository.findAllById(personId).get(0);
    }

}
