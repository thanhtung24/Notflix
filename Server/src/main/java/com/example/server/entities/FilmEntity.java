package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.sql.Date;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class FilmEntity {
    @Id
    @Column
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @NotEmpty(message = "Name des Films darf nicht leer sein")
    @Column
    private String name;

    @Column
    private String kategorie;

    @Column
    private String filmLaenge;

    @Column
    private String erscheinungsdatum;

    @Column
    private Long regisseurId;

    @Column
    private Long drehbuchautorId;

    @Column
    @Lob
    private byte[] filmbanner;

    public FilmEntity(String name, String kategorie, String filmLaenge, String erscheinungsdatum, Long regisseurId, Long drehbuchautorId, byte[] filmbanner) {
        this.name = name;
        this.kategorie = kategorie;
        this.filmLaenge = filmLaenge;
        this.erscheinungsdatum = erscheinungsdatum;
        this.regisseurId = regisseurId;
        this.drehbuchautorId = drehbuchautorId;
        this.filmbanner = filmbanner;
    }
}
