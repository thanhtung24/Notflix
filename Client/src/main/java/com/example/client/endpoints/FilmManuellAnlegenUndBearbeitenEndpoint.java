package com.example.client.endpoints;

import com.example.client.model.Film;
import com.example.client.model.Person;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FilmManuellAnlegenUndBearbeitenEndpoint {
    @POST("/filmManuellAnlegenUndBearbeiten/filmAnlegen")
    Call<Film> filmAnlegen(@Body Film film);

    @GET("/filmManuellAnlegenUndBearbeiten/getFilmByNameAndRegisseur")
    Call<Film> getFilmByNameAndRegisseur(@Query("filmName") String filmName, @Query("regisseurId") Long regisseurId);

    @PUT("/filmManuellAnlegenUndBearbeiten/filmBearbeiten")
    Call<Film> bearbeiteFilm(@Query("filmId") Long filmId,
                             @Query("neuerFilmName") String name,
                             @Query("neuerFilmKategorie") String kategorie,
                             @Query("neuerFilmLaenge") String filmLaenge,
                             @Query("neuerFilmErscheinungsdatum") String erscheinungsdatem,
                             @Query("neuerFilmRegisseurId") Long regisseurId,
                             @Query("neuerFilmDrebuchautorID") Long drehbuchautorId,
                             @Query("neuerFilmFilmbanner") String filmbanner);

    @GET("/filmManuellAnlegenUndBearbeiten/getCast")
    Call<List<Person>> getPersonenByFilmId(@Query("filmId") Long filmId);


    @GET("/filmManuellAnlegenUndBearbeiten/getPerson/{personId}")
    Call<Person> getPersonById(@Path("personId") Long personId);
}
