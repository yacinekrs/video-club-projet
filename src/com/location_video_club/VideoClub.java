package com.location_video_club;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
public class VideoClub {
    private final List<ProduitVideo> produits;
    private final List<Abonnee> abonnees;
  /**
   * Constructeur pour creer un videoclub nouveau
   */
    public VideoClub() {
        this(null, null);
    }

    /**
     * Constructeur pour creer un videoclub existant
     * @param produits la listes des produits video 
     * @param abonnees la liste des abonnees
     */
    public VideoClub(List<ProduitVideo> produits, List<Abonnee> abonnees) {
       if (produits == null) {
            this.produits = new ArrayList<ProduitVideo>();
        } else {
            this.produits = produits;
        }
        if (abonnees == null) {
            this.abonnees = new ArrayList<Abonnee>();
        } else {
            this.abonnees = abonnees;
        }
    }

     /**
      * Ajoute un abonnee dans la liste des abonnees du videoclub
      * @param abonnee
      */
    public void ajouterAbonnee(Abonnee abonnee) {
        if (abonnee == null) {
            System.out.println("Abonnee vide");
            return; 
        }
        this.abonnees.add(abonnee);
    }

    /**
     * retouver un abonnee par son nom 
     * @param nom de l'abonnee a rechercher
     * @return null si l'abonnee n'existe pas sinon retourne l'abonnee
     */
    public Abonnee trouveerAbonnee(String nom) {
        for (Abonnee abonnee : abonnees) {
            if (abonnee.getNom().equalsIgnoreCase(nom)) {
                return abonnee;
            }
        }
        return null;
    }
/**
 * Ajoute un film dans la liste des films du videoclub
 * @param film
 */
     public void ajouterFilm(Film film) {
        if (film == null) {
            System.out.println("Film vide");
            return;
        }
        this.produits.add(film);
    }
    
    
    public void enregistrerPrets(Abonnee abonnee, Film film) {
       System.out.println("rien");
    }   
/**
 * extraire les abonnee dans la meme fourchette
 * @param diakite
 * @return null si l'abonnee est vide sinon retourne la liste des abonnee dans la meme fourchette
 */
    public List<Abonnee> extraireAbonneeParRevenu(Abonnee diakite) {
        List<Abonnee> abonnee_dans_meme_fourchette = new ArrayList<Abonnee>();
        if(diakite == null){
            System.out.println("l'abonnee est  vide");
            return null;
        }
        for (Abonnee abonnee : abonnees) { 
            if(abonnee.getFourchette().equals(diakite.getFourchette())){
                abonnee_dans_meme_fourchette.add(abonnee);
            }
        } 
        return abonnee_dans_meme_fourchette;
}

    public Genre extraireGenrePopulaires() {
        return null;
    }
/**
 * extraire les films similaire a un film donner
 * @param titre du film donner
 * @return liste de film qui sont similaire a notre film
 */
    public List<Film> extraireFilmSimilaire(String titre) {
        List<Film> film_plus_similaire=new ArrayList<Film>();
        List<Film> films = produits.stream()
            .filter(produit -> produit instanceof Film)
            .map(produit -> (Film) produit)
            .collect(Collectors.toList());
        Film monFilm= films.stream()
            .filter(film -> film.getTitre().equalsIgnoreCase(titre))
            .findFirst() 
            .orElse(null);
        float[] tab= new float[films.size()];
        for (Film f : films) {
            tab[films.indexOf(f)]=monFilm.calculSimilarite(f);
        }
        Arrays.sort(tab);
        for (int i = 0; i < (tab.length/2 )+1/2; i++) {
            for (Film f : films) {
                if (tab[i] == f.calculSimilarite(monFilm)) {
                    film_plus_similaire.add(f);
                }
            }
        }
        return film_plus_similaire;
    }

    public List<Abonnee> extraireAbonneeCurieux() {
        return null;
    }

    public List<Abonnee> extraireAbonneeProche() {
        return null;
    }

    public List<Film> extraireFilmsPublicType(float type) {
        return null;
    }

    public Map<ProduitVideo,List<Film>> extraireProduitSimilaire() {
        return null;
    }
    

    @Override
    public String toString() {
        return "VideoClub [produits=" + produits + ", abonnees=" + abonnees + "]";
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((abonnees == null) ? 0 : abonnees.hashCode());
        result = prime * result + ((produits == null) ? 0 : produits.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        VideoClub other = (VideoClub) obj;
        if (abonnees == null) {
            if (other.abonnees != null)
                return false;
        } else if (!abonnees.equals(other.abonnees))
            return false;
        if (produits == null) { 
            if (other.produits != null)
                return false;
        } else if (!produits.equals(other.produits))
            return false;    
        return true;            
    }
    
}
