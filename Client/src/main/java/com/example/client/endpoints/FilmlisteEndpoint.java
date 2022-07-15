package com.example.client.endpoints;

import com.example.client.model.Film;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface FilmlisteEndpoint {

    @GET("/filmliste/alleFilmNamen")
    Call<List<Film>> getAlleFilmNamen();

}
