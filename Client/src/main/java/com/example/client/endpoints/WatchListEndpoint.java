package com.example.client.endpoints;

import com.example.client.model.Person;
import com.example.client.model.WatchListItem;
import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface WatchListEndpoint {
    @POST("/watchList/filmZuWatchListAnlegen")
    Call<WatchListItem> filmZuWatchListAnlegen(@Body WatchListItem watchListItem);

    @GET("/watchList/watchlistAbrufen/{filmId}/{nutzerId}")
    Call<WatchListItem> getWatchListItemByFilmIdAndNutzerId(@Path("filmId") Long filmId, @Path("nutzerId") Long nutzerId);

    @DELETE("/watchList/filmVonWatchListEntfernen/{id}")
    Call<Long> filmVonWatchListEntfernen(@Path(value = "id") Long id);
}
