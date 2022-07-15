package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@NoArgsConstructor


public class ReportEntity {

    @Id //Primary Key
    @Column
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Column
    @NotEmpty (message = "Filmname muss angebene werden!")
    private String filmname;

    @Column
    @NotEmpty (message = "Regisseur muss angebene werden!")
    private String regisseur;

    @Column
    @NotEmpty (message = "'Fehler in' muss ausgewählt werden!")
    private String fehler_in;

    @Column
    private boolean erledigt;

    @Column
    private String kommentar;


    public ReportEntity (String filmname, String regisseur, String fehler_in, boolean erledigt, String kommentar){
        this.filmname=filmname;
        this.regisseur=regisseur;
        this.fehler_in=fehler_in;
        this.erledigt=erledigt;
        this.kommentar=kommentar;
    }

}
