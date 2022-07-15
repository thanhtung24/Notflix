package com.example.server.service;

import com.example.server.entities.BereitsGesehenEntity;
import com.example.server.entities.FilmbewertungEntity;
import com.example.server.entities.FilmeinladungEntity;
import com.example.server.entities.WatchListEntity;
import com.example.server.repository.BereitsGesehenRepository;
import com.example.server.repository.FilmbewertungRepository;
import com.example.server.repository.FilmeinladungRepository;
import com.example.server.repository.WatchListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Service
@Transactional
public class KalenderService {

    @Autowired
    private FilmbewertungRepository filmbewertungRepository;

    @Autowired
    private WatchListRepository watchListRepository;

    @Autowired
    private BereitsGesehenRepository bereitsGesehenRepository;

    @Autowired
    private FilmeinladungRepository filmeinladungRepository;


    public List<FilmbewertungEntity> getBewertungenByNutzerId(Long nutzerId){ return this.filmbewertungRepository.findAllByNutzerId(nutzerId);}

    public List<WatchListEntity> getWatchlistByNutzerId(Long nutzerId){ return this.watchListRepository.findAllByNutzerId(nutzerId);}

    public List<BereitsGesehenEntity> getBereitsGesehenByNutzerId(Long nutzerId){ return this.bereitsGesehenRepository.findAllByNutzerId(nutzerId);}

    public List<FilmeinladungEntity> getFilmeinladungenByNutzerId(Long einladungsempfaengerId){ return this.filmeinladungRepository.findAllByEinladungsempfaengerId(einladungsempfaengerId);}
}
