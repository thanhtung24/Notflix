package com.example.client.endpoints;

import com.example.client.model.Film;
import com.example.client.model.Freundschaft;
import com.example.client.model.Nutzer;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;
import java.util.concurrent.Executor;

public interface ProfilAndererNutzerEndpoint {
    @GET("/profilAndererNutzer/alleFreunde/{nutzerId}")
    Call<List<Nutzer>> getAlleFreunde(@Path("nutzerId") Long nutzerId);

    @POST("/profilAndererNutzer/akzeptiereAnfrage")
    Call<List<Freundschaft>> akzeptiereAnfrage(@Body List<Freundschaft> freundschaften);

    @GET("/profilAndererNutzer/freundschaftExistiert")
    Call<Boolean> freundschaftExistiert(@Query("nutzerId") Long nutzerId, @Query("freundId") Long freundID);

    @GET("/profilAndererNutzer/getWatchlist/{nutzerId}")
    Call<List<Film>> getWatchlist(@Path("nutzerId") Long nutzerId);

    @GET("/profilAndererNutzer/getlisteBereitsGesehenerFilme/{nutzerId}")
    Call<List<Film>> getlisteBereitsGesehenerFilme(@Path("nutzerId") Long nutzerId);
}
