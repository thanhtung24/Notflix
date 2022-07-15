package com.example.client.endpoints;

import com.example.client.model.Film;
import com.example.client.model.Nutzer;
import retrofit2.Call;
import retrofit2.http.*;

import java.time.LocalDate;
import java.util.List;

public interface NutzerEndpoint {

    @GET("/nutzer/alleNutzer")
    Call<List<Nutzer>> getAlleNutzer();

    @POST("/nutzer/nutzerRegistrierung")
    Call<Nutzer> nutzerRegistrierung(@Body Nutzer nutzer);

    @POST("/nutzer/nutzerLogin")
    Call<Nutzer> nutzerLogin(@Body Nutzer nutzer);

    @GET("/profil/watchlistAbrufen/{nutzerId}")
    Call<List<Film>> watchlistAbrufen(@Path("nutzerId") Long nutzerId);

    @GET("/profil/bereitsGesehenAbrufen/{nutzerId}")
    Call<List<Film>> bereitsGesehenAbrufen(@Path("nutzerId") Long nutzerId);

    @GET("/nutzer/getNutzer/{id}")
    Call<Nutzer> aktuellerNutzerAnhandId(@Path("id") Long id);

    @PUT("/profil/profildatenBearbeiten")
    Call <Nutzer> profilBearbeiten (@Query("nutzerId") Long nutzerId, @Query("vorname") String vorname,
                                    @Query("nachname") String nachname, @Query("geburtsdatum") String geburtsdatum,
                                    @Query("email") String email);

    @PUT("/profil/profildatenUndProfilbildBearbeiten")
    Call <Nutzer> profildatenUndProfilbildBearbeiten(@Query("nutzerId") Long nutzerId, @Query("vorname") String vorname,
                                                     @Query("nachname") String nachname, @Query("geburtsdatum") String geburtsdatum,
                                                     @Query("email") String email, @Query("profilbild") String profilbild);



}
