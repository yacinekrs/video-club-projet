package com.test_video_club;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.location_video_club.*;
import java.util.*;

class VideoClubTest {

    private VideoClub videoClub;
    private Abonnee abonnee1, abonnee2;
    private Film film1, film2, film3,film4;
   
    private Genre genreAction, genreComedie;
    private List<Acteur> acteurs;

    // Yacine
    Genre genreActiony,genreComediey;
    Film film1y,film2y,film3y,film4y;
    List<ProduitVideo> produitsy = new ArrayList<>();
    Abonnee abonnee1y, abonnee3y;
    List<Abonnee> abonneesy = new ArrayList<>();
    List<Genre> genresy = new ArrayList<>();
    VideoClub videoCluby ;


    @BeforeEach
    void setUp() {
        videoClub = new VideoClub();
        genreAction = new Genre("Action", null);
        genreComedie = new Genre("Comédie", null);
        acteurs = Arrays.asList(new Acteur("Diakite", "Madiassa"), new Acteur("Khorsi", "Yacine"));
        
        abonnee1 = new Abonnee("Diallo", "Moussa", 30, "Masculin", 40000);
        abonnee2 = new Abonnee("Diallo", "Nadia", 25, "Feminin", 35000);
        
        film1 = new Film("Film Action", genreAction, acteurs, true);
        film2 = new Film("Film Comédie", genreComedie, acteurs, true);
        film3 = new Film("Autre Film Action", genreAction, acteurs, false);
        film4 = new Film("Autre Film Action", genreComedie, acteurs, false);

        //YAcine
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
  

    @Test
    void testAjouterAbonnee() {
        videoClub.ajouterAbonnee(abonnee1);
        assertNotNull(videoClub.trouveerAbonnee("Diallo"));
    }

    @Test
    void testAjouterAbonneeNull() {
        videoClub.ajouterAbonnee(null);
        assertTrue(videoClub.extraireAbonneeParRevenu(40000).isEmpty());
    }

    @Test
    void testTrouverAbonnee() {
        videoClub.ajouterAbonnee(abonnee1);
        assertEquals(abonnee1, videoClub.trouveerAbonnee("Diallo"));
        assertNull(videoClub.trouveerAbonnee("traore"));
    }

    @Test
    void testAjouterFilm() {
        videoClub.ajouterFilm(film1);
        assertFalse(videoClub.extraireFilmSimilaire("Film Action").isEmpty());
    }

    @Test
    void testAjouterFilmNull() {
        videoClub.ajouterFilm(null);
        assertThrows(IllegalArgumentException.class, () -> videoClub.extraireFilmSimilaire("Film Action"));
    }

    @Test
    void testEnregistrerPrets() {
        videoClub.ajouterAbonnee(abonnee1);
        videoClub.ajouterFilm(film1);
        videoClub.enregistrerPrets(abonnee1, film1);
        assertTrue(abonnee1.getProduits().contains(film1));
    }

    @Test
    void testEnregistrerPretsFilmNonExistant() {
        videoClub.ajouterAbonnee(abonnee1);
        videoClub.enregistrerPrets(abonnee1, film1);
        assertTrue(abonnee1.getProduits().isEmpty());
    }

    @Test
    void testExtraireAbonneeParRevenu() {
        videoClub.ajouterAbonnee(abonnee1);
        videoClub.ajouterAbonnee(abonnee2);
        List<Abonnee> abonneesMoyens = videoClub.extraireAbonneeParRevenu(40000);
        assertEquals(1, abonneesMoyens.size());
        assertTrue(abonneesMoyens.contains(abonnee1));
    }



    @Test
    void testExtraireFilmSimilaire() {
        videoClub.ajouterFilm(film1);
        videoClub.ajouterFilm(film2);
        videoClub.ajouterFilm(film3);
        List<Film> filmsSimilaires = videoClub.extraireFilmSimilaire("Film Action");
        assertTrue(filmsSimilaires.contains(film3));
        assertTrue(filmsSimilaires.contains(film2));
        assertFalse(filmsSimilaires.contains(film4));
    }

    @Test
    void testExtraireAbonneeCurieux() {
        videoClub.ajouterAbonnee(abonnee1);
        videoClub.ajouterAbonnee(abonnee2);
        videoClub.ajouterFilm(film1);
        videoClub.ajouterFilm(film2);
        videoClub.enregistrerPrets(abonnee1, film1);
        videoClub.enregistrerPrets(abonnee1, film2);
        videoClub.enregistrerPrets(abonnee2, film1);
        List<Abonnee> abonneesCurieux = videoClub.extraireAbonneeCurieux();
        assertTrue(abonneesCurieux.contains(abonnee1));
    }

    @Test
    void testExtraireAbonneeProche() {
        videoClub.ajouterAbonnee(abonnee1);
        videoClub.ajouterAbonnee(abonnee2);
        Abonnee profilType = new Abonnee("Test", "Test", 28, "Masculin", 38000);
        List<Abonnee> abonnesProches = videoClub.extraireAbonneeProche(profilType);
        assertEquals(abonnee1, abonnesProches.get(0));
    }

    @Test
    void testExtraireFilmsPublicTypeAvecSeuilSup() {
        videoClub.ajouterAbonnee(abonnee1);
        videoClub.ajouterAbonnee(abonnee2);
        videoClub.ajouterFilm(film1);
        videoClub.ajouterFilm(film2);
        videoClub.enregistrerPrets(abonnee1, film1);
        videoClub.enregistrerPrets(abonnee2, film1);
        List<Film> filmsPublicType = videoClub.extraireFilmsPublicType(2.5f);
        assertTrue(filmsPublicType.contains(film1));
    }

    @Test
    void testExtraireFilmsPublicTypeAvecSeuilInf() {
        videoClub.ajouterAbonnee(abonnee1);
        videoClub.ajouterAbonnee(abonnee2);
        videoClub.ajouterFilm(film1);
        videoClub.ajouterFilm(film2);
        videoClub.enregistrerPrets(abonnee1, film1);
        videoClub.enregistrerPrets(abonnee2, film1);
        List<Film> filmsPublicType = videoClub.extraireFilmsPublicType(0.5f);
        assertFalse(filmsPublicType.contains(film1));
    }

    @Test
    void testExtraireProduitSimilaire() {
        videoClub.ajouterFilm(film1);
        videoClub.ajouterFilm(film2);
        videoClub.ajouterFilm(film3);
        Map<ProduitVideo, List<Film>> produitsSimilaires = videoClub.extraireProduitSimilaire();
        assertTrue(produitsSimilaires.get(film1).contains(film3));
        assertTrue(produitsSimilaires.get(film1).contains(film2));
    }
}