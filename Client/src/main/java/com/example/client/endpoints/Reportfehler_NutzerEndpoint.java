package com.example.client.endpoints;

import com.example.client.model.Person;
import com.example.client.model.Report;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;

public interface Reportfehler_NutzerEndpoint {

    @POST("/report/ReportAufnehmen")
    Call <Report> reportAufnehmen (@Body Report report);

}
