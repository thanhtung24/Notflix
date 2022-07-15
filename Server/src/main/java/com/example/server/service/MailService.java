package com.example.server.service;

import com.example.server.entities.FilmeinladungEntity;
import com.example.server.entities.NutzerEntity;
import com.example.server.repository.NutzerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMultipart;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@Service
public class MailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    NutzerRepository nutzerRepository;

    private String serviceSenderMail = "notflixservice@gmail.com";
    private String betreff = "Sicherheitscode zum Login";
    //
    private String betreffReport ="Es liegt ein neuer Report vor";
    private String bodyReport ="Es liegt ein neuer Report vor. \nBitte log dich jetzt ein und bearbeite den Report.";

    private HashMap<String, String> nachrichtenCodes = new HashMap<>();


    public boolean sendeFilmeinladungsMail (FilmeinladungEntity filmeinladungEntity) {
        NutzerEntity nutzerEmpfaenger = nutzerRepository.findNutzerEntityById(filmeinladungEntity.getEinladungsempfaengerId().longValue());
        NutzerEntity nutzerSender = nutzerRepository.findNutzerEntityById(filmeinladungEntity.getEinladungssenderId().longValue());

        String betreffFilmeinladungSenden = "Du hast eine neue Filmeinladung!";
        String bodyFilmeinladungSenden = nutzerSender.getVorname()+" "+nutzerSender.getNachname()+" hat dir eine Filmeinladung gesendet.\n\nFilm: "+filmeinladungEntity.getFilmname()+"\nDatum: "+filmeinladungEntity.getDatum()+"\nUhrzeit: "+filmeinladungEntity.getUhrzeit()+"\nKommentar: "+filmeinladungEntity.getKommentar()+"\n\nLog dich ein und sage "+nutzerSender.getVorname()+" bescheid, ob du die Einladung annimmst :)";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.serviceSenderMail); //Unsere Email-Adresse


        message.setText(bodyFilmeinladungSenden); //Nachricht
        message.setSubject(betreffFilmeinladungSenden); //Betreff
        message.setTo(nutzerEmpfaenger.getEmail());
        javaMailSender.send(message);

        return true;
    }

    public boolean sendeFilmeinladungAkzeptiertMail (FilmeinladungEntity filmeinladungEntity) {
        //Der der akzeptiert hat
        NutzerEntity nutzerEinladungsEmpfaenger = nutzerRepository.findNutzerEntityById(filmeinladungEntity.getEinladungsempfaengerId().longValue());
        //Der, der Gesendet hat
        NutzerEntity nutzerEinladungsSender = nutzerRepository.findNutzerEntityById(filmeinladungEntity.getEinladungssenderId().longValue());

        String betreff = "Neue Benachrichtigung zu deiner Filmeinladung.";
        String body= nutzerEinladungsEmpfaenger.getVorname()+ " " +nutzerEinladungsEmpfaenger.getNachname() +" hat deine Filmeinladung zu dem Film "+filmeinladungEntity.getFilmname()+" angenommen.";


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.serviceSenderMail); //Unsere Email-Adresse


        message.setText(body); //Nachricht
        message.setSubject(betreff); //Betreff
        message.setTo(nutzerEinladungsSender.getEmail());
        javaMailSender.send(message);

        return true;
    }

    public boolean sendeFilmeinladungAbgelehntMail (FilmeinladungEntity filmeinladungEntity) {
        //Der der akzeptiert hat
        NutzerEntity nutzerEinladungsEmpfaenger = nutzerRepository.findNutzerEntityById(filmeinladungEntity.getEinladungsempfaengerId().longValue());
        //Der, der Gesendet hat
        NutzerEntity nutzerEinladungsSender = nutzerRepository.findNutzerEntityById(filmeinladungEntity.getEinladungssenderId().longValue());

        String betreff = "Neue Benachrichtigung zu deiner Filmeinladung.";
        String body= nutzerEinladungsEmpfaenger.getVorname()+ " " +nutzerEinladungsEmpfaenger.getNachname() +" hat deine Filmeinladung zu dem Film "+filmeinladungEntity.getFilmname()+" leider abgelehnt.";


        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(this.serviceSenderMail); //Unsere Email-Adresse


        message.setText(body); //Nachricht
        message.setSubject(betreff); //Betreff
        message.setTo(nutzerEinladungsSender.getEmail());
        javaMailSender.send(message);

        return true;
    }


    public boolean sendEmailToAllAdmins (String [] to) {

        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom(this.serviceSenderMail); //Unsere Email-Adresse
        message.setText(this.bodyReport); //Nachricht
        message.setSubject(this.betreffReport); //Betreff
        message.setTo(to); //-> alle Systemadministratoren sollen eine Email erhalten
        javaMailSender.send(message);


        return true;
    }


    public String sendSimpleMessage(String to) {
        System.out.println("Im Mail Service");
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(serviceSenderMail);
        message.setTo(to);
        message.setSubject(betreff);
        String code = loginCode();
        nachrichtenCodes.put(to, code);
        message.setText("Sicherheitscode: " + code);
        javaMailSender.send(message);
        System.out.println("Mail wurde geschickt");
        return code;
    }

    public String loginCode(){
        int code = 0;
        StringBuilder stringBuilder = new StringBuilder();
        for(int i = 0; i < 7; i++){
            code = new Random().nextInt(10);
            stringBuilder.append(code);
        }
        //wenn der code mit einer 0 anfaengt dann akzeptiert der client den code nicht
        if(stringBuilder.toString().startsWith("0")){
            stringBuilder.replace(0, 1, "1");
        }

        return stringBuilder.toString();
    }

    public boolean codeKontrolle(String mail, String code){
        System.out.println(nachrichtenCodes);
        if(nachrichtenCodes.containsKey(mail)){
            if(nachrichtenCodes.get(mail).equals(code)){
                return true;
            }
        }
        return false;
    }


}
