package com.example.client.endpoints;

import com.example.client.model.ChatNachricht;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

import java.util.List;

public interface PrivaterChatEndpoint {

    @POST("/chat/sendeNachricht")
    Call<ChatNachricht> nachrichteSenden(@Body ChatNachricht nachricht);

    @GET("/chat/chatVerlauf/{id1}/{id2}")
    Call<List<ChatNachricht>> meinChatVerlauf(@Path(value = "id1") Long id1, @Path(value = "id2") Long id2);
}
