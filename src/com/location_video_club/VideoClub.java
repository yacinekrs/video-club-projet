package com.location_video_club;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

     
    public void ajouterAbonnee(Abonnee abonnee) {
        this.abonnees.add(abonnee);
    }

    public Abonnee trouveerAbonnee(String nom) {
        for (Abonnee abonnee : abonnees) {
            if (abonnee.getNom().equalsIgnoreCase(nom)) {
                return abonnee;
            }
        }
        return null;
    }

     public void ajouterFilm(Film film) {
        this.produits.add(film);
    }
    
    
    public void enregistrerPrets(Abonnee abonnee, Film film) {
       System.out.println("rien");
    }   

    public List<Abonnee> extraireAbonneeParRevenu() {
        return null;    
    }

    public Genre extraireGenrePopulaires() {
        return null;
    }

    public List<Film> extraireFilmSimilaire(String titre) {
        return null;
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
