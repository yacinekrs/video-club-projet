package com.test_video_club;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.location_video_club.Abonnee;
import com.location_video_club.Film;
import com.location_video_club.Coffret;
import com.location_video_club.Genre;
import com.location_video_club.ProduitVideo;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class AbonneeTest {

    private Abonnee abonnee;
    private ProduitVideo film1;
    private ProduitVideo film2;
    private ProduitVideo coffret1;

    @BeforeEach
    void setUp() {
        Genre genre=new Genre("Genre", null);
        abonnee = new Abonnee("Dupont", "Jean", 30, "Masculin", 40000);
        film1 = new Film("Film1", new Genre("Action", genre), new ArrayList<>(), true);
        film2 = new Film("Film2", new Genre("Comédie", genre), new ArrayList<>(), false);
        List<Film> liste_film=new ArrayList<Film>();
        Film f1=(film1 instanceof Film)? (Film)film1:null;
        Film f2=(film2 instanceof Film)? (Film)film2:null;
        liste_film.add(f1);
        liste_film.add(f2);
        coffret1 = new Coffret("Coffret1", new Genre("Action", null ), new ArrayList<>(), true, liste_film);
    }

    @Test
    void testConstructeur() {
        assertEquals("Dupont", abonnee.getNom());
        assertEquals("Jean", abonnee.getPrenom());
        assertEquals(30, abonnee.getAge());
        assertEquals(Abonnee.Sexe.Masculin, abonnee.getSexe());
        assertEquals(40000, abonnee.getRevenue());
        assertEquals(Abonnee.Fourchette.Moyen, abonnee.getFourchette());
    }

    @Test
    void testConstructeurAvecListeProduits() {
        List<ProduitVideo> produits = new ArrayList<>();
        produits.add(film1);
        Abonnee abonneeAvecProduits = new Abonnee("Durand", "Marie", 25, "Feminin", 30000, produits);
        assertEquals(1, abonneeAvecProduits.getProduits().size());
        assertTrue(abonneeAvecProduits.getProduits().contains(film1));
    }

    @Test
    void testLouerProduit() {
        abonnee.louerProduit(film1);
        abonnee.louerProduit(coffret1);
        assertTrue(abonnee.getProduits().contains(film1));
        assertTrue(abonnee.getProduits().contains(coffret1));
        assertEquals(2, abonnee.getProduits().size());
    }

    @Test
    void testRendreProduit() {
        abonnee.louerProduit(film1);
        abonnee.louerProduit(film2);
        abonnee.rendreProduit(film1);
        assertFalse(abonnee.getProduits().contains(film1));
        assertTrue(abonnee.getProduits().contains(film2));
        assertEquals(1, abonnee.getProduits().size());
    }

    @Test
    void testCalculerSimilarite() {
        Abonnee autreAbonnee = new Abonnee("Martin", "Luc", 35, "Masculin", 50000);
        float similarite = abonnee.calculerSimilarite(autreAbonnee);
        assertTrue(similarite >= 0);
        assertTrue(similarite < 3);  // on doit ajuster cette valeur selon notre choix 
    }
}