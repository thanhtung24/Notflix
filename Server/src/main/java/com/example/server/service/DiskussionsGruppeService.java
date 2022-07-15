package com.example.server.service;

import com.example.server.entities.DiskussionsGruppeChatNachrichtEntity;
import com.example.server.entities.DiskussionsGruppeEntity;
import com.example.server.entities.DiskussionsGruppenMitgliedEntity;
import com.example.server.entities.NutzerEntity;
import com.example.server.repository.DiskussionsGruppeChatNachrichtRepository;
import com.example.server.repository.DiskussionsGruppeRepository;
import com.example.server.repository.DiskussionsGruppenMitgliedRepository;
import com.example.server.repository.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class DiskussionsGruppeService {

    @Autowired
    private DiskussionsGruppeRepository diskussionsGruppeRepository;

    @Autowired
    private DiskussionsGruppeChatNachrichtRepository diskussionsGruppeChatNachrichtRepository;

    @Autowired
    private DiskussionsGruppenMitgliedRepository diskussionsGruppenMitgliedRepository;

    @Autowired
    private NutzerRepository nutzerRepository;

    public void gruppeAnlegen(DiskussionsGruppeEntity diskussionsGruppeEntity){
        diskussionsGruppeRepository.save(diskussionsGruppeEntity);
    }

    public List<DiskussionsGruppeEntity> alleGruppen(){
        return diskussionsGruppeRepository.findAll();
    }

    public void nachrichtSpeichern(DiskussionsGruppeChatNachrichtEntity nachricht){
        diskussionsGruppeChatNachrichtRepository.save(nachricht);
    }

    public List<DiskussionsGruppeChatNachrichtEntity> chatVerlauf(Long gruppenId){
        return diskussionsGruppeChatNachrichtRepository.findAllByGruppenId(gruppenId);
    }

    public DiskussionsGruppenMitgliedEntity gruppenMitgliedSpeichern(DiskussionsGruppenMitgliedEntity mitlgied){
        return diskussionsGruppenMitgliedRepository.save(mitlgied);
    }

    public boolean mitgliedStatus(Long nutzerid, Long gruppenid){
        DiskussionsGruppenMitgliedEntity mitglied = diskussionsGruppenMitgliedRepository.findByNutzerIdAndGruppenId(nutzerid,gruppenid);
        return mitglied == null ? false : true;
    }

    public List<DiskussionsGruppeEntity> meineGruppen(Long nutzerId){
        List<DiskussionsGruppenMitgliedEntity> mitgliedsListe = diskussionsGruppenMitgliedRepository.findAllByNutzerId(nutzerId);
        List<DiskussionsGruppeEntity> meineGruppen = new ArrayList<>();
        for(DiskussionsGruppenMitgliedEntity mitglied: mitgliedsListe){
            DiskussionsGruppeEntity gruppe = diskussionsGruppeRepository.getById(mitglied.getGruppenId());
            meineGruppen.add(gruppe);
        }
        return meineGruppen;
    }

    public void gruppeVerlassen(Long gruppenId,Long nutzerId){
        diskussionsGruppenMitgliedRepository.deleteDiskussionsGruppenMitgliedEntityByGruppenIdAndNutzerId(gruppenId,nutzerId);
    }

    public boolean privacyStatus(Long gruppenId){
        DiskussionsGruppeEntity gruppe = diskussionsGruppeRepository.getDiskussionsGruppeEntityById(gruppenId);
        return  gruppe.isIstPrivat();

    }

    public DiskussionsGruppeEntity gruppeBearbeiten(Long gruppenId, Boolean istPrivat){
        DiskussionsGruppeEntity gruppe = diskussionsGruppeRepository.getDiskussionsGruppeEntityById(gruppenId);
        gruppe.setIstPrivat(istPrivat);
        diskussionsGruppeRepository.save(gruppe);
        return gruppe;
    }

    public List<NutzerEntity> alleNutzerEinerGruppe(Long gruppenId){
        List<DiskussionsGruppenMitgliedEntity> gruppenMitglieder = diskussionsGruppenMitgliedRepository.findAllByGruppenId(gruppenId);
        List<NutzerEntity> nutzerListe = new ArrayList<>();
        for(DiskussionsGruppenMitgliedEntity mitglied : gruppenMitglieder){
            NutzerEntity nutzer = nutzerRepository.findNutzerEntityById(mitglied.getNutzerId());
            nutzerListe.add(nutzer);
        }
        return nutzerListe;

    }
}
