package com.example.client.endpoints;

import com.example.client.model.Filmeinladung;
import com.example.client.model.Systemadministrator;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.*;

import java.util.List;

public interface MailEndpoint {

    @PUT("/email/sendMail")
    Call<String> mailSenden(@Query(value = "empfaenger") String empfaenger);


    @PUT ("/email/sendMailReport")
    Call <Boolean> reportMailSenden (@Query(value="empfaenger") String[] empfaenger);

    //Filmeinladung wurde gesendet
    @PUT ("/email/sendMailFilmeinladung")
    Call <Boolean> sendeFilmeinladungsMail (@Body Filmeinladung filmeinladung);

    //Filmeinladung wurde akzeptiert
    @PUT ("/email/sendMailFilmeinladungAkzeptiert")
    Call <Boolean> sendeFilmeinladungAkzeptiert (@Body Filmeinladung filmeinladung);


    @PUT ("/email/sendMailFilmeinladungAbgelehnt")
    Call <Boolean> sendeFilmeinladungAbgelehnt (@Body Filmeinladung filmeinladung);


    @GET("/email/getCode/{mail}/{code}")
    Call<Boolean> mailCode(@Path(value = "mail") String mail, @Path(value = "code") String code);
}
