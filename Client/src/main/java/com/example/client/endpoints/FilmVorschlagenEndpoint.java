package com.example.client.endpoints;

import com.example.client.model.Film;
import com.example.client.model.FilmVorschlag;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

import java.util.List;
import java.util.Map;

public interface FilmVorschlagenEndpoint {
    @GET("/filmVorschlagen/getFilme/{nutzerId}")
    Call<List<FilmVorschlag>> getFilmVorschlaege(@Path("nutzerId") Long nutzerId);
}
