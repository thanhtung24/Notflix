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
public class FilmCastEntity {
    @Id
    @Column
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Column
    @NotNull
    private Long filmId;

    @Column
    @NotNull
    private Long personId;

    public FilmCastEntity(Long filmId, Long personId) {
        this.filmId = filmId;
        this.personId = personId;
    }
}
