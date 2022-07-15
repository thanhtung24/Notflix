package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DiskussionsGruppenMitgliedEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    private Long nutzerId;

    private Long gruppenId;

    public DiskussionsGruppenMitgliedEntity(Long nutzerId, Long gruppenId) {
        this.nutzerId = nutzerId;
        this.gruppenId = gruppenId;
    }
}
