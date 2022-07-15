package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class PrivateEinstellungen {

    private Long id;

    private Long nutzerId;

    private String watchlistEinstellung;

    private String geseheneFilmeListeEinstellung;

    private String freundelisteEinstellung;

    private String bewertungenEintstellung;

    public PrivateEinstellungen(Long nutzerId) {
        this.nutzerId = nutzerId;
        this.watchlistEinstellung = "Alle";
        this.geseheneFilmeListeEinstellung = "Alle";
        this.freundelisteEinstellung = "Alle";
        this.bewertungenEintstellung = "Alle";
    }

    public PrivateEinstellungen(Long nutzerId, String watchlistEinstellung, String geseheneFilmeListeEinstellung, String freundelisteEinstellung, String bewertungenEintstellung) {
        this.nutzerId = nutzerId;
        this.watchlistEinstellung = watchlistEinstellung;
        this.geseheneFilmeListeEinstellung = geseheneFilmeListeEinstellung;
        this.freundelisteEinstellung = freundelisteEinstellung;
        this.bewertungenEintstellung = bewertungenEintstellung;
    }
}
