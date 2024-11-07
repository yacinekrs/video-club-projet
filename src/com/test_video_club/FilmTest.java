package com.test_video_club;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.location_video_club.Film;
import com.location_video_club.Genre;
import com.location_video_club.Acteur;
import java.util.ArrayList;
import java.util.List;

class FilmTest {

    private Film film,film1,film3t,film4t;
    private Genre genreAction,genreComedie;
    private List<Acteur> acteurs,acteurs2;

    @BeforeEach
    void setUp() {
        genreAction = new Genre("Action", null);
        genreComedie = new Genre("Comedie", null);
        acteurs = new ArrayList<>();
        acteurs2 = new ArrayList<>();
        acteurs2.add(new Acteur("Diabi", "Moussa"));
        acteurs2.add(new Acteur("Moussa", "Diabate"));
        acteurs.add(new Acteur("Diakite", "Madiassa"));
        acteurs.add(new Acteur("Yacine", "Khorsi"));
        acteurs.add(new Acteur("Moussa", "Diabate"));
        film = new Film("Test Film Action", genreAction, acteurs, true);
        film1 = new Film("Test Film comedie", genreComedie, acteurs, false);
        film3t = new Film("film", genreComedie, acteurs2, true);
        film4t = new Film("film", genreComedie, acteurs, false);
    }

    @Test
    void testConstructeur() {
        assertEquals("Test Film Action", film.getTitre());
        assertEquals("Test Film comedie", film1.getTitre());
        assertEquals(genreAction, film.getGenre());
        assertEquals(genreComedie, film1.getGenre());
        assertEquals(acteurs, film.getActeurs());
        assertEquals(acteurs, film1.getActeurs());
        assertTrue(film.getCouleur());
        assertFalse(film1.getCouleur());
    }

    @Test
    void testConstructeurAvecListeActeursNull() {
        assertThrows(IllegalArgumentException.class, () -> new Film("Film Sans Acteurs", genreAction, null, false));
    }
    @Test
    void testConstructeurAvecGenreNull() {
        assertThrows(IllegalArgumentException.class, () -> new Film("Film Sans Genre", null, acteurs, true));
    }

    @Test
    void testGetCouleur() {
        assertTrue(film.getCouleur());
        Film filmNoirEtBlanc = new Film("Film Noir et Blanc", genreComedie, acteurs, false);
        assertFalse(filmNoirEtBlanc.getCouleur());
    }

    @Test
    void testCalculSimilariteMemesFilms() {
        Film filmIdentique = new Film("Test Film", genreAction, acteurs, true);
        assertEquals(acteurs.size(), film.calculSimilarite(filmIdentique));
    }

    @Test
    void testCalculSimilariteFilmsDifferents() {
        Genre genreComedie = new Genre("Comédie", null);
        List<Acteur> autresActeurs = new ArrayList<>();
        autresActeurs.add(new Acteur("John", "lakamy"));
        Film autreFfilm = new Film("Autre Film", genreComedie, autresActeurs, false);
        
        float similarite = film.calculSimilarite(autreFfilm);
        assertTrue(similarite >=4);
    }

    @Test
    void testCalculSimilariteFilmsDifferents2() {
        float similarite = film3t.calculSimilarite(film4t);
        assertEquals(5, similarite);
    }
 

    @Test
    void testCalculSimilariteAvecFilmNull() {
        assertEquals(Float.MAX_VALUE, film.calculSimilarite(null));
    }
    @Test
    void testAddActeurs() {
        Acteur nouvelActeur = new Acteur("kola", "moussa");
        film.addActeurs(nouvelActeur);
        assertTrue(film.getActeurs().contains(nouvelActeur));
    }
}
