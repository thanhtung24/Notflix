package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DiskussionsGruppeChatNachricht {

    private Long id;

    //ToDo: Id an welche die Nachricht geschickt werden soll
    private Long gruppenId;

    private Long personSenderId;

    private String inhalt;

    private Long zeitStempel;
}

