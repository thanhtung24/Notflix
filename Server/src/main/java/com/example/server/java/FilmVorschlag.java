package com.example.server.java;

import com.example.server.entities.FilmEntity;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FilmVorschlag {

    private FilmEntity vorgeschlagenerFilm;

    private String filmVorschlagKommentar;
}
