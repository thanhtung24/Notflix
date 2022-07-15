package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import java.util.Date;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BereitsGesehenEntity {
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Column
    @NotNull
    private Long filmId;

    @Column
    @NotNull
    private Long nutzerId;

    @Column
    private String filmname;

    @Column
    @NonNull
    private Date datum= new Date();

    public BereitsGesehenEntity(Long filmId, Long nutzerId, Date datum, String filmname) {
        this.filmId = filmId;
        this.nutzerId = nutzerId;
        this.filmname = filmname;
        this.datum=datum;
    }
}
