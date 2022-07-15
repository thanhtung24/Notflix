package com.example.server.restcontroller;


import com.example.server.entities.FilmeinladungEntity;
import com.example.server.entities.ReportEntity;
import com.example.server.entities.SystemadministratorEntity;
import com.example.server.repository.SystemadministratorRepository;
import com.example.server.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/email")
public class MailRestController {

    @Autowired
    private MailService mailService;

    @Autowired
    SystemadministratorRepository systemadministratorRepository;


    @PutMapping("/sendMail")
    public ResponseEntity<String> sendMail(@RequestParam(value = "empfaenger") String empfaenger){
        System.out.println("Anfrage im Mail Controller eingegangen");
        String code = mailService.sendSimpleMessage(empfaenger);
        System.out.println(empfaenger);
        return new ResponseEntity<>(code, HttpStatus.OK);
    }

    @GetMapping("/getCode/{mail}/{code}")
    public ResponseEntity<Boolean> mailCode(@PathVariable(value = "mail") String mail,@PathVariable(value = "code") String code){
        return new ResponseEntity<>(mailService.codeKontrolle(mail, code), HttpStatus.OK);
    }


    @PutMapping ("/sendMailReport")
    public ResponseEntity<Boolean> sendReportMail (@RequestParam (value="empfaenger") String [] empfaengerFuerReport ) {
        boolean funktioniert;
        funktioniert=mailService.sendEmailToAllAdmins(empfaengerFuerReport);
        return new ResponseEntity<>(funktioniert, HttpStatus.OK);
    }

    @PutMapping ("/sendMailFilmeinladung")
    public ResponseEntity<Boolean> sendeFilmeinladungsMail (@RequestBody FilmeinladungEntity filmeinladungEntity) {
        boolean funktioniert;
        funktioniert = mailService.sendeFilmeinladungsMail(filmeinladungEntity);
        return new ResponseEntity<>(funktioniert, HttpStatus.OK);
    }

    @PutMapping ("/sendMailFilmeinladungAkzeptiert")
    public ResponseEntity<Boolean> sendeFilmeinladungAkzeptiert (@RequestBody FilmeinladungEntity filmeinladungsEntity) {
        boolean funktioniert;
        funktioniert = mailService.sendeFilmeinladungAkzeptiertMail(filmeinladungsEntity);
        return new ResponseEntity<>(funktioniert, HttpStatus.OK);
    }

    @PutMapping ("/sendMailFilmeinladungAbgelehnt")
    public ResponseEntity<Boolean> sendeFilmeinladungAbgelehnt (@RequestBody FilmeinladungEntity filmeinladungsEntity) {
        boolean funktioniert;
        funktioniert = mailService.sendeFilmeinladungAbgelehntMail(filmeinladungsEntity);
        return new ResponseEntity<>(funktioniert, HttpStatus.OK);
    }

}
