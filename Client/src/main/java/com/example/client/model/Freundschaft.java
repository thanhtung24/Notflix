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
public class Freundschaft {

    private Long id;

    @NonNull
    private Long nutzerId;

    @NonNull
    private Long freundId;

    public Freundschaft(@NonNull Long nutzerId, @NonNull Long freundId) {
        this.nutzerId = nutzerId;
        this.freundId = freundId;
    }
}
