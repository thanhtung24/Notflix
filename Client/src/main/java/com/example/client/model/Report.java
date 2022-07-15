package com.example.client.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)


public class Report {

    private Long id;

    private String filmname;

    private String regisseur;

    private String fehler_in;

    private boolean erledigt;

    private String kommentar;

    public Report (String filmname, String regisseur, String fehler_in, boolean erledigt, String kommentar) {
        this.filmname=filmname;
        this.regisseur=regisseur;
        this.fehler_in=fehler_in;
        this.erledigt=erledigt;
        this.kommentar=kommentar;
    }

    public Report (String filmname, String regisseur, boolean erledigt, String fehler_in) {
        this.filmname=filmname;
        this.regisseur=regisseur;
        this.erledigt=erledigt;
        this.fehler_in=fehler_in;
    }


}
