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
public class DiskussionsGruppeChatNachrichtEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    //ToDo: Id an welche die Nachricht geschickt werden soll
    private Long gruppenId;

    private Long personSenderId;

    private String inhalt;

    private Long zeitStempel;
}
