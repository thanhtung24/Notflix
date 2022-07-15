package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.lang.NonNull;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)

public class Filmeinladung {

    private Long id;

    @NonNull
    private String filmname;

    @NonNull
    private Long einladungssenderId;

    @NonNull
    private String einladungssenderName;

    @NonNull
    private Long einladungsempfaengerId;

    private String kommentar;

    @NonNull
    private String datum;

    @NonNull
    private String uhrzeit;

    @NonNull
    private boolean akzeptiert = false;

    @NonNull
    private boolean gesehen =false;

    public Filmeinladung (@NonNull String filmname, @NonNull Long einladungssenderId, @NonNull String einladungssenderName, @NonNull Long einladungsempfaengerId,
            String kommentar, @NonNull String datum, @NonNull String uhrzeit){

        this.filmname=filmname;
        this.einladungssenderId=einladungssenderId;
        this.einladungssenderName=einladungssenderName;
        this.einladungsempfaengerId=einladungsempfaengerId;
        this.kommentar=kommentar;
        this.datum=datum;
        this.uhrzeit=uhrzeit;
    }

}
