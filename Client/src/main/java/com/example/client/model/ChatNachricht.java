package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatNachricht {

    private Long id;

    private Long personSenderId;

    private Long personEmpfaengerId;

    private String inhalt;

    private boolean gelesen;

    private Long zeitStempel;
}
