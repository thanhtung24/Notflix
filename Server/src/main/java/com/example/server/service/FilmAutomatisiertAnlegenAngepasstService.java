package com.example.server.service;

import com.example.server.entities.*;
import com.example.server.repository.FilmCastRepository;
import com.example.server.repository.FilmRepository;
import com.example.server.repository.FilmStatistikRepository;
import com.example.server.repository.PersonRepository;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownHttpStatusCodeException;

import javax.imageio.ImageIO;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.sql.Timestamp;

@Service
@Component
public class FilmAutomatisiertAnlegenAngepasstService {

    @Autowired
    private FilmRepository filmRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private FilmCastRepository filmCastRepository;

    private List <FilmEntity> alleFilme =new ArrayList<>();

    private int filmCounter;

    private HtmlPage wikipedia;

    private WebClient client;

    @Autowired
    private FilmStatistikRepository filmStatistikRepository;

    private void connection(String url) {
        this.client = new WebClient();

        client.getOptions().setCssEnabled(false);
        client.getOptions().setJavaScriptEnabled(false);


        try {
            wikipedia = client.getPage(url);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fehler");
        }

    }


    public ResponseEntity scrapeScraper(String url, String xpath1, String xpath2, int modus, String bedingung) {

            filmCounter = 0;

            connection(url);

            List<String> filmTitel = new ArrayList<>();

            //bei horror filmen sollte i bei 2 anfangen
            for (int i = 3; i <= 5 && filmTitel.size() <= 150; i++) {

                HtmlTableBody tableBody = wikipedia.getFirstByXPath(xpath1 + i + xpath2);


                if (tableBody != null && filmTitel.size() <= 150) {
                    List<HtmlTableRow> liste = tableBody.getRows();

                    //wenn eine Tabelle mehr als acht zeilen hat
                    if (liste.size() > 8 && filmTitel.size() <= 150) {

                        int j = 0;

                        while (j < liste.size() && filmTitel.size() <= 150) {

                            HtmlTableRow row = liste.get(j);
                            HtmlAnchor filmNameInReihe = null;

                            //Idee : Modus 1 = Jahr, Modus 2-x = Kategorie
                            if(modus == 1){
                                filmNameInReihe = row.getFirstByXPath("td[*]/i/a");
                            } else {
                                filmNameInReihe = row.getFirstByXPath("td[*]/i/b/a");
                            }

                            //Bei drama kategorien sind die wikipediatabellen anders als bei den anderen kategorien aufgeteilt
                            if(url.contains("drama") ||url.contains("thriller")){
                                filmNameInReihe = row.getFirstByXPath("td[*]/i/a");
                            }

                            if (filmNameInReihe == null) {
                                System.out.println("Moeglicher Film hat kein filmNameInReihe");
                            } else {
                                //fuer die condition
                                filmTitel.add(filmNameInReihe.asNormalizedText());

                                System.out.println("Nr: " + filmCounter + " " + filmNameInReihe.asNormalizedText());

                                saveFilmInDatenbank(leerzeichenZuPlus(filmNameInReihe.asNormalizedText()), bedingung);
                            }
                            j++;
                            filmCounter++;
                        }
                    }
                    System.out.println("----------------------------------");
                }
            }
            //automatisch angelegte Filme in der LogDatei dokumentieren
            logDatei(url, alleFilme);



            this.client.close();
            filmTitel.clear();

            System.out.println("Am Ende der Scraper Methode");
            return new ResponseEntity(HttpStatus.OK);

    }

    public ResponseEntity scraper2(String url, String xpath1, String xpath2, String jahr){

        connection(url);

        int counter = 1;

        for(int i = 3; i <= 30; i++){
            HtmlUnorderedList unorderedList = wikipedia.getFirstByXPath(xpath1 + i + xpath2);
            if(unorderedList != null){
                int laenge = unorderedList.getChildElementCount();
                for(int j = 1; j <= laenge; j++){
                    HtmlListItem listItem = wikipedia.getFirstByXPath(xpath1 + i + "]/ul/li[" + j + "]");
                    String filmName = listItem.asNormalizedText();
                    if(filmName.contains("(film)") || filmName.contains("(" + jahr + " film)")){
                        filmName = filmName.replace("(film)", "");
                        filmName = filmName.replace("(" + jahr + " film)", "");
                    }
                    System.out.println("Nr: " + counter + " " + filmName);
                    saveFilmInDatenbank(leerzeichenZuPlus(filmName), jahr);
                    counter++;
                }
            }
        }
        System.out.println("Fertig mit scrapen und anlegen");
        return new ResponseEntity(HttpStatus.OK);
    }

