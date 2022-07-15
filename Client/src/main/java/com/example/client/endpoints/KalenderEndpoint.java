package com.example.client.endpoints;

import com.example.client.model.BereitsGesehenItem;
import com.example.client.model.Filmbewertung;
import com.example.client.model.Filmeinladung;
import com.example.client.model.WatchListItem;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.Date;
import java.util.List;

public interface KalenderEndpoint {

    @GET("/Kalender/getBewertungenByNutzerId")
    Call<List<Filmbewertung>> getBewertungenByNutzerId(@Query("nutzerId") Long nutzerId);

    @GET("/Kalender/getWatchlistByNutzerId")
    Call<List<WatchListItem>> getWatchlistByNutzerId(@Query("nutzerId") Long nutzerId);

    @GET("/Kalender/getBereitsGesehenByNutzerId")
    Call<List<BereitsGesehenItem>> getBereitsGesehenByNutzerId(@Query("nutzerId") Long nutzerId);

    @GET("/Kalender/getFilmeinladungenByNutzerId")
    Call<List<Filmeinladung>> getFilmeinladungenByNutzerId(@Query("einladungsempfaengerId") Long einladungsempfaengerId);
}
