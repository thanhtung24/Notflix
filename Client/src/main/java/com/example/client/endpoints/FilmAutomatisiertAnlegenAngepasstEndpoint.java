package com.example.client.endpoints;

import com.example.client.model.Film;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface FilmAutomatisiertAnlegenAngepasstEndpoint {

    @POST("/filmAutomatisiertAnlegen/jahr")
    Call<ResponseBody> automatisierteFilmeAnhandZeitraum(@Body String[] zeitraum);

    @POST("/filmAutomatisiertAnlegen/kategorie")
    Call<ResponseBody> automatisierteFilmeAnhandKategorie(@Body String kategorie);

    @POST("/filmAutomatisiertAnlegen/kategorieJahr")
    Call<ResponseBody> automatisierteFilmeAnhandKategorieUndJahr(@Body String[] kategorieUndJahr);

}
