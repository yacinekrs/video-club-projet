package com.test_video_club;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import com.location_video_club.Acteur;
import com.location_video_club.Coffret;
import com.location_video_club.Film;
import com.location_video_club.Genre;

public class CoffretTest {
  private Genre genre;
  private Acteur acteur1, acteur2, acteur3, acteur4;
  private List<Acteur> listeActeur = new ArrayList<>();
  private List<Acteur> listeActeur2 = new ArrayList<>();
  private Film film, film2;
  private List<Film> listeFilm = new ArrayList<>();

  @BeforeEach
  public void setUp(){
    genre = new Genre("Aventure", new Genre("Action",null));
    acteur1 = new Acteur("toto1", "titi1");
    acteur2 = new Acteur("toto2", "titi2");
    acteur3 = new Acteur("toto3", "titi3");
    acteur4 = new Acteur("toto4", "titi4");
    listeActeur.add(acteur1); listeActeur.add(acteur2);
    listeActeur.add(acteur3); listeActeur2.add(acteur3);
    listeActeur2.add(acteur4);
    film = new Film("titreF", genre, listeActeur,true);
    film2 = new Film("titreF", genre, listeActeur2,false);
    listeFilm.add(film); 
    listeFilm.add(film2); 
  }

  @Test
  public void testConstructor(){
    
    Coffret coffret = new Coffret("titre", genre, listeActeur, false, listeFilm);
    assertEquals("titre",coffret.getTitre());
    assertEquals(genre, coffret.getGenre());
    assertEquals(listeActeur, coffret.getActeurs());
    assertEquals(listeFilm, coffret.getFilms());
    assertEquals(false, coffret.isAbonus());
  }

  @Test
  public void testConstructorWithFilmNull(){

    Coffret coffret = new Coffret("titre", genre, listeActeur, false, null);
    assertEquals("titre",coffret.getTitre());
    assertEquals(genre, coffret.getGenre());
    assertEquals(listeActeur, coffret.getActeurs());
    assertEquals(new ArrayList<Film>(), coffret.getFilms());
    assertEquals(false, coffret.isAbonus());
  }

  @Test
  public void testaddFilmNull(){

    Coffret coffret = new Coffret("titre", genre, listeActeur, false, null);
    assertEquals(new ArrayList<Film>(), coffret.getFilms());
    coffret.addFilms(film);
    assertTrue(coffret.getFilms().contains(film));
  }

  @Test
  public void testaddFilm(){

    Coffret coffret = new Coffret("titre", genre, listeActeur, false, listeFilm);
    coffret.addFilms(film);
    assertTrue(coffret.getFilms().contains(film));
    assertEquals(3, coffret.getFilms().size());
  }

  @Test
  public void testcalculSimilarite(){
    Coffret coffret1 = new Coffret("titre1", genre, listeActeur, false, listeFilm);
    Float cal = coffret1.calculSimilarite(film2);
    assertEquals(7.0f, cal, 0);
  }


 

}
