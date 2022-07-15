package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NotizEntity {
    @Id
    @Column
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @Column
    private Long nutzerId;

    @Column
    private String text;

    @Column
    @NonNull
    private Date datum= new Date();

    public NotizEntity(Long nutzerId, String text, Date datum){
        this.nutzerId = nutzerId;
        this.text = text;
        this.datum = datum;
    }
}
