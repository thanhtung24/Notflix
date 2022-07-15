package com.example.server.service;

import com.example.server.entities.PrivateEinstellungenEntity;
import com.example.server.repository.PrivateEinstellungenRepository;
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
public class PrivateEinstellungenService {

    @Autowired
    private PrivateEinstellungenRepository privateEinstellungenRepository;

    public ResponseEntity<PrivateEinstellungenEntity> nutzerStandardeinstellungenAnlegen(PrivateEinstellungenEntity privateEinstellungen) {
        this.privateEinstellungenRepository.save(privateEinstellungen);
        if(this.privateEinstellungenRepository.findAllByNutzerId(privateEinstellungen.getNutzerId()).isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(privateEinstellungen, HttpStatus.OK);
    }

    public ResponseEntity<PrivateEinstellungenEntity> getPrivateEinstellungen(Long nutzerId) {
        if(this.privateEinstellungenRepository.findAllByNutzerId(nutzerId).isEmpty()) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }

        PrivateEinstellungenEntity privateEinstellungen = this.privateEinstellungenRepository.findAllByNutzerId(nutzerId).get(0);

        return new ResponseEntity<>(privateEinstellungen, HttpStatus.OK);
    }

    public ResponseEntity<PrivateEinstellungenEntity> speichereEinstellungen(Long nutzerId, String watchListEinstg, String geseheneFilmeListeEinstg, String freundeListeEinstg, String bewertungenEinstg) {
        if(!this.privateEinstellungenRepository.findAllByNutzerId(nutzerId).isEmpty()) {
            PrivateEinstellungenEntity privateEinstellungen = this.privateEinstellungenRepository.findAllByNutzerId(nutzerId).get(0);
            privateEinstellungen.setWatchlistEinstellung(watchListEinstg);
            privateEinstellungen.setGeseheneFilmeListeEinstellung(geseheneFilmeListeEinstg);
            privateEinstellungen.setFreundelisteEinstellung(freundeListeEinstg);
            privateEinstellungen.setBewertungenEintstellung(bewertungenEinstg);

            this.privateEinstellungenRepository.save(privateEinstellungen);
            return new ResponseEntity<>(privateEinstellungen, HttpStatus.OK);
        }
        return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
    }
}
