package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.SuperBuilder;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiskussionsGruppe {

    private Long id;

    private String gruppenName;

    private Long gruppenErstellerID;

    private boolean istPrivat;


}
