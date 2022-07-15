package com.example.client.endpoints;

import com.example.client.model.Systemadministrator;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

import java.util.List;
//Methodenspiegelung vom Server RestController
public interface SystemadministratorEndpoint {

    @GET("/admin/alleAdmin")
    Call<List<Systemadministrator>> getAlleAdmins();

    @GET ("admin/alleAdminEmails")
    Call <List<Systemadministrator>>  getAlleEMailsVonAdmins();

    @POST("/admin/adminRegistrierung")
    Call<Systemadministrator> adminRegistrierung(@Body Systemadministrator systemadmin);
    @POST("/admin/adminLogin")
    Call<Systemadministrator> adminLogin(@Body Systemadministrator systemadmin);

}

