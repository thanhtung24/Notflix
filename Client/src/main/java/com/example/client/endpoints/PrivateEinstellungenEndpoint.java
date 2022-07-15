package com.example.client.endpoints;

import com.example.client.model.PrivateEinstellungen;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PrivateEinstellungenEndpoint {

    @POST("/privateEinstellungen/nutzerPrivateEinstellungen")
    Call<PrivateEinstellungen> nutzerStandardeinstellungenAnlegen(@Body PrivateEinstellungen privateEinstellungen);

    @GET("/privateEinstellungen/getPrivateEinstellungen/{nutzerId}")
    Call<PrivateEinstellungen> getPrivateEinstellungen(@Path("nutzerId") Long id);

    @PUT("/privateEinstellungen/speichereEinstellungen")
    Call<PrivateEinstellungen> speichereEinstellungen(@Query("nutzerId") Long nutzerId,
                                                      @Query("watchListEinstg") String watchListEinstg,
                                                      @Query("geseheneFilmeListeEinstg") String geseheneFilmeListeEinstg,
                                                      @Query("freundeListeEinstg") String freundeListeEinstg,
                                                      @Query("bewertungenEinstg") String bewertungenEinstg);
}