    private Date datum;
    
    //Filmobjekt wird erstellt und in der Datenbank gespeichert
    private void saveFilmInDatenbank(String filmName, String bedingung){

        RestTemplate restTemplate = new RestTemplate();

        //Api wird aufgrufen fuer die Informationen des Films anhand ihres Namens vielleicht & year = jahr hinzufuegen
        FilmPOJO film = null;
        try {
            // xxx ist deine api key
            film = restTemplate.getForObject("http://www.omdbapi.com/?apikey=xxx" + filmName, FilmPOJO.class);
        } catch (UnknownHttpStatusCodeException e ) {
            e.printStackTrace();
            return;
        }

        if(film != null && film.getTitle() != null && film.getDirector() != null && film.getWriter() != null){

            //damit nur filme aus den passenden jahren angelegt werden oder nur filme mit der passenden kategorie
            if(bedingung.equals(film.getYear()) || film.getGenre().toLowerCase().contains(bedingung.toLowerCase())){

                System.out.println("Erwartetes Jahr: " + bedingung);
                System.out.println("Geliefertes Jahr: " + film.getYear());


                //Erst Regisseur und Autor anlegen
                PersonEntity regisseur = new PersonEntity(vorUndNachnameSeparieren(film.getDirector())[0], vorUndNachnameSeparieren(film.getDirector())[1]);
                PersonEntity autor = new PersonEntity(vorUndNachnameSeparieren(film.getWriter())[0], vorUndNachnameSeparieren(film.getWriter())[1]);

                //Regisseur und Autor in der Datenbank speichern
                Long regisseurId = personRepository.save(regisseur).getId();
                Long autorId = personRepository.save(autor).getId();

                //Film anlegen
                FilmEntity filmAnlegen = new FilmEntity(film.getTitle(), film.getGenre(), film.getRuntime(), film.getReleased(), regisseurId, autorId, downloadFilmBanner(film.getPoster()));
                Long filmId = filmRepository.save(filmAnlegen).getId();

                //Angelegten Film für FilmStatistikEntity
                zuFilmStatistikEntityhinzufuegen(filmAnlegen);

                String cast = film.getActors();
                String[] casts = cast.split(",");
                List<String> namenGetrennteListe = new ArrayList<>();
                List<PersonEntity> castMember = new ArrayList<>();

                for(String a : casts) {
                    String ready = a.replaceFirst("^ *", "");
                    namenGetrennteListe.add(ready);
                }
                for(String actors: namenGetrennteListe) {
                    HashMap<String, String> vorUndNachname = vorUndNachnameSeparierenNeu(actors);
                    for(Map.Entry<String, String> namenPaar : vorUndNachname.entrySet()) {
                        if(namenPaar.getKey() != null && namenPaar.getValue() != null){
                            PersonEntity castMitglied = new PersonEntity(namenPaar.getKey(), namenPaar.getValue());
                            castMember.add(castMitglied);
                        }
                    }
                }


                List<Long> idsVonCastMitgliedern = new ArrayList<>();
                for(PersonEntity mitglied: castMember){
                    Long id = personRepository.save(mitglied).getId();
                    idsVonCastMitgliedern.add(id);
                }

                for(Long alleIds : idsVonCastMitgliedern){
                    FilmCastEntity filmCastEntity = new FilmCastEntity(filmId, alleIds);
                    filmCastRepository.save(filmCastEntity);
                }

                //Film wird in eine Liste hinzugefuegt, fuer die Log Datei
                alleFilme.add(filmAnlegen);
            } else {
                System.out.println("Api liefert Filme die entweder kein uebereinstimmendes jahr oder entahltende kategorie hat");
            }



        } else {
            System.out.println("Ein Film muss einen Namen haben!");
        }
    }

    public void zuFilmStatistikEntityhinzufuegen(FilmEntity filmEntity){
        datum= new Date();
        FilmStatistikEntity filmStatistikEntity= new FilmStatistikEntity(filmEntity.getId(), datum);
        filmStatistikRepository.save(filmStatistikEntity);
    }


