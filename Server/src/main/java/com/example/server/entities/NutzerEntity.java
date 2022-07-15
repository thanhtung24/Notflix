package com.example.server.entities;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor


public class NutzerEntity {

    @Id
    @Column
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)

    private Long id;

    @NotEmpty(message = "Vorname muss eingegeben werden")
    private String vorname;

    @NotEmpty(message = "Nachname muss eingegeben werden")
    private String nachname;

    @NotEmpty(message= "Geburtsdatum muss eingegeben werden")
    private String geburtsdatum;

    @NotEmpty(message = "Email muss eingegeben werden")
    @Column(unique = true)
    private String email;

    @NotEmpty(message = "Passwort muss eingegeben werden")
    private String passwort;

    @Column
    @Lob
    private byte[] profilbild;

    public NutzerEntity(String vorname, String nachname, String geburtsdatum, String email, String passwort, byte[] profilbild) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.geburtsdatum = geburtsdatum;
        this.email = email;
        this.passwort = passwort;
        this.profilbild = profilbild;
    }
}
