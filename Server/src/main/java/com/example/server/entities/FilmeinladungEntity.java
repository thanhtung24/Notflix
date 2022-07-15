package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data

public class FilmeinladungEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = READ_ONLY)
    private Long id;


    private String filmname;

    private Long einladungssenderId;

    private String einladungssenderName;

    private Long einladungsempfaengerId;

    private String kommentar;

    private String datum;

    private String uhrzeit;

    private boolean akzeptiert=false;

    private boolean gesehen=false;
}
