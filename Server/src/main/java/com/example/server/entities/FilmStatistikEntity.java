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

public class FilmStatistikEntity {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = READ_ONLY)

    private Long id;

    @Column(unique = true)
    @NotNull
    private Long filmId;

    @Column
    private Date datum;



    public FilmStatistikEntity(Long filmId, Date datum){
        this.filmId= filmId;
        this.datum=datum;
    }


}
