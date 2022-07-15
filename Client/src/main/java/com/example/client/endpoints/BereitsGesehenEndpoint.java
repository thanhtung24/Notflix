package com.example.client.endpoints;

import com.example.client.model.BereitsGesehenItem;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.lang.reflect.Array;
import java.util.List;

public interface BereitsGesehenEndpoint {

    @POST("/bereitsGesehen/filmZuBereitsGesehenAnlegen")
    Call<BereitsGesehenItem> filmZuBereitsGesehenAnlegen(@Body BereitsGesehenItem bereitsGesehenItem);

    @GET ("/bereitsGesehen/bereitsGesehenAbrufen/{filmId}/{nutzerId}")
    Call<BereitsGesehenItem> getBereitsGesehenItemByFilmIdAndNutzerId(@Path("filmId") Long filmId, @Path("nutzerId") Long nutzerId);



}
