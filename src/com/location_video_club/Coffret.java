package com.location_video_club;
import java.util.ArrayList;
import java.util.List;
public class Coffret extends ProduitVideo {
    private final boolean abonus;
    private final List<Film> films ;

    /**
     *  le contructeur de Coffret
     * @param atitre le titre du coffret 
     * @param agenre le genre du coffret 
     * @param aActeurs la listes des acteurs du coffret
     * @param abonus  le bonus  true si il ya un bonus sinon false
     */
    public Coffret(final String atitre, final Genre agenre, final List<Acteur> aActeurs, final boolean abonus, final List<Film> films) {
          
        super(atitre, agenre, aActeurs);
        this.abonus = abonus;
        if(films == null){
            this.films = new ArrayList<Film>();
        }else{
            this.films = films;
        }
    }

    /**
     * verifier si le coffret a un bonus
     * @return true or false
     */
    public boolean isAbonus() {
        return abonus;
    }
    /**
     * ajoute un film a la liste des films du coffret 
     * @param film 
     */
    public void addFilms(Film film) {
        this.films.add(film);
    }
    /**
     *  
     * @return la liste des films du coffret
     */
    public List<Film> getFilms(){
        return this.films;
    }

    /**
     * Retourne la similarité de  un films et les films du coffret
     * @param film 
     */
    public float calculSimilarite(Film film) {
        float[] tab=new float[this.films.size()];
        for (Film f : this.films) {
            tab[this.films.indexOf(f)]=f.calculSimilarite(film);  
        }
        float max = 0;    
        for (int i = 0; i < tab.length; i++) {
            if (tab[i] > max) {
                max = tab[i];
            }
        }
        int bonus = isAbonus() ? 1 : 0;
        return max+this.films.size()+bonus;
    }

    @Override
    public String toString() {
        return "Coffret{" +    
                "  abonus=" + abonus +
                ", films=" + films +
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
        result = 31 * result + (this.abonus ? 1 : 0);  
        result = 31 * result + (this.films == null ? 0 : this.films.hashCode());  
        return result;  
    }   
    @Override
    public boolean equals(Object o) {   
        if (this == o) return true;   
        if (!(o instanceof Coffret coffret)) return false;   
        if (this.titre == null) {   
            if (coffret.titre != null) return false;   
        } else if (!this.titre.equals(coffret.titre)) return false;   
        if (this.genre == null) {   
            if (coffret.genre != null) return false;   
        } else if (!this.genre.equals(coffret.genre)) return false;   
        if (this.acteurs == null) {   
            if (coffret.acteurs != null) return false;   
        } else if (!this.acteurs.equals(coffret.acteurs)) return false;   
        if (this.abonus != coffret.abonus) return false;   
        if (this.films == null) {   
            if (coffret.films != null) return false;   
        } else if (!this.films.equals(coffret.films)) return false;   
        return true;   
    }
}
