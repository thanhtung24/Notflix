package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilmbewertungEntity {
    @Id
    @Column
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column
    private Long filmId;

    @Column
    private Long nutzerId;

    @Column
    private String vorname;

    @Column
    private String nachname;

    @NotEmpty(message = "Anzahl der Sterne darf nicht leer sein")
    @Column
    private String sterne;

    @Column
    private String kommentar;

    @Column
    private String filmname;

    @Column
    @NonNull
    private Date datum= new Date();

    public FilmbewertungEntity(Long filmId, Long nutzerId, String vorname, String nachname, String sterne, String kommentar, Date datum, String filmname){
        this.filmId = filmId;
        this.nutzerId = nutzerId;
        this.vorname = vorname;
        this.nachname = nachname;
        this.sterne = sterne;
        this.kommentar = kommentar;
        this.datum=datum;
        this.filmname = filmname;
    }

}
