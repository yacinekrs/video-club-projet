package com.location_video_club;

import java.util.List;
public class Film extends ProduitVideo{
    private final boolean couleur;
    /**
     * Constructeur d'un film
     * @param atitre le titre du film
     * @param agenre le genre du film
     * @param aActeurs la liste des acteurs
     * @param acouleur la couleur du film si c'est 1 le film est en couleur sinon c'estv noir&blanc
     */
    public Film(final String atitre, final Genre agenre, final List<Acteur> aActeurs, final boolean acouleur) {
        super(atitre, agenre, aActeurs);
        this.couleur = acouleur;
    }

    /**
     * Retourne true or false pour la couleur du film   
     * @return 
     */
    public boolean getCouleur() {   
        return couleur;          
    }
    /**
     * Retourne la similarité de deux films 
     * @param film
     */
    public float calculSimilarite(Film film) {
        return 0;
    }

    @Override
    public String toString() {  
        return "Film{" +    
                "couleur=" + couleur +
                ", titre='" + getTitre() + '\'' +
                ", genre=" + getGenre() +
                ", acteurs=" + getActeurs() +
                '}';
    }   

    @Override   
    public int hashCode() {  
        int result = 17;  
        result = 31 * result + (this.titre == null ? 0 : this.titre.hashCode());  
        result = 31 * result + (this.genre == null ? 0 : this.genre.hashCode());  
        result = 31 * result + (this.acteurs == null ? 0 : this.acteurs.hashCode());  
        result = 31 * result + (this.couleur ? 1 : 0);  
        return result;  
    }
    
    @Override
    public boolean equals(Object o) {  
        if (this == o) return true;  
        if (!(o instanceof Film film)) return false;  
        if (this.titre == null) {  
            if (film.titre != null) return false;  
        } else if (!this.titre.equals(film.titre)) return false;  
        if (this.genre == null) {  
            if (film.genre != null) return false;  
        } else if (!this.genre.equals(film.genre)) return false;  
        if (this.acteurs == null) {  
            if (film.acteurs != null) return false;  
        } else if (!this.acteurs.equals(film.acteurs)) return false;  
        return this.couleur == film.couleur;  
    }

    
}
