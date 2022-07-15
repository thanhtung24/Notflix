package com.example.client.endpoints;

import com.example.client.model.Person;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.List;

public interface FilmuebersichtEndpoint {

    @GET("/filmUebersicht/getCast")
    Call<List<Person>> getPersonenByFilmId(@Query("filmId") Long filmId);

    @GET("/filmUebersicht/getPerson/{personId}")
    Call<Person> getPersonById(@Path("personId") Long personId);
}
