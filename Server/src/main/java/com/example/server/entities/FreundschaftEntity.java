package com.example.server.entities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import static com.fasterxml.jackson.annotation.JsonProperty.Access.READ_ONLY;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FreundschaftEntity {
    @Id
    @Column
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    @JsonProperty(access = READ_ONLY)
    private Long id;

    @Column
    @NotNull
    private Long nutzerId;

    @Column
    @NotNull
    private Long freundId;
}
