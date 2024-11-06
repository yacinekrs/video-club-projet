package com.location_video_club;
import java.util.ArrayList;
import java.util.HashMap;
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


    /**
     * Enregistre le prêt d'un film à un abonné.
     *
     * Vérifie que l'abonné et le film existent dans les listes respectives avant d'ajouter
     * l'entrée dans la map des prêts, où l'abonné est la clé et le film est la valeur.
     *
     * @param abonnee L'abonné empruntant le film (ne doit pas être null).
     * @param film    Le film à prêter (ne doit pas être null).
     */
    public void enregistrerPret(Abonnee abonnee, Film film) {
    }

    public List<Abonnee> extraireAbonneeParRevenu() {

        return null;    
    }

    public Genre extraireGenrePopulaires() {
        Map<Genre, Integer> compteur = new HashMap<>();
        int maxNb = 0;
        Genre genrePopulaire = null;

        for (Abonnee a : abonnees) {
            List<ProduitVideo> listProduitsVideo = a.getProduits_louer();

            // Parcourt chaque produit vidéo loué par cet abonné
            for (ProduitVideo produit : listProduitsVideo) {
                // Récupère le genre du produit vidéo et incrémente son compteur dans la map
                Genre genre = produit.getGenre();
                compteur.put(genre, compteur.getOrDefault(genre, 0) + 1);
            }
        }

        for (Map.Entry<Genre, Integer> entry : compteur.entrySet()) {
            if (entry.getValue() > maxNb) {
                maxNb = entry.getValue();
                genrePopulaire = entry.getKey();
            }
        }
        return genrePopulaire;
    }

    public List<Film> extraireFilmSimilaire(String titre) {
        return null;
    }

    public List<Abonnee> extraireAbonneeCurieux() {
        return null;
    }

    public List<Film> extraireFilmsPublicType(float seuil) {
        Map<Film, List<Abonnee>> filmsAbonnesMap = new HashMap<>();
        List<Film> filmsPublicType = new ArrayList<>();

        for (Abonnee a : abonnees) {
            List<ProduitVideo> listProduitsVideo = a.getProduits_louer();
            for (ProduitVideo produit : listProduitsVideo) {
                if (produit instanceof Film) {
                    Film film = (Film) produit;
                    filmsAbonnesMap.putIfAbsent(film, new ArrayList<>());
                    filmsAbonnesMap.get(film).add(a);
                }
            }
        }

        for (Map.Entry<Film, List<Abonnee>> entry : filmsAbonnesMap.entrySet()) {
            float similariteTotale = 0;
            List<Abonnee> listAbonnee = entry.getValue();

            // Si le film a moins de deux abonnés, on ne peut pas calculer de similarité
            if (listAbonnee.size() < 2) {
                continue;
            }

            for (int i = 0; i < listAbonnee.size(); i++) {
                Abonnee ab1 = listAbonnee.get(i);  // Premier abonné
                for (int j = i + 1; j < listAbonnee.size(); j++) {
                    Abonnee ab2 = listAbonnee.get(j);  // Deuxième abonné
                    similariteTotale += ab1.calculerSimilarite(ab2);
                }
            }
            float moyenneSimilarite = similariteTotale / listAbonnee.size();

            if (moyenneSimilarite < seuil) {
                filmsPublicType.add(entry.getKey());
            }
        }
        return filmsPublicType;
    }


    public List<Abonnee> extraireAbonneProche() {
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
