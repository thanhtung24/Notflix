package com.example.client.endpoints;

import com.example.client.model.Filmeinladung;
import com.example.client.model.Freundschaftsanfrage;
import lombok.Data;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FilmeinladungSendenEndpoint {

    @POST("/filmeinladung/sendeFilmeinladung")
    Call <Filmeinladung> sendeFilmeinladung(@Body Filmeinladung filmeinladung);


    @GET("/filmeinladung/alleFilmeinladungen/{einladungsempfaengerId}")
    Call <List<Filmeinladung>> getAlleFilmeinladungen (@Path("einladungsempfaengerId") Long einladungsempfaengerId);

    @GET ("/filmeinladung/getAlleFilmeinladungenWhereAkzeptiert/{einladungsenderId}")
    Call <List<Filmeinladung>> getAlleFilmeinladungenWhereAkzeptiert (@Path ("einladungsenderId") Long einladungsenderId);

    @PUT("/filmeinladung/akzeptiert")
    Call <Filmeinladung> akzeptiereFilmeinladung(@Query ("filmeinladungsId") Long filmeinladungsId);

    @PUT("/filmeinladung/gesehen")
    Call <Filmeinladung> geseheneAkzeptiereFilmeinladung(@Query ("filmeinladungsId") Long filmeinladungsId);



    @DELETE ("/filmeinladung/abgelehnt/{id}")
    Call <Long> abgelehnteFilmeinladung (@Path (value="id") Long id);

}
