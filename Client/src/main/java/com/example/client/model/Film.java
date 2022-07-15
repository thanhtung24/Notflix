package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Film {

    private Long id;

    private String name;

    private String kategorie;

    private String filmLaenge;

    private String erscheinungsdatum;

    private Long regisseurId;

    private Long drehbuchautorId;

    private byte[] filmbanner;

    public Film(String name, String kategorie, String filmLaenge, String erscheinungsdatum, Long regisseurId, Long drehbuchautorId, byte[] filmbanner) {
        this.name = name;
        this.kategorie = kategorie;
        this.filmLaenge = filmLaenge;
        this.erscheinungsdatum = erscheinungsdatum;
        this.regisseurId = regisseurId;
        this.drehbuchautorId = drehbuchautorId;
        this.filmbanner = filmbanner;
    }
}
