package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FilmCast {
    private Long id;

    private Long filmId;

    private Long personId;

    public FilmCast(Long filmId, Long personId) {
        this.filmId = filmId;
        this.personId = personId;
    }
}
