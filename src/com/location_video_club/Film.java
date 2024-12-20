package com.location_video_club;

import java.util.List;
public class Film extends ProduitVideo{
    private final boolean couleur;
    /**
     * Constructeur d'un film
     * @param atitre le titre du film
     * @param agenre le genre du film
     * @param aActeurs la liste des acteurs
     * @param acouleur la couleur du film si c'est true le film est en couleur sinon c'est en  noir&blanc
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
        if (film == null) {
            return Float.MAX_VALUE;
        }

        int similarite_genre;
        int niveau = 0;
        Genre genre1 = this.getGenre();
        Genre genre2 = film.getGenre();
        

        while (genre1 != null || genre2 != null) {
            if (genre1 != null && genre2 != null && genre1.getNom().equalsIgnoreCase(genre2.getNom())) {
                similarite_genre = niveau; 
                break;
            }
            if (genre1 != null) {
                genre1 = genre1.getParent();}
            if (genre2 != null) {
                genre2 = genre2.getParent();}
                niveau = niveau+1;
        }
        similarite_genre = niveau;


        int similarite_couleur = (this.getCouleur() == film.getCouleur()) ? 0 : 1;
        int nombre_acteurs_total = this.getActeurs().size() + film.getActeurs().size();
        int nombre_acteur_commun = 0;
         
        for (Acteur acteur : this.getActeurs()) {
            for (Acteur acteur2 : film.getActeurs()) {
                if (acteur.getNom().equalsIgnoreCase(acteur2.getNom()) &&
                    acteur.getPrenom().equalsIgnoreCase(acteur2.getPrenom())) {
                    nombre_acteur_commun++;
                }
            }
        }

        int similarite_acteur = nombre_acteurs_total - nombre_acteur_commun;

        return similarite_genre + similarite_couleur + similarite_acteur;
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
