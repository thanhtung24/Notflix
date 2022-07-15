package com.example.client.endpoints;

import com.example.client.model.Film;
import com.example.client.model.Report;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;

import java.util.List;

public interface ReportanfrageAdminEndpoint {

    @GET("/report/alleReports")
    Call <List<Report>> getAlleReports();

}
