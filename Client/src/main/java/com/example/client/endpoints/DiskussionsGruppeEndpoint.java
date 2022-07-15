package com.example.client.endpoints;


import com.example.client.model.DiskussionsGruppe;
import com.example.client.model.DiskussionsGruppeChatNachricht;
import com.example.client.model.DiskussionsGruppenMitglied;
import com.example.client.model.Nutzer;
import retrofit2.Call;
import retrofit2.http.*;
import java.util.List;

public interface DiskussionsGruppeEndpoint {

    @POST("/diskussionsgruppen/erstellen")
    Call<DiskussionsGruppe> diskussionsGruppeErstellen(@Body DiskussionsGruppe diskussionsGruppeEntity);

    @GET("/diskussionsgruppen/alleGruppen")
    Call<List<DiskussionsGruppe>> alleDiskussionsGruppe();

    @POST("/diskussionsgruppen/nachrichtSpeichern")
    Call<DiskussionsGruppeChatNachricht> nachrichtSpeichern(@Body DiskussionsGruppeChatNachricht nachricht);

    @GET("/diskussionsgruppen/chatVerlauf/{id}")
    Call<List<DiskussionsGruppeChatNachricht>> chatVerlaufVonGruppe(@Path(value = "id") Long id);

    @POST("/diskussionsgruppen/gruppeBeitreten")
    Call<DiskussionsGruppenMitglied> gruppenMitgliedHinzufuegen(@Body DiskussionsGruppenMitglied mitglied);

    @PUT("/diskussionsgruppen/mitgliedStatus/{nutzerid}/{gruppenid}")
    Call<Boolean> mitgliedStatus(@Path(value = "nutzerid") Long nutzerid, @Path(value = "gruppenid") Long gruppenid);

    @GET("/diskussionsgruppen/meineGruppen/{id}")
    Call<List<DiskussionsGruppe>> meineGruppen(@Path(value = "id") Long id);

    @PUT("/diskussionsgruppen/gruppeVerlassen/{gruppenId}/{nutzerId}")
    Call<Long> gruppeVerlassen(@Path(value = "gruppenId") Long gruppenId, @Path(value = "nutzerId") Long nutzerId);

    @GET("/diskussionsgruppen/gruppenPrivacyEinstellung/{gruppenId}")
    Call<Boolean> gruppenPrivacyEinstellung(@Path(value = "gruppenId") Long gruppenId);

    @PUT("/diskussionsgruppen/gruppeBearbeiten")
    Call<DiskussionsGruppe> gruppeBearbeiten(@Query("gruppenId") Long gruppenId, @Query("istPrivat") Boolean istPrivat);

    @GET("/diskussionsgruppen/gruppenMitglieder/{gruppenId}")
    Call<List<Nutzer>> alleGruppenMitglieder(@Path(value = "gruppenId") Long gruppenId);

}
