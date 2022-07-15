package com.example.client.endpoints;

import com.example.client.model.Freundschaftsanfrage;
import com.example.client.model.Nutzer;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface FreundschaftsanfrageEndpoint {
    @GET("/freundschaftsanfragen/alleFreundschaftsanfragen/{nutzerId}")
    Call<List<Nutzer>> getAlleFreundschaftsanfragen(@Path("nutzerId") Long nutzerId);

    @POST("/freundschaftsanfragen/sendeFreundschaftsanfrage")
    Call<Freundschaftsanfrage> sendeFreundschaftsanfrage(@Body Freundschaftsanfrage anfrage);

    @GET("/freundschaftsanfragen/anfrageExistiert")
    Call<Boolean> anfrageExistiert(@Query("anfrageSenderId") Long anfrageSenderId, @Query("nutzerId") Long nutzerId);


}
