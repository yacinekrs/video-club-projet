package com.location_video_club;
import java.util.*;
import java.util.stream.Collectors;

public class VideoClub {
    private final List<ProduitVideo> produits;
    private final List<Abonnee> abonnees;

    /**
     *  Constructeur pour creer un nouveau videoclub
     */
    public VideoClub() {
        this(null, null);
    }

    /**
     * Constructeur pour creer un videoclub existant
     *
     * @param produits la listes des produits video
     * @param abonnees la liste des abonnees
     */
    public VideoClub(List<ProduitVideo> produits, List<Abonnee> abonnees) {
        this.produits = Objects.requireNonNullElseGet(produits, ArrayList::new);
        this.abonnees = Objects.requireNonNullElseGet(abonnees, ArrayList::new);
    }

    /**
     * Ajoute un abonné à la liste des abonnés du VideoClub.
     *
     * @param abonnee L'abonné à ajouter.
     */
    public void ajouterAbonnee(Abonnee abonnee) {
        if (abonnee == null) {
            System.out.println("Abonnee vide");
            return; 
        }
        this.abonnees.add(abonnee);
    }

    /**
     * Recherche un abonné par son nom.
     *
     * @param nom Le nom de l'abonné à rechercher.
     * @return L'abonné correspondant au nom, ou null si non trouvé.
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
     * Ajoute un film à la liste des films du vidéoclub.
     *
     * @param film Le film à ajouter.
     */
     public void ajouterFilm(Film film) {
        if (film == null) {
            System.out.println("Film vide");
            return;
        }
        this.produits.add(film);
    }

    /**
     * Enregistre un prêt de film effectué par un abonné.
     * Ajoute le film à la liste des produits loués par l'abonné.
     * Si le film n'est pas trouvé dans la liste des films du vidéoclub, affiche un message d'erreur.
     *
     * @param abonnee L'abonné qui loue le film.
     * @param film Le film à louer.
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
     * Extrait les abonnés dont la fourchette de revenu correspond à celle du revenu spécifié.
     *
     * @param revenu Revenu pour déterminer la fourchette cible.
     * @return Liste des abonnés ayant une fourchette de revenu correspondante.
     */
    public List<Abonnee> extraireAbonneeParRevenu(double revenu) {
        List<Abonnee> abonneesCorrespondants  = new ArrayList<Abonnee>();
        Abonnee.Fourchette fourchetteCible = Abonnee.determinFourchette(revenu);

        for (Abonnee abonnee : abonnees) {
            if(abonnee.getFourchette().equals(fourchetteCible)){
                abonneesCorrespondants .add(abonnee);
            }
        }
        return abonneesCorrespondants ;
    }

    /**
     * Détermine le genre le plus populaire parmi les produits loués par les abonnés.
     *
     * @return Le genre le plus fréquemment loué par les abonnés.
     */
    public List<Genre> extraireGenrePopulaires() {
        Map<Genre, Integer> compteur = new HashMap<>();
    
        List<Genre> genrePopulaire = new ArrayList<>();

        for (Abonnee a : abonnees) {
            List<ProduitVideo> listProduitsVideo = a.getProduits();

            for (ProduitVideo produit : listProduitsVideo) {
                // Récupère le genre du produit vidéo et incrémente son compteur dans la map
                Genre genre = produit.getGenre();
                compteur.put(genre, compteur.getOrDefault(genre, 0) + 1);
            }
        }
        int maxNb = Collections.max(compteur.values());
   
        for (Map.Entry<Genre, Integer> entry : compteur.entrySet()) {
            if (entry.getValue() == maxNb) {
                maxNb = entry.getValue();
                genrePopulaire.add(entry.getKey());
            }
        }
        return genrePopulaire;
    }

