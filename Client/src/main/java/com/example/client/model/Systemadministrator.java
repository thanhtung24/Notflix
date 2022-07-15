package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@EqualsAndHashCode()
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Systemadministrator {

    private Long id;

    private String vorname;

    private String nachname;

    private String email;

    private String passwort;

    public Systemadministrator(String vorname, String nachname, String email, String passwort) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.passwort = passwort;
    }

    public Systemadministrator(String email, String passwort) {
        this.email = email;
        this.passwort = passwort;
    }
}

