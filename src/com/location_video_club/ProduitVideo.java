package com.location_video_club;

import java.util.ArrayList;
import java.util.List;

public abstract class  ProduitVideo {
    protected final String titre;
    protected final Genre genre;
    protected final List<Acteur> acteurs;
 /**
  * Constructeur de la classe ProduitVideo.
  * @param atitre le nom du produit
  * @param agenre le  genre du produit
  * @param aActeurs la liste des acteurs
  */
    public ProduitVideo(final String  atitre, final Genre agenre, final List<Acteur> aActeurs) {
        this.titre = atitre;
        this.genre = agenre;
        if (aActeurs == null) {
            this.acteurs = new ArrayList<Acteur>();
        }
        else {      
            this.acteurs = aActeurs;
        }
    }


    /**
     * Retourne le titre du produit
     * @return
     */
    public String getTitre() { return titre; }
    /**
     * Retourne le genre du produit
     * @return
     */
    public Genre getGenre() { return genre; }
    /**
     * Retourne la liste des acteurs
     * @return
     */
    public List<Acteur> getActeurs() { return acteurs; } 
    /**
     * Ajoute un acteur au produit
     * @param aActeurs
     */
    public void addActeurs(final Acteur aActeurs) { this.acteurs.add(aActeurs); }
    
    @Override
    public String toString() {  
        return "ProduitVideo{" +
                "titre='" + titre + '\'' +
                ", genre=" + genre +
                ", acteurs=" + acteurs +
                '}';
    }

    @Override   
    public int hashCode() {  
        int result = 17;  
        result = 31 * result + (this.titre == null ? 0 : this.titre.hashCode());  
        result = 31 * result + (this.genre == null ? 0 : this.genre.hashCode());  
        result = 31 * result + (this.acteurs == null ? 0 : this.acteurs.hashCode());  
        return result;  
    }

      @Override  
    public boolean equals(Object o) {  
        if (this == o) return true;  
        if (!(o instanceof ProduitVideo produitVideo)) return false;  
        if (this.titre == null) {  
            if (produitVideo.titre != null) return false;  
        } else if (!this.titre.equals(produitVideo.titre)) return false;  
        if (this.genre == null) {  
            if (produitVideo.genre != null) return false;  
        } else if (!this.genre.equals(produitVideo.genre)) return false;  
        if (this.acteurs == null) {  
            if (produitVideo.acteurs != null) return false;  
        } else if (!this.acteurs.equals(produitVideo.acteurs)) return false;  
        return true;  
    }


}
