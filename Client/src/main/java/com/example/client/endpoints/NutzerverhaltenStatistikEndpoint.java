package com.example.client.endpoints;

import com.example.client.model.Film;
import com.example.client.model.Person;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface NutzerverhaltenStatistikEndpoint {
    @GET("/nutzerverhaltenstatistik/getAnzahlKategorie/{nutzerId}/{startDatum}/{endDatum}")
    Call<HashMap<String, Integer>> getAnzahlKategorie(@Path("nutzerId") Long nutzerId, @Path("startDatum") Date startDatum,@Path("endDatum")  Date endDatum);

    @GET("/nutzerverhaltenstatistik/getGeschauteSchauspieler/{nutzerId}/{startDatum}/{endDatum}")
    Call<List<Person>> getGeschauteSchauspieler(@Path("nutzerId") Long nutzerId, @Path("startDatum") Date startDatum, @Path ("endDatum") Date endDatum);

    @GET("/nutzerverhaltenstatistik/getLieblingsfilme/{nutzerId}/{startDatum}/{endDatum}")
    Call<List<Film>> getLieblingsfilme(@Path("nutzerId") Long nutzerId, @Path("startDatum") Date startDatum, @Path ("endDatum") Date endDatum);

    @GET("/nutzerverhaltenstatistik/getGesamtzeitGeschauteFilme/{nutzerId}/{startDatum}/{endDatum}")
    Call<Integer> getGesamtzeitGeschauteFilme(@Path("nutzerId") Long nutzerId, @Path("startDatum") Date startDatum, @Path ("endDatum") Date endDatum);

}
