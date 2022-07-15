package com.example.client.endpoints;

import com.example.client.model.Nutzer;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;

public interface FreundschaftEndpoint {

    @GET("/profil/meineFreunde{id}")
    Call<List<Nutzer>> meineFreunde(@Path(value = "id") Long id);
}
