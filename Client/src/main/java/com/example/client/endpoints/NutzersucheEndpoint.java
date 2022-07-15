package com.example.client.endpoints;

import com.example.client.model.Nutzer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface NutzersucheEndpoint {
    @GET("/nutzersuche/alleNutzer")
    Call<List<Nutzer>> getAlleNutzer();

    @GET("/nutzersuche/alleFreunde/{nutzerId}")
    Call<List<Nutzer>> getAlleFreunde(@Path("nutzerId") Long nutzerId);
}
