package com.example.client.endpoints;

import com.example.client.model.Filmbewertung;
import lombok.Getter;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.Executor;

public interface FilmbewertungEndpoint {

    @POST("/Filmbewertungen/absenden")
    Call<Filmbewertung> absenden(@Body Filmbewertung filmBewertung);

    @PUT("/Filmbewertungen/bewertungBearbeiten")
    Call<Filmbewertung> bewertungBearbeiten(@Query("bewertungId") Long bewertungId,
                                            @Query("filmId") Long filmId,
                                            @Query("nutzerId") Long nutzerId,
                                            @Query("vorname") String vorname,
                                            @Query("nachname") String nachname,
                                            @Query("neueSterne") String sterne,
                                            @Query("neuerKommentar") String kommentar
                                            );

    @GET("/Filmbewertungen/getInformationToTable")
    Call<List<Filmbewertung>> getBewertungenByFilmId(@Query("filmId") Long filmId);

    @GET("/Filmbewertungen/getBewertungByFilmIdAndNutzerId")
    Call<Filmbewertung> getBewertungenByFilmIdAndNutzerId(@Query("filmId") Long filmId, @Query("nutzerId") Long nutzerId);

    @GET("/Filmbewertungen/freundschaftExistiert")
    Call<Boolean> freundschaftExistiert(@Query("nutzerId") Long nutzerId, @Query("freundId") Long freundID);



}
