package com.example.client.endpoints;

import com.example.client.model.Filmbewertung;
import com.example.client.model.Notiz;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.util.List;

public interface NotizEndpoint {

    @POST("/Kalender/notizSpeichern")
    Call<Notiz> speichern(@Body Notiz notiz);

    @GET("/Kalender/getNotizenByNutzerId")
    Call<List<Notiz>> getNotizenByNutzerId(@Query("nutzerId") Long nutzerId);
}
