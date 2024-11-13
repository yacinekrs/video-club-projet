package com.test_video_club;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.location_video_club.Genre;

public class GenreTest {

    @Test
    public void testConstructorwithNAmeNull(){
      assertThrows(IllegalArgumentException.class, ()->{
        new Genre(null, null);
      });
    }  

    @Test   
    public void testConstructorWithCorrectParent() {
        Genre genre = new Genre("Aventure", new Genre("Action",null));
        assertEquals("Aventure", genre.getNom());
        assertEquals("Action", genre.getParent().getNom());
        assertEquals("Genre", genre.getParent().getParent().getNom());
    }

    @Test   
    public void testConstructorWithCorrectParent2() {
        Genre genre = new Genre("Western", new Genre("Action",null));
        assertEquals("Western", genre.getNom());
        assertEquals("Action", genre.getParent().getNom());
        assertEquals("Genre", genre.getParent().getParent().getNom());
    }

    @Test   
    public void testConstructorWithCorrectParent3() {
        Genre genre = new Genre("Romance", new Genre("Comédie",null));
        assertEquals("Romance", genre.getNom());
        assertEquals("Comédie", genre.getParent().getNom());
        assertEquals("Genre", genre.getParent().getParent().getNom());
    }

    @Test   
    public void testConstructorWithCorrectParent4() {
        Genre genre = new Genre("Musique", new Genre("Comédie",null));
        assertEquals("Musique", genre.getNom());
        assertEquals("Comédie", genre.getParent().getNom());
        assertEquals("Genre", genre.getParent().getParent().getNom());
    }

    @Test   
    public void testConstructorWithAction() {
        Genre genre = new Genre("Action", null);
        assertEquals("Action", genre.getNom());
        assertEquals("Genre", genre.getParent().getNom());
    }

    @Test   
    public void testConstructorWithComedie() {
        Genre genre = new Genre("Comédie", null);
        assertEquals("Comédie", genre.getNom());
        assertEquals("Genre", genre.getParent().getNom());
    }

    @Test   
    public void testConstructorWithWrongParent() {
         assertThrows(IllegalArgumentException.class, () -> {
            new Genre("Aventure", new Genre("Comédie", new Genre("Genre", null)));
        });
    }

    @Test
    public void testGetNom() {
        // Arrange
        Genre parent = new Genre("Action", null);
        Genre genre = new Genre("Aventure", parent);

        assertEquals("Aventure", genre.getNom());
    }

    @Test
    public void testGetParent() {

      Genre parent = new Genre("Action", null);
      Genre genre = new Genre("Aventure", parent);
      
      assertNotNull(genre.getParent());
      assertEquals(parent, genre.getParent());
    }

}
