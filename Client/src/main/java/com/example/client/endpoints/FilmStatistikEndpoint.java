package com.example.client.endpoints;

import com.example.client.model.FilmStatistik;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.HashMap;
import java.util.List;

public interface FilmStatistikEndpoint {

    @GET("/filmstatistik/getAnzahl/{filmId}")
    Call<HashMap<String, Integer>> getAnzahl(@Path ("filmId") Long filmId);

    @PUT("/filmstatistik/zuruecksetzen/{filmId}")
    Call<Long> zuruecksetzen (@Query("filmId") Long filmId);

    @GET("/filmstatistik/getDurchschnitt")
    Call<String> getDurchschnitt();

}
