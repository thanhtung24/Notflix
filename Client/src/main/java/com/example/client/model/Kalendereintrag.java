package com.example.client.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Kalendereintrag {
       public static List<Filmbewertung> bewertungen;
       public static List<WatchListItem> watchListItems;
       public static List<BereitsGesehenItem> bereitsGesehenItems;
       public static List<Filmeinladung> filmeinladungen;
       public static List<Notiz> notizen;
}
