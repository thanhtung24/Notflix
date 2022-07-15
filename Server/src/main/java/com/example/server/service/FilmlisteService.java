package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.repository.FilmRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Service
@Transactional
public class FilmlisteService {

    @Autowired
    private FilmRepository filmRepository;

    public List<FilmEntity> getAlleFilmNamen () {
        return this.filmRepository.findAll();
    }
}