    /**
     * Extrait les films similaires à un film donné par son titre.
     * Calcule la similarité entre chaque film de la liste et le film recherché,
     * puis retourne une liste des films les plus similaires.
     *
     * @param titre Le titre du film pour lequel on cherche des films similaires.
     * @return Liste de films similaires au film donné, basée sur un calcul de similarité.
     */
    public List<Film> extraireFilmSimilaire(String titre) {
        List<Film> film_plus_similaire=new ArrayList<Film>();
        List<Film> films;
        List<Film> film_catalogue = produits.stream()
            .filter(produit -> produit instanceof Film)
            .map(produit -> (Film) produit)
            .collect(Collectors.toList());
            
        Film monFilm= film_catalogue.stream()
            .filter(film -> film.getTitre().equalsIgnoreCase(titre))
            .findFirst() 
            .orElse(null);

            if (monFilm != null) {
                films = film_catalogue.stream()
                    .filter(film -> !film.equals(monFilm)) // Exclure monFilm
                    .collect(Collectors.toList());
                    film_plus_similaire.add(monFilm);

                    float[] tab= new float[films.size()];
                    for (Film f : films) {
                        tab[films.indexOf(f)]=monFilm.calculSimilarite(f);
                    }
                    Arrays.sort(tab);
                    for (int i = 0; i < Math.round(tab.length/2); i++) {
                        for (Film f : films) {
                            if (tab[i] == f.calculSimilarite(monFilm)) {
                                film_plus_similaire.add(f);
                            }
                        }
                    }
                    return film_plus_similaire;
            }
            else {
                throw new IllegalArgumentException("Film introuvable");
            }
    }

    /**
     * Extrait la liste des abonnés les plus curieux, c'est-à-dire ceux ayant la plus grande diversité
     * dans les films qu'ils louent, en comparant la similarité entre leurs films.
     * Un abonné est considéré comme curieux s'il loue des films très différents les uns des autres.
     *
     * @return La liste des abonnés avec le score de diversité minimum (les plus curieux).
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
                scoreDiversite = film_louer_abonnee.get(0).calculSimilarite(film_louer_abonnee.get(0))*1000;
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
     * Calcule un score de diversité de similarité entre une liste de films.
     * Le score est plus bas lorsque les films sont plus diversifiés (moins similaires).
     *
     * @param film_louer_abonnee Liste des films loués par un abonné.
     * @return Un score de diversité : plus le score est bas, plus les films sont diversifiés.
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
     * Extrait les abonnés proches d'un profil donné en fonction de leur similarité.
     * Trie les abonnés par similarité (du plus similaire au moins similaire).
     *
     * @param profilType L'abonné de référence avec lequel comparer les autres.
     * @return Une liste d'abonnés triés par similarité croissante avec le profil donné.
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
    /**
     * Extrait les films dont la similarité moyenne entre les abonnés est inférieure au seuil spécifié.
     *
     * @param seuil La similarité moyenne en dessous de laquelle un film est sélectionné.
     * @return Liste des films correspondant au critère de similarité.
     */
    public List<Film> extraireFilmsPublicType(float seuil) {
        Map<Film, List<Abonnee>> filmsAbonnesMap = new HashMap<>();
        List<Film> filmsPublicType = new ArrayList<>();

        for (Abonnee a : abonnees) {
            List<ProduitVideo> listProduitsVideo = a.getProduits();
            for (ProduitVideo produit : listProduitsVideo) {
                if (produit instanceof Film film) {
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
                Abonnee ab1 = listAbonnee.get(i);
                for (int j = i + 1; j < listAbonnee.size(); j++) {
                    Abonnee ab2 = listAbonnee.get(j);
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

    /**
     * Extrait les produits vidéo similaires à chaque produit dans la liste.
     *
     * Pour chaque produit, cela retourne une liste des films similaires.
     * @return Une carte associant chaque produit vidéo à une liste de films similaires.
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
     * Retourne les films similaires à un film donné, triés par similarité croissante.
     * @param film Le film de référence.
     * @param tousLesFilms Liste de tous les films à comparer.
     * @return Liste de films similaires.
     */
    private List<Film> trouverFilmsSimilaires(Film film, List<Film> tousLesFilms) {
        List<Film> film_plus_similaire=new ArrayList<>();
        List<Film> films ;
        films = tousLesFilms.stream()
            .filter(f -> !f.equals(film)) // Exclure le film lui-même
            .collect(Collectors.toList()); // Collecter tous les films similaires sans limite
                    float[] tab= new float[films.size()];
                    for (Film f : films) {
                        tab[films.indexOf(f)]=film.calculSimilarite(f);
                    }
                    Arrays.sort(tab);
                    for (int i = 0; i < Math.round(tab.length/2); i++) {
                        for (Film f : films) {
                            if (tab[i] == f.calculSimilarite(film)) {
                                film_plus_similaire.add(f);
                            }
                        }
                    }
                    return film_plus_similaire;
    }
    
    @Override
    public String toString() {
        return "VideoClub [produits=" + produits + ", abonnees=" + abonnees + "]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VideoClub videoClub)) return false;
        return Objects.equals(produits, videoClub.produits) && Objects.equals(abonnees, videoClub.abonnees);
    }

    @Override
    public int hashCode() {
        return Objects.hash(produits, abonnees);
    }
}
