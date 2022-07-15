package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PrivateEinstellungenEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Column(unique = true)
    @NotNull
    private Long nutzerId;

    @Column
    @NotEmpty
    private String watchlistEinstellung = "Alle";

    @Column
    @NotEmpty
    private String geseheneFilmeListeEinstellung = "Alle";

    @Column
    @NotEmpty
    private String freundelisteEinstellung = "Alle";

    @Column
    @NotEmpty
    private String bewertungenEintstellung = "Alle";
}
