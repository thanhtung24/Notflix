package com.example.server.restcontroller;

import com.example.server.service.FilmAutomatisiertAnlegenAngepasstService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashSet;

@RestController
@RequestMapping("/filmAutomatisiertAnlegen")
public class FilmAutomatisiertAnlegenAngepasstRestController {

    @Autowired
    private FilmAutomatisiertAnlegenAngepasstService filmAutomatisiertAnlegenAngepasstService;

    private String XPATH1 ="/html/body/div[3]/div[3]/div[5]/div[1]/table[";
    private String XPATH2 ="]/tbody";


    //Filme anhand Jahr automatisch anlegen
    @PostMapping("/jahr")
    public ResponseEntity<String> automatisierteFilmeAnhandZeitraum(@RequestBody String[] zeitraum){
        if(zeitraum.length == 0){
            return new ResponseEntity("", HttpStatus.BAD_REQUEST);
        }
        String startJahr = zeitraum[0];
        String endJahr = zeitraum[1];
        int laengeDerAbstaende = Integer.parseInt(endJahr) - Integer.parseInt(startJahr);
        ArrayList<String> jahrListe = new ArrayList<>();
        jahrListe.add(startJahr);

        for(int i = 1; i < laengeDerAbstaende; i++) {
            Integer ja = Integer.parseInt(startJahr) + i;
            String jahre = String.valueOf(ja);
            jahrListe.add(jahre);
        }
        jahrListe.add(endJahr);

        System.out.println(jahrListe);
        HashSet<Integer> set = new HashSet<>();
        for(String jahr : jahrListe){
            int code = filmAutomatisiertAnlegenAngepasstService.scrapeScraper("https://en.wikipedia.org/wiki/List_of_American_films_of_" + jahr, XPATH1, XPATH2, 1, jahr).getStatusCodeValue();
            set.add(code);
        }


        if(set.size() == 1){
            set.clear();
            return new ResponseEntity<>("",HttpStatus.OK);
        } else {
            set.clear();
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/kategorie")
    public ResponseEntity<String> automatisierteFilmeAnhandKategorie(@RequestBody String kategorie){

        StringBuilder stringBuilder = new StringBuilder(kategorie);
        stringBuilder.deleteCharAt(kategorie.length() - 1);
        stringBuilder.deleteCharAt(0);
        String gewaehltekategorie = stringBuilder.toString();
        System.out.println("Gewaehlte kategorie: " + gewaehltekategorie);


        int code = filmAutomatisiertAnlegenAngepasstService.scrapeScraper("https://en.wikipedia.org/wiki/List_of_" + gewaehltekategorie + "_films_of_the_2020s", XPATH1, XPATH2, 2, gewaehltekategorie).getStatusCodeValue();
        if(code == 200){
            return new ResponseEntity<>("", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("", HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/kategorieJahr")
    public ResponseEntity<String> automatisierteFilmeAnhandKategorieUndJahr(@RequestBody String[] kategorieUndJahr){

        System.out.println("Im Controller fuer Kategorie und Jahr");

        String xpath1 = "/html/body/div[3]/div[3]/div[5]/div[2]/div[2]/div/div/div[";

        String xpath2 = "]/ul";

        String startJahr = kategorieUndJahr[0];
        String endJahr = kategorieUndJahr[1];

        int laengeDerAbstaende = Integer.parseInt(endJahr) - Integer.parseInt(startJahr);
        ArrayList<String> jahrListe = new ArrayList<>();
        jahrListe.add(startJahr);

        for(int i = 1; i < laengeDerAbstaende; i++) {
            Integer ja = Integer.parseInt(startJahr) + i;
            String jahre = String.valueOf(ja);
            jahrListe.add(jahre);
        }
        jahrListe.add(endJahr);


        String kategorie = kategorieUndJahr[2];
        HashSet<Integer> set = new HashSet<>();

        for(String jahr : jahrListe){
            String baseUrl = "https://en.wikipedia.org/wiki/Category:2020_drama_films";
            //int code = filmAutomatisiertAnlegenAngepasstService.scraper2("https://en.wikipedia.org/wiki/Category:" + jahr + "_" + kategorie + "_films", xpath1, xpath2, jahr).getStatusCodeValue();
            int code;
            if(kategorie.equalsIgnoreCase("crime")){
                // crime_thriller tabellen sind wieder anders aufgebaut
                String altXpath = "/html/body/div[3]/div[3]/div[5]/div[2]/div/div/div/div[";
                String altXpath2 = "]/ul";
                code = filmAutomatisiertAnlegenAngepasstService.scraper2("https://en.wikipedia.org/wiki/Category:" + jahr + "_" + kategorie + "_thriller_films", altXpath, altXpath2, jahr).getStatusCodeValue();
                //code = filmAutomatisiertAnlegenAngepasstService.scraper2("https://en.wikipedia.org/wiki/Category:" + jahr + "_" + kategorie + "_thriller_films", xpath1, xpath2, jahr).getStatusCodeValue();


            } else {
                code = filmAutomatisiertAnlegenAngepasstService.scraper2("https://en.wikipedia.org/wiki/Category:" + jahr + "_" + kategorie + "_films", xpath1, xpath2, jahr).getStatusCodeValue();
            }



            set.add(code);
        }

        return new ResponseEntity<>("", HttpStatus.OK);

    }

}
