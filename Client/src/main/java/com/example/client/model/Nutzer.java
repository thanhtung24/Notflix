package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)


public class Nutzer {

    private Long id;

    private String vorname;

    private String nachname;

    private String geburtsdatum;

    private String email;

    private String passwort;

    private byte [] profilbild;

    public Nutzer(String vorname, String nachname, String geburtsdatum, String email, String passwort){
        this.vorname=vorname;
        this.nachname=nachname;
        this.geburtsdatum=geburtsdatum;
        this.email=email;
        this.passwort=passwort;
    }

    public Nutzer (String vorname, String nachname, String geburtsdatum, String email){
        this.vorname=vorname;
        this.nachname=nachname;
        this.geburtsdatum=geburtsdatum;
        this.email=email;
    }

    public Nutzer (String email, String passwort){
        this.email=email;
        this.passwort=passwort;
    }

    public Nutzer(String vorname, String nachname, String geburtsdatum, String email, String passwort, byte[] profilbild){
        this.vorname=vorname;
        this.nachname=nachname;
        this.geburtsdatum=geburtsdatum;
        this.email=email;
        this.passwort=passwort;
        this.profilbild=profilbild;
    }

    public Nutzer(String vorname, String nachname, String geburtsdatum, String email, byte[] profilbild){
        this.vorname=vorname;
        this.nachname=nachname;
        this.geburtsdatum=geburtsdatum;
        this.email=email;
        this.profilbild=profilbild;
    }



}
