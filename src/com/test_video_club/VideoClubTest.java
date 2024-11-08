package com.test_video_club;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.location_video_club.Abonnee;
import com.location_video_club.Film;
import com.location_video_club.Genre;
import com.location_video_club.ProduitVideo;
import com.location_video_club.VideoClub;

public class VideoClubTest {
  Genre genreActiony,genreComediey;
  Film film1y,film2y,film3y,film4y;
  List<ProduitVideo> produitsy = new ArrayList<>();
  Abonnee abonnee1y, abonnee3y;
  List<Abonnee> abonneesy = new ArrayList<>();
  List<Genre> genresy = new ArrayList<>();
  VideoClub videoCluby ;

  @BeforeEach
  public void setUpY(){
    genreComediey = new Genre("Comédie", null);
    genreActiony = new Genre("Action", null);
    film1y = new Film("film1y", new Genre("Action", null), new ArrayList<>(), true);
    film2y = new Film("film2y", new Genre("Comédie", null), new ArrayList<>(), false);
    film4y = new Film("film4y", new Genre("Comédie", null), new ArrayList<>(), false);
    film3y = new Film("film3y", new Genre("Comédie", null), new ArrayList<>(), false);
    produitsy.add(film1y); produitsy.add(film2y); 
    produitsy.add(film3y); produitsy.add(film4y);
    abonnee1y = new Abonnee("toto1", "Titi1", 30, "Masculin", 40000,produitsy); 
    abonnee3y = new Abonnee("toto3", "Titi3", 23, "Feminin", 35000); 
    abonneesy.add(abonnee3y);
    abonneesy.add(abonnee1y);
    videoCluby = new VideoClub(null, abonneesy);
  }

  @Test
  public void testextraireGenrePopulaires(){
    genresy.add(genreComediey);
    assertEquals(genresy, videoCluby.extraireGenrePopulaires());
  
  }

  @Test
  public void testextraireGenrePopulairesAfterAddActionGerne(){
    Film film5 = new Film("film1y", new Genre("Action", null), new ArrayList<>(), true);
    Film film6 = new Film("film1y", new Genre("Action", null), new ArrayList<>(), true);
    Film film7 = new Film("film1y", new Genre("Action", null), new ArrayList<>(), true);
    produitsy.add(film5); 
    produitsy.add(film7); 
    produitsy.add(film6);
    genresy.add(genreActiony);
    assertEquals(genresy, videoCluby.extraireGenrePopulaires());
  }

  @Test
  public void testextraireGenrePopulaireswhenComedieAndComedieIsEqual(){
    Film film5 = new Film("film1y", new Genre("Action", null), new ArrayList<>(), true);
    Film film7 = new Film("film1y", new Genre("Action", null), new ArrayList<>(), true);
    produitsy.add(film5); 
    produitsy.add(film7); 
    genresy.add(genreComediey);
    genresy.add(genreActiony);
    assertEquals(genresy, videoCluby.extraireGenrePopulaires());
  }
}