    private HashMap<String, String> vorUndNachnameSeparierenNeu(String vorUndNachname){
        String preb = vorUndNachname.replaceFirst(" ", "@");
        String[] strings = preb.split("@", 2);
        HashMap<String, String> map = new HashMap<>();
        for(int i = 0; i < strings.length - 1; i++) {
            map.put(strings[i], strings[i + 1]);
        }
        return map;
    }

    private String leerzeichenZuPlus(String filmName){
        String fertig = filmName.replace(" ", "+");
        return fertig;
    }


    private  String[] vorUndNachnameSeparieren(String vorUndNachname){
        String preb = vorUndNachname.replaceFirst(" ", "@");
        String[] strings = preb.split("@", 2);
        String[] ans = new String[2];
        for(int i = 0; i < strings.length - 1; i++) {
            ans[i] = strings[i];
            ans[i + 1] = strings[i + 1];
        }
        if(ans[0] != null && ans[1]!= null){
            return ans;
        } else if(ans[0] != null && ans[1] == null){
            return new String[]{ans[0], " "};
        } else if(ans[0] == null && ans[1] != null){
            return new String[]{" ",ans[1]};
        } else {
            return new String[]{" "," "};
        }
    }


    private final SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public void logDatei(String url, List<FilmEntity> filmListe){


        int filmNummer = 1;

        try (FileOutputStream fileOutputStream =new FileOutputStream(new File("log.txt"))){

            PrintWriter printWriter = new PrintWriter(fileOutputStream,true);

            printWriter.write(url);

            for (FilmEntity film : filmListe){

                printWriter.write("\n");
                Timestamp timestamp = new Timestamp(System.currentTimeMillis());
                printWriter.write(timeFormat.format(timestamp));
                printWriter.write("\n");
                printWriter.write("Film-Nummer: "+filmNummer);
                printWriter.write("\n");
                printWriter.write("Filmname: " +film.getName());
                printWriter.write("\n");
                printWriter.write("Kategorie: "+film.getKategorie());
                printWriter.write("\n");
                printWriter.write("Filmlaenge: "+film.getFilmLaenge());
                printWriter.write("\n");
                printWriter.write("Erscheinungsdatum: "+film.getErscheinungsdatum());
                printWriter.write("\n");


                PersonEntity regisseurPerson = personRepository.getById(film.getRegisseurId());

                printWriter.write("Regisseur: "+regisseurPerson.getVorname()+" "+regisseurPerson.getNachname());
                printWriter.write("\n");


                PersonEntity drehbuchautorPerson = personRepository.getById(film.getDrehbuchautorId());

                printWriter.write("Drehbuchautor: "+drehbuchautorPerson.getVorname()+" "+drehbuchautorPerson.getNachname());
                printWriter.write("\n");



                List<PersonEntity> cast = personRepository.findAllByFilmId(film.getId());


                printWriter.write("Cast: ");
                for (int i=0; i<cast.size() - 1;i++){
                    printWriter.write(cast.get(i).getVorname()+" "+cast.get(i).getNachname() + ", ");
                }

                try{
                    printWriter.write(cast.get(cast.size() - 1).getVorname()+" "+cast.get(cast.size() - 1).getNachname());
                } catch (IndexOutOfBoundsException e){
                    System.out.println("Fehler in der Log Datei beim Cast teil");
                }

                printWriter.write("\n");
                printWriter.write("\n");

                filmNummer++;
            }

            printWriter.close();

        } catch (IOException e){
            System.out.println("Fehler in der Log Methode");
        }
    }


    //https://stackoverflow.com/questions/28840604/how-to-initiate-a-file-download-in-the-browser-using-java
    private byte[] downloadFilmBanner(String url) {

        URL link = null;
        try {
            link = new URL(url);
        } catch (MalformedURLException e) {
            System.out.println("Kann keinen Filmbanner unter dem URL finden");
            return new byte[0];
        }

        InputStream in = null;
        try {
            in = new BufferedInputStream(link.openStream());
        } catch (IOException e) {
            System.out.println("Fehler beim Herunterladen von Filmbannern");
            return new byte[0];
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int n = 0;

        while (true){
            try {
                if (!(-1!=(n=in.read(buf)))) break;
            } catch (IOException e) {
                return new byte[0];
            }
            out.write(buf, 0, n);
        }

        try {
            out.close();
            in.close();
        } catch (IOException e) {
            return new byte[0];
        }

        byte[] response = out.toByteArray();

        return (response == null) ? new byte[0] : response;
    }

}
