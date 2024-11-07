package com.test_video_club;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.location_video_club.Abonnee;
import com.location_video_club.Film;
import com.location_video_club.Genre;
import com.location_video_club.ProduitVideo;

import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.List;

class AbonneeTest {

    private Abonnee abonnee;
    private ProduitVideo film1;
    private ProduitVideo film2;

    @BeforeEach
    void setUp() {
        abonnee = new Abonnee("Dupont", "Jean", 30, "Masculin", 40000);
        film1 = new Film("Film1", new Genre("Action", null), new ArrayList<>(), true);
        film2 = new Film("Film2", new Genre("Comédie", null), new ArrayList<>(), false);
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
    void testDeterminerSexe() {
        assertEquals(Abonnee.Sexe.Masculin, new Abonnee("Test", "Test", 20, "H", 30000).getSexe());
        assertEquals(Abonnee.Sexe.Feminin, new Abonnee("Test", "Test", 20, "F", 30000).getSexe());
        assertEquals(Abonnee.Sexe.Autre, new Abonnee("Test", "Test", 20, "X", 30000).getSexe());
    }


    @Test
    void testLouerProduit() {
        abonnee.louerProduit(film1);
        assertTrue(abonnee.getProduits().contains(film1));
        assertEquals(1, abonnee.getProduits().size());
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
        // La similarité exacte dépendra de l'implémentation, mais on peut vérifier qu'elle est dans une plage raisonnable
        assertTrue(similarite < 3);  // Ajustez cette valeur selon votre implémentation
    }
}