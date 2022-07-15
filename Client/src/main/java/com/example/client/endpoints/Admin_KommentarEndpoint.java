package com.example.client.endpoints;

import com.example.client.model.Film;
import com.example.client.model.Report;
import retrofit2.Call;
import retrofit2.http.*;

public interface Admin_KommentarEndpoint {


    @PUT("/reportErledigt/erledigt")
    Call<Report> bearbeiteReport(@Query("reportId") Long reportId);

}
