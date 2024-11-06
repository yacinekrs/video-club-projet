package com.location_video_club;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
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
    
    /**
     * enregistrer un pret d'un film par un abonnee
     * @param abonnee
     * @param film
     */
    public void enregistrerPrets(Abonnee abonnee, Film film) {
        if(abonnee ==null) {
            System.out.println("Abonnee vide");
            return;
        }
        if (film == null) {
            System.out.println("Film vide");
            return;
        }
        List<Film> films = produits.stream()
        .filter(produit -> produit instanceof Film)
        .map(produit -> (Film) produit)
        .collect(Collectors.toList());

      for(Film f : films) {
          if(f.getTitre().equalsIgnoreCase(film.getTitre()) 
          && f.getGenre().getNom().equalsIgnoreCase(film.getGenre().getNom()) 
          && f.getActeurs().containsAll(film.getActeurs()) 
          && f.getCouleur()==film.getCouleur()) {
            abonnee.louerProduit(f);
            return;
          }
        }
        System.out.println("Film introuvable");
    }   
/**
 * extraire les abonnee dans la meme fourchette de revenu
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
 * extraire les films similaire a un film  de titre donner
 * @param titre du film donner
 * @return liste de film qui sont similaire a notre titre de film
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

  /**
   * extraire les abonnees curieux 
   * @return liste d'abonnee curieux
   */
    public List<Abonnee> extraireAbonneeCurieux() {
        List<Abonnee> abonneesCurieux = new ArrayList<>();
        Map<Abonnee, Double> diversiteScores = new HashMap<>();
        
        // Calculer le score de diversité pour chaque abonné
        for (Abonnee abonnee : abonnees) {
            List<ProduitVideo> produitsLoues = abonnee.getProduits();
            List<Film> film_louer_abonnee = produitsLoues.stream()
                                .filter(produit -> produit instanceof Film)
                                .map(produit -> (Film) produit)
                                .collect(Collectors.toList());
            double scoreDiversite;
            
            if (produitsLoues.isEmpty()) {
                scoreDiversite = Double.MAX_VALUE; // Attribuer un score très élevé pour ceux sans films loués
            } else if (produitsLoues.size() == 1) {
                scoreDiversite = film_louer_abonnee.get(0).calculSimilarite(film_louer_abonnee.get(0));
            } else {
                scoreDiversite = calculerScoreDiversite(film_louer_abonnee);
            }
            
            diversiteScores.put(abonnee, scoreDiversite);
        }
        
        // Trouver le score minimum de diversité
        double minScoreDiversite = diversiteScores.values().stream()
                .min(Double::compareTo)
                .orElse(Double.MAX_VALUE);
        
        // Ajouter tous les abonnés avec le score minimum à la liste des abonnés curieux
        for (Map.Entry<Abonnee, Double> entry : diversiteScores.entrySet()) {
            if (entry.getValue() == minScoreDiversite) {
                abonneesCurieux.add(entry.getKey());
            }
        }
        
        return abonneesCurieux;
    }
    /**
     *  nous permet de calaculer la score de diversite de similarite 
     * @param film_louer_abonnee
     * @return Un score plus bas indique une plus grande diversité
     */
    private double calculerScoreDiversite(List<Film> film_louer_abonnee) {
        double scoreTotalSimilarite = 0;
        int comparaisons = 0;
        
        for (int i = 0; i < film_louer_abonnee.size(); i++) {
            for (int j = i + 1; j < film_louer_abonnee.size(); j++) {
    
                    Film film1 = film_louer_abonnee.get(i);
                    Film film2 =  film_louer_abonnee.get(j);
                    scoreTotalSimilarite += film1.calculSimilarite(film2);
                    comparaisons++;
                
            }
        }
        
        return comparaisons > 0 ? scoreTotalSimilarite / comparaisons : Double.MAX_VALUE; 
    }
/**
 * permet d'extraire les abonees  proches par rapport a la simularite
 * Calculer la similarité entre chaque abonné et le profiltype
 * Trier les abonnés par similarité (du plus similaire au moins similaire)
 * @param profilType  l'abonne en entree
 * @return liste  d'abonees qui sont proches
 */
    public List<Abonnee> extraireAbonneeProche(Abonnee profilType) {
        if (profilType == null) {
            System.out.println("Le profil type est vide");
            return null;
        }
        Map<Abonnee, Float> similarites = new HashMap<>();
        for (Abonnee abonne : abonnees) {
            float similarite = abonne.calculerSimilarite(profilType);
            similarites.put(abonne, similarite);
        }
        List<Abonnee> abonnesProches = similarites.entrySet().stream()
            .sorted(Map.Entry.comparingByValue())
            .map(Map.Entry::getKey)
            .collect(Collectors.toList());

        return abonnesProches;
    }

    public List<Film> extraireFilmsPublicType(float type) {
        return null;
    }

/**
 * 
 * @return une map de chaque  produit suivie de leur film similaire
 */
  public Map<ProduitVideo, List<Film>> extraireProduitSimilaire() {
        Map<ProduitVideo, List<Film>> produitsSimilaires = new HashMap<>();
        
        // Récupérer tous les films de la liste des produits
        List<Film> films = produits.stream()
            .filter(produit -> produit instanceof Film)
            .map(produit -> (Film) produit)
            .collect(Collectors.toList());
    
        // Pour chaque produit vidéo, déterminer les films similaires
        for (ProduitVideo produit : produits) {
            List<Film> filmsSimilaires = new ArrayList<>();
            
            if (produit instanceof Film) {
                Film filmActuel = (Film) produit;
                filmsSimilaires = trouverFilmsSimilaires(filmActuel, films);
            } else if (produit instanceof Coffret) {
                Coffret coffretActuel = (Coffret) produit;
                for (Film film : coffretActuel.getFilms()) {
                    filmsSimilaires.addAll(trouverFilmsSimilaires(film, films));
                }
            }
            
            produitsSimilaires.put(produit, filmsSimilaires);
        }
    
        return produitsSimilaires;
    }
    
/**
 * elle va nous permettre de trouver les films similaire
 * @param film
 * @param tousLesFilms
 * @return liste de ces films
 */
    private List<Film> trouverFilmsSimilaires(Film film, List<Film> tousLesFilms) {
        return tousLesFilms.stream()
            .filter(f -> !f.equals(film)) // Exclure le film lui-même
            .sorted(Comparator.comparingDouble(f -> film.calculSimilarite(f))) // Trier par similarité
            .collect(Collectors.toList()); // Collecter tous les films similaires sans limite
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
