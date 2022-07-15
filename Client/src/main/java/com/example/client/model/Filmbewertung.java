package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Filmbewertung {
    private Long id;

    private Long filmId;

    private Long nutzerId;

    private String vorname;

    private String nachname;

    private String sterne;

    private String kommentar;

    private String filmname;

    public Date datum;

    public Filmbewertung(Long filmId, Long nutzerId, String vorname, String nachname, String sterne, String kommentar, String filmname){
        this.filmId = filmId;
        this.nutzerId = nutzerId;
        this.vorname = vorname;
        this.nachname = nachname;
        this.sterne = sterne;
        this.kommentar = kommentar;
        this.filmname = filmname;
        datum = new Date();
    }

}
