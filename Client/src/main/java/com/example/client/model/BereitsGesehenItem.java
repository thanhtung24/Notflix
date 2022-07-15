package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.Date;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class BereitsGesehenItem {
    private Long id;

    private Long filmId;

    private Long nutzerId;

    private String filmname;

    public Date datum;

    public BereitsGesehenItem(Long filmId, Long nutzerId, String filmname) {
        this.filmId = filmId;
        this.nutzerId = nutzerId;
        this.filmname = filmname;
        datum = new Date();
    }
}
