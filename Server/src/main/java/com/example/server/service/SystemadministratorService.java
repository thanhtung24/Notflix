package com.example.server.service;

import com.example.server.entities.FilmEntity;
import com.example.server.entities.SystemadministratorEntity;
import com.example.server.repository.SystemadministratorRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;

@Component
@AllArgsConstructor
@Service
public class SystemadministratorService {

    private SystemadministratorRepository systemadministratorRepository;

    public List<SystemadministratorEntity> getAdmins(){
        return systemadministratorRepository.findAll();
    }

    public ResponseEntity<SystemadministratorEntity> createAdmin(SystemadministratorEntity systemadministrator){
        List<SystemadministratorEntity> systemadministratorList =  findeAlleSystemadminsAnhandEmail(systemadministrator.getEmail());
        if (systemadministratorList.isEmpty()){
            System.out.println("Admin erstellt");
            systemadministratorRepository.save(systemadministrator);
            return new ResponseEntity(systemadministrator, HttpStatus.OK);
        }
        else{
            System.out.println("User versucht sich mit einer bereits vorhandenen Email zu registrieren");
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
    }

    public List<SystemadministratorEntity> findeAlleSystemadminsAnhandEmail(String email){
        return systemadministratorRepository.findSystemadministratorByEmail(email);
    }

    public List<SystemadministratorEntity> getAlleEMailsVonAdmins () {
        return this.systemadministratorRepository.findAll();
    }


    public ResponseEntity<SystemadministratorEntity> adminLogin(SystemadministratorEntity systemadministrator){
        List<SystemadministratorEntity> systemadministratorList = findeAlleSystemadminsAnhandEmail(systemadministrator.getEmail());
        if(systemadministratorList.isEmpty()){
            System.out.println("Es gibt keinen Admin mit dieser E-Mail");
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
        SystemadministratorEntity systemadministratorReturn = findeAlleSystemadminsAnhandEmail(systemadministrator.getEmail()).get(0);
        if(systemadministrator.getPasswort().equals(systemadministratorReturn.getPasswort())){
            System.out.println("Erfolgreicher Login");
            return new ResponseEntity<>(systemadministratorReturn, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
