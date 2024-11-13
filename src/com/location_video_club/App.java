package com.location_video_club;

import java.util.Arrays;
import java.util.List;

public class App {
    public static void main(String[] args) {
        VideoClub club = new VideoClub();
        Genre genreAction=new Genre("Action", null);
        Film film1 = new Film("Inception", new Genre("Action",null), Arrays.asList(new Acteur("DiCaprio", "Leonardo")), true);
        Film film2 = new Film("The Godfather", new Genre("comedie",null), Arrays.asList(new Acteur("Pacino", "Al")), false);
        Coffret coffret1=new Coffret("El dorado", new Genre("Aventure",genreAction),Arrays.asList(new Acteur("Diakite", "Madiassa")), true,Arrays.asList(film1,film2));
        club.ajouterFilm(film1);
        club.ajouterFilm(film2);
        club.ajouterCoffret(coffret1);

        // Ajout de quelques abonnés
        Abonnee abonne1 = new Abonnee("Dupont", "Jean", 30, "M", 50000);
        Abonnee abonne2 = new Abonnee("Martin", "Marie", 25, "F", 40000);
        club.ajouterAbonnee(abonne1);
        club.ajouterAbonnee(abonne2);
        

        // Enregistrement de prêts
        club.enregistrerPrets(abonne1, film1);
        club.enregistrerPrets(abonne2, film2);
        abonne1.louerProduit(coffret1);

        // Sauvegarde des données dans MongoDB
        club.sauvegardeGenre();
        club.sauvegardeActeur();
        club.sauvegardeFilm();
        club.sauvegardeAbonnee();
        club.sauvegardeCoffret();
        System.out.println("Opérations terminées.");   

       /* VideoClub BOOOMS=VideoClub.chargementAll();
        System.out.println(BOOOMS.toString());*/
      
    

    }
    
}
