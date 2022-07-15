package com.example.client.endpoints;

import com.example.client.model.Person;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface PersonAnlegenEndpoint {
    @GET("/person/allePersonen")
    Call<List<Person>> getAllePersonen();

    @POST("/person/personAnlegen")
    Call<Person> personAnlegen(@Body Person person);
}
