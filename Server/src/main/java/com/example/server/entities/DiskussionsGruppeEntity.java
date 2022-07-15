package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiskussionsGruppeEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    private String gruppenName;

    private Long gruppenErstellerID;

    private boolean istPrivat;

    public DiskussionsGruppeEntity(String gruppenName, Long gruppenErstellerID, boolean istPrivat) {
        this.gruppenName = gruppenName;
        this.gruppenErstellerID = gruppenErstellerID;
        this.istPrivat = istPrivat;
    }
}
