package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@EqualsAndHashCode
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class FilmStatistik {

    private Long id;

    private Long filmId;

    private Date datum;

    public FilmStatistik(Long filmId,  Date datum){
        this.id= id;
        this.filmId=filmId;
        this.datum= datum;
    }
}
