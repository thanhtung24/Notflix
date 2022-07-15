package com.example.client.endpoints;

import com.example.client.model.Film;
import com.example.client.model.FilmCast;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.HashMap;
import java.util.List;

public interface FilmCastEndpoint {

    @POST("/filmCast/filmCastEntitesAnlegen")
    Call<FilmCast> filmCastAnlegen(@Body FilmCast filmCast);
}
