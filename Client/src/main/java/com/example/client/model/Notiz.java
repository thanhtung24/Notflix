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
public class Notiz {
    private Long id;
    private Long nutzerId;
    private String text;
    public Date datum;

    public Notiz(Long nutzerId, String text, Date datum){
        this.nutzerId = nutzerId;
        this.text = text;
        this.datum = datum;
    }

}
