package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonEntity {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Column
    @NotEmpty
    @Lob
    private String vorname;

    @Column
    @Lob
    @NotEmpty
    private String nachname;

    public PersonEntity(String vorname, String nachname) {
        this.vorname = vorname;
        this.nachname = nachname;
    }
}
