package com.example.client.endpoints;

import com.example.client.controller.NutzerverhaltenStatistikController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class RetrofitClient {

    @Bean
    public Retrofit retrofit() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8080")
                .addConverterFactory(JacksonConverterFactory.create())
                .build();
        return  retrofit;
    }

    @Bean
    public SystemadministratorEndpoint systemadministratorEndpoint(Retrofit retrofit) {
        return retrofit.create(SystemadministratorEndpoint.class);
    }

    @Bean
    public FilmlisteEndpoint filmlisteEndpoint(Retrofit retrofit) {
        return retrofit.create(FilmlisteEndpoint.class);
    }

    @Bean
    public FilmManuellAnlegenUndBearbeitenEndpoint FilmManuellAnlegenEndpoint(Retrofit retrofit) { return retrofit.create(FilmManuellAnlegenUndBearbeitenEndpoint.class); }

    @Bean
    public PersonAnlegenEndpoint personAnlegenEndpoint(Retrofit retrofit) { return retrofit.create(PersonAnlegenEndpoint.class); }

    @Bean
    public FilmuebersichtEndpoint filmuebersichtEndpoint(Retrofit retrofit) { return retrofit.create((FilmuebersichtEndpoint.class)); }

    @Bean
    public FilmCastEndpoint filmCastEndpoint(Retrofit retrofit) { return retrofit.create((FilmCastEndpoint.class)); }

    @Bean
    public FilmAutomatisiertAnlegenAngepasstEndpoint filmAutomatisiertAnlegenAngepasstEndpoint(Retrofit retrofit){ return retrofit.create(FilmAutomatisiertAnlegenAngepasstEndpoint.class); }

    @Bean
    public NutzerEndpoint nutzerEndpoint(Retrofit retrofit){
        return retrofit.create(NutzerEndpoint.class);
    }

    @Bean
    public MailEndpoint mailEndpoint(Retrofit retrofit){ return retrofit.create(MailEndpoint.class); }

    @Bean
    public Reportfehler_NutzerEndpoint reportfehler_nutzerEndpoint(Retrofit retrofit) {return retrofit.create(Reportfehler_NutzerEndpoint.class);
    }

    @Bean
    public ReportanfrageAdminEndpoint reportanfrageAdminEndpoint(Retrofit retrofit) {return retrofit.create(ReportanfrageAdminEndpoint.class);
    }

    @Bean
    public WatchListEndpoint watchListEndpoint(Retrofit retrofit){return retrofit.create(WatchListEndpoint.class);}

    @Bean
    public Admin_KommentarEndpoint admin_kommentarEndpoint(Retrofit retrofit){return retrofit.create(Admin_KommentarEndpoint.class);}

    @Bean
    public NutzersucheEndpoint nutzersucheEndpoint(Retrofit retrofit){ return retrofit.create(NutzersucheEndpoint.class); }

    @Bean
    public FreundschaftsanfrageEndpoint freundschaftsanfragenEndpoint(Retrofit retrofit){ return retrofit.create(FreundschaftsanfrageEndpoint.class); }

    @Bean
    public ProfilAndererNutzerEndpoint profilAndererNutzerEndpoint(Retrofit retrofit){ return retrofit.create(ProfilAndererNutzerEndpoint.class); }

    @Bean
    public PrivateEinstellungenEndpoint privateEinstellungenEndpoint(Retrofit retrofit){ return retrofit.create(PrivateEinstellungenEndpoint.class); }

    @Bean
    public PrivaterChatEndpoint privaterChatEndpoint(Retrofit retrofit){ return retrofit.create(PrivaterChatEndpoint.class);}

    @Bean
    public FreundschaftEndpoint freundeEndpoint(Retrofit retrofit){ return retrofit.create(FreundschaftEndpoint.class);}

    @Bean
    public FilmbewertungEndpoint filmbewertungEndpoint(Retrofit retrofit){ return retrofit.create(FilmbewertungEndpoint.class);}

    @Bean
    public BereitsGesehenEndpoint bereitsGesehenEndpoint(Retrofit retrofit){ return retrofit.create(BereitsGesehenEndpoint.class);}

    @Bean
    public FilmeinladungSendenEndpoint filmeinladungSendenEndpoint (Retrofit retrofit){
        return retrofit.create(FilmeinladungSendenEndpoint.class);}

    @Bean
    public DiskussionsGruppeEndpoint diskussionsGruppeEndpoint(Retrofit retrofit){ return retrofit.create(DiskussionsGruppeEndpoint.class);}

    @Bean
    public FilmStatistikEndpoint filmStatistikEndpoint(Retrofit retrofit){return retrofit.create(FilmStatistikEndpoint.class);}

    @Bean
    public FilmVorschlagenEndpoint filmVorschlagenEndpoint(Retrofit retrofit){ return retrofit.create(FilmVorschlagenEndpoint.class);}

    @Bean
    public KalenderEndpoint kalenderEndpoint(Retrofit retrofit){ return retrofit.create(KalenderEndpoint.class);}

    @Bean
    public NutzerverhaltenStatistikEndpoint nutzerverhaltenStatistikEndpoint(Retrofit retrofit){ return retrofit.create(NutzerverhaltenStatistikEndpoint.class);}

    @Bean
    public NotizEndpoint notizEndpoint(Retrofit retrofit){ return retrofit.create(NotizEndpoint.class);}
}