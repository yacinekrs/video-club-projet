package com.location_video_club;
import java.util.*;
import java.util.stream.Collectors;

import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import org.bson.Document;
import org.bson.types.ObjectId;
public class VideoClub {
    private final List<ProduitVideo> produits;
    private final List<Abonnee> abonnees;
    private static MongoClient mongoClient ;
    private static MongoDatabase database;
    private static MongoCollection<Document> collection_coffret;
    private static MongoCollection<Document> collection_acteur;
    private static MongoCollection<Document> collection_film;
    private static MongoCollection<Document> collection_genre;
    private static MongoCollection<Document> collection_abonnee;
    private static final String MONGO_URI = "mongodb+srv://madiassalakamyd:uKp4gTCeI3EfcICl@cluster0.neped.mongodb.net/";
    private static final String DB_NAME = "video_club_project";

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
     * Retourne la liste des abonnées du VideoClub.
     * 
     * @return La liste des abonnées.
     */
    public List<Abonnee> getAbonnees() {
        return abonnees;
    }
    /**
     * Retourne la liste des produits du VideoClub.
     * 
     * @return La liste des produits.
     */
    public List<ProduitVideo> getProduits() {
        return produits;
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

    public void ajouterCoffret(Coffret coffret) {
        if (coffret == null) {
            System.out.println("Film vide");
            return;
        }
        this.produits.add(coffret);
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
    /**
     * Connexion à la base de données MongoDB et création des collections. 
     */
    private static  void connectionMongodb(){
           mongoClient = MongoClients.create(MONGO_URI);
           database = mongoClient.getDatabase(DB_NAME);
            collection_coffret= database.getCollection("Coffrets");
            collection_acteur= database.getCollection("Acteurs");
            collection_film= database.getCollection("Films");
            collection_genre= database.getCollection("Genres");
           collection_abonnee= database.getCollection("Abonnees");
           System.out.println("Connexion à MongoDB réussie");
           
    }
    /**
     * Fermeture de la connexion à la base de données MongoDB.
     */
    private static void endConnection(){ mongoClient.close();}
   /**
    * Retourne la liste des ID des acteurs d'un produit video donné.
    * @param produitVideo
    * @return
    */
    public List<ObjectId> getIdActeurs(ProduitVideo produitVideo){
        List<ObjectId> liste_id_acteurs=new ArrayList<>();
        for(Acteur acteur: produitVideo.getActeurs()){
            Document acteur_film = new Document("nom",acteur.getNom())
                                       .append("prenom", acteur.getPrenom());
            Document docacteur=collection_acteur.find(acteur_film).first();
            if(docacteur!=null){
                liste_id_acteurs.add(docacteur.getObjectId("_id"));
            } else {
                System.out.println("pas d'acteur trouver");
                return null;
            }
        }
        return liste_id_acteurs;
    }
    /**
     * Retourne l'ID du genre d'un film donné.
     * @param genre_film
     * @return
     */
    public ObjectId getIdGenre(Document genre_film){
        Document docgenre = collection_genre.find(genre_film).first();
        ObjectId idgenre;
        if(docgenre!=null){
            idgenre= docgenre.getObjectId("_id");
        } else {
            throw new NullPointerException("pas de genre trouver");
        }
        return idgenre;
    }
  /**
   * Ajoute un document à une collection si il n'existe pas deja.
   * @param isDocument
   * @param document
   * @param collection
   */
    public void AddinDocuments(Document isDocument,Document document, List<Document> collection){
        if(isDocument != null){ 
            System.out.println("Cet abonnee existe deja!!!");
        }else{
            document.append("_id", new ObjectId());
            collection.add(document);
        }
    }
    /**
     * Sauvegarde toutes les données dans la base de données MongoDB.
     */
    public void SauvegardeAll(){
        sauvegardeGenre();
        sauvegardeActeur();
        sauvegardeFilm();
        sauvegardeCoffret();
        sauvegardeAbonnee();
    }
    /**
     * Sauvegarde des genres dans la base de données MongoDB.
     */
    public void sauvegardeGenre(){
        connectionMongodb();
        Set<Genre> nos_genres = new HashSet<>();
        for( ProduitVideo produit : produits){
            nos_genres.add(produit.getGenre());
        }

        List<Document> documents = new ArrayList<>();
        for (Genre genre : nos_genres) {
            Document genre_g = new Document("nom",genre.getNom());
            if (genre.getParent() != null) {
            genre_g.append("parent", genre.getParent().getNom());
            }else{
                genre_g.append("parent", "Genre");
            }
            Document docgenre = collection_genre.find(genre_g).first();
            AddinDocuments(docgenre, genre_g, documents);
        }
        if(documents.isEmpty()) {
            System.out.println("Ces genre ont deja ete sauvegarder en amont!!! /n ou bien il ya pas de genre");
        }
        else{
            collection_genre.insertMany(documents);
        }
        
        endConnection();
    }
   /**
    * Sauvegarde des acteurs dans la base de données MongoDB.
    */
    public void sauvegardeActeur(){
        connectionMongodb();
        Set<Acteur> nos_acteurs = new HashSet<>();
        for( ProduitVideo produit : produits){
            for(Acteur acteur : produit.getActeurs()){
                nos_acteurs.add(acteur);
            }
        }

        List<Document> documents = new ArrayList<>();
        for (Acteur acteur : nos_acteurs) {
            Document acteur_g = new Document("nom",acteur.getNom()).append("prenom", acteur.getPrenom());
            Document docActeur = collection_acteur.find(acteur_g).first();
            AddinDocuments(docActeur, acteur_g, documents);
        }
        if(documents.isEmpty()){
            System.out.println("Ces acteurs existe deja ils ont ete sauvegarder en amont !!! /n ou bien ya pas d'acteur");
        }else{
            collection_acteur.insertMany(documents);
        }
        endConnection();
    }
   /**
    * Sauvegarde des films dans la base de données MongoDB.
    */
    public void sauvegardeFilm(){
        connectionMongodb();
        List<Film> films = produits.stream()
            .filter(produit -> produit instanceof Film)
            .map(produit -> (Film) produit)
            .collect(Collectors.toList());

        List<Document> documents = new ArrayList<>();
        for (Film film : films) {
            List<ObjectId> liste_id_acteurs = getIdActeurs(film);
            Document genre_film = new Document("nom",film.getGenre().getNom());
            ObjectId idgenre = getIdGenre(genre_film);
            Document film_g = new Document("titre",film.getTitre())
                            .append("genre",idgenre )
                            .append("acteurs",liste_id_acteurs)
                            .append("couleur",film.getCouleur());
            Document docfilm_g = collection_film.find(film_g).first();
            AddinDocuments(docfilm_g, film_g, documents);
        }
        if(documents.isEmpty()){
            System.out.println("ces films existe deja dans la base il sont ete sauvegarder en amont!!!/n ou bien ya pas de films");
        }
        else{
            collection_film.insertMany(documents);
        }
      
        endConnection();
    }
   /**
    * Sauvegarde des abonnees dans la base de données MongoDB.
    */
    public void sauvegardeAbonnee(){
        connectionMongodb();
        List<Document> documents = new ArrayList<>();
        for (Abonnee abonnee : abonnees) {
            List<ObjectId> liste_id_produit = new ArrayList<>();
            for(ProduitVideo produit : abonnee.getProduits()){
              if(produit instanceof Film){
                    Document film_g = new Document("titre",((Film)produit).getTitre())
                                    .append("couleur",((Film)produit).getCouleur());
                    Document genre_film = new Document("nom",((Film)produit).getGenre().getNom());
                    ObjectId idgenre = getIdGenre(genre_film);
                    film_g.append("genre",idgenre);
                    Document docfilm = collection_film.find(film_g).first();
                    if(docfilm!=null){
                        liste_id_produit.add(docfilm.getObjectId("_id"));
                    } else {
                        throw new NullPointerException("pas de film trouver");
                    }
                } else if(produit instanceof Coffret){
                    Document coffret_g = new Document("titre",((Coffret)produit).getTitre())
                                        .append("bonus",((Coffret)produit).isAbonus());
                    Document genre_coffret = new Document("nom",((Coffret)produit).getGenre().getNom());
                    ObjectId idgenre = getIdGenre(genre_coffret);
                    coffret_g.append("genre",idgenre);
                    Document docCoffret = collection_coffret.find(coffret_g).first();
                    if(docCoffret!=null){
                        liste_id_produit.add(docCoffret.getObjectId("_id"));
                    } else {
                        throw new NullPointerException("pas de coffret trouver");
                    }
                }else{
                    throw new NullPointerException("pas de produit trouver");
                }
            }
            Document abonnee_g = new Document("nom",abonnee.getNom())
                            .append("prenom", abonnee.getPrenom())
                            .append("age",abonnee.getAge())
                            .append("sexe", abonnee.getSexe())
                            .append("revenu", abonnee.getRevenue())
                            .append("fourchette",abonnee.getFourchette())
                            .append("produit_louer",liste_id_produit);
                            
            Document docabonnee_g = collection_abonnee.find(abonnee_g).first();
            AddinDocuments(docabonnee_g, abonnee_g, documents);
        }
        
        if(documents.isEmpty()){
            System.out.println("Ces Abonnees existe deja dans la base ils ont ete Enregistrer en amont/n ou birn ya pas de coffret");
        }else{
            collection_abonnee.insertMany(documents);
        }
        endConnection();
    }
   /**
    * Sauvegarde des coffrets dans la base de données MongoDB.
    */
    public void sauvegardeCoffret(){
        connectionMongodb();
        List<Coffret> coffrets = produits.stream()
                .filter(produit -> produit instanceof Coffret)
                .map(produit -> (Coffret) produit)
                .collect(Collectors.toList());
        List<Document> documents = new ArrayList<>();
        for (Coffret coffret : coffrets) {
            List<ObjectId> liste_id_acteurs= getIdActeurs(coffret);
            List<ObjectId> liste_id_films = new ArrayList<>();

            for(Film film : coffret.getFilms()){
                Document doc_film = new Document("titre",film.getTitre()).append("couleur",film.getCouleur());
                Document genre_film = new Document("nom",film.getGenre().getNom());
                ObjectId idgenre = getIdGenre(genre_film);
                doc_film.append("genre",idgenre);
                Document isfilm = collection_film.find(doc_film).first();
                if(isfilm != null){
                    liste_id_films.add(isfilm.getObjectId("_id"));
                } else {
                    throw new NullPointerException("pas de film trouver");
                }
            }
            Document genre_coffret = new Document("nom",coffret.getGenre().getNom());
            ObjectId idgenre = getIdGenre(genre_coffret);
            Document coffret_g = new Document("titre",coffret.getTitre())
                                        .append("genre",idgenre )
                                        .append("acteurs",liste_id_acteurs)
                                        .append("films", liste_id_films)
                                        .append("bonus",coffret.isAbonus());
             Document docCoffret_g=collection_coffret.find(coffret_g).first();
             if(docCoffret_g!=null){ 
                System.out.println("ce coffret  existe deja!!!");
                }
                else{
                    coffret_g.append("_id", new ObjectId());
                     documents.add(coffret_g);
                }
        }
        if(documents.isEmpty()){
            System.out.println("Ces cofrets existent deja dans la base ils ont ete enregistre en amont");
        }else{
            collection_coffret.insertMany(documents);
        }
        
        endConnection();
    }
    /**
     * Chargement de la base de données MongoDB.
     * @return VideoClub
     */
    public static VideoClub chargementAll(){
        connectionMongodb();
        List<Abonnee> liste_abonnnee=chargementAbonnees();
        List<Film> liste_Film=chargementFilms();
        List<Coffret> liste_coffret=chargementCoffrets();
        List<ProduitVideo> liste_ProduitVideos=new ArrayList<>();
        for(Film film:liste_Film){
            liste_ProduitVideos.add(film);
        }
        for(Coffret cofret:liste_coffret){
            liste_ProduitVideos.add(cofret);
        }
        endConnection();
    return new VideoClub(liste_ProduitVideos,liste_abonnnee);
    }
    /**
     * Chargement des genres de la base de données MongoDB.
     * @return une liste de genres
     */
    public static List<Genre> chargementGenres(){
    
        List<Genre> genres = new ArrayList<>();
        for (Document doc : collection_genre.find()) {
            String nom = doc.getString("nom");
            String parentNom = doc.getString("parent");
            Genre parent = parentNom != null ? new Genre(parentNom, null) : null;
            genres.add(new Genre(nom, parent));
        }
        
        return genres;
    }
    /**
     * Chargement des acteurs de la base de données MongoDB.
     * @return une liste d'acteurs
     */
    public static List<Acteur> chargementActeurs(){
        List<Acteur> acteurs = new ArrayList<>();
        for (Document doc : collection_acteur.find()) {
            String nom = doc.getString("nom");
            String prenom = doc.getString("prenom");
            acteurs.add(new Acteur(nom, prenom));
        }
        return acteurs;
    }
    /**
     * Chargement des films de la base de données MongoDB.
     * @return une liste de films
     */
    public static List<Film> chargementFilms(){
        List<Genre> genres = chargementGenres();
        List<Film> films = new ArrayList<>();
        for (Document doc : collection_film.find()) {
            String titre = doc.getString("titre");
            Genre genreFilm = sousChargementGenreProduit(doc, genres);
            if(genreFilm == null){
                throw new NullPointerException("L'objet genre de film est null");
            }
            List<Acteur> acteursDuFilm = new ArrayList<>();
            List<ObjectId> acteurIds = doc.getList("acteurs", ObjectId.class);
            for (ObjectId acteurId : acteurIds) {
                Document acteurDoc = collection_acteur.find(new Document("_id", acteurId)).first();
                if (acteurDoc != null) {
                    String nomActeur = acteurDoc.getString("nom");
                    String prenomActeur = acteurDoc.getString("prenom");
                    acteursDuFilm.add(new Acteur(nomActeur, prenomActeur));
                }
            }

            boolean couleur = doc.getBoolean("couleur");
            films.add(new Film(titre, genreFilm, acteursDuFilm, couleur));
        }
        return films;
    }
    /**
     * Chargement du genre d'un produit de la base de données MongoDB.
     * @param produit
     * @param genres
     * @return
     */
    public static Genre sousChargementGenreProduit(Document produit, List<Genre> genres){
        ObjectId genre_produit_id = produit.getObjectId("genre");
        Document d_genre = collection_genre.find(new Document("_id",genre_produit_id)).first();
        for(Genre genre : genres){
            if(genre.getNom().equalsIgnoreCase(d_genre.getString("nom"))){
                return genre;
            }
        }
        return null;
    }
    /**
     * Chargement des coffrets de la base de données MongoDB.
     * @return une liste de coffrets
     */
    public static List<Coffret> chargementCoffrets(){
    List<Coffret> coffrets = new ArrayList<>();
    List<Genre> genres=chargementGenres();
    for (Document doc : collection_coffret.find()) {
        String titre = doc.getString("titre");
        Genre genreCoffret = sousChargementGenreProduit(doc, genres);
        if(genreCoffret==null){
            throw new NullPointerException("L'objet genre de coffret est null");
        }
            List<Acteur> acteursDucoffret = new ArrayList<>();
            List<ObjectId> acteurIds_coffret = doc.getList("acteurs", ObjectId.class);
            for (ObjectId acteurId : acteurIds_coffret) {
                Document acteurDocCoffret = collection_acteur.find(new Document("_id", acteurId)).first();
                if (acteurDocCoffret != null) {
                    String nomActeur = acteurDocCoffret.getString("nom");
                    String prenomActeur = acteurDocCoffret.getString("prenom");
                    acteursDucoffret.add(new Acteur(nomActeur, prenomActeur));
                }
            }
        boolean abonus = doc.getBoolean("bonus");

        // Récupération des films associés au coffret
        List<Film> filmsDuCoffret = new ArrayList<>();
        List<ObjectId> filmIds = doc.getList("films", ObjectId.class); // Utilisation de getList pour récupérer les ObjectId
        for (ObjectId filmId : filmIds) {
            Document filmDoc = collection_film.find(new Document("_id", filmId)).first();
            if (filmDoc != null) {
                String titreFilm = filmDoc.getString("titre");
                Genre genreFilm = sousChargementGenreProduit(filmDoc, genres);
                if(genreFilm==null){
                    throw new NullPointerException("un genre des film du  coffret est null");
                }
                
                List<Acteur> acteursDuFilm = new ArrayList<>();
                List<ObjectId> acteurIds = filmDoc.getList("acteurs", ObjectId.class); // Récupérer les acteurs
                for (ObjectId acteurId : acteurIds) {
                    Document acteurDoc = collection_acteur.find(new Document("_id", acteurId)).first();
                    if (acteurDoc != null) {
                        String nomActeur = acteurDoc.getString("nom");
                        String prenomActeur = acteurDoc.getString("prenom");
                        acteursDuFilm.add(new Acteur(nomActeur, prenomActeur));
                    }
                }

                boolean couleurFilm = filmDoc.getBoolean("couleur");
                filmsDuCoffret.add(new Film(titreFilm, genreFilm, acteursDuFilm, couleurFilm));
            }

        }
        // Créer le coffret avec les films récupérés
        Coffret coffret = new Coffret(titre, genreCoffret, acteursDucoffret, abonus, filmsDuCoffret);
        coffrets.add(coffret);
    }
    return coffrets;

    }

    /**
     * Charge la liste des abonnés à partir de la base de données.
     * 
     * @return Une liste d'objets Abonnee contenant tous les abonnés chargés
     */
    public static List<Abonnee> chargementAbonnees() {
        List<Genre> genres = chargementGenres();
        List<Acteur> acteurs_all = chargementActeurs();
        List<Film> film_all = chargementFilms();
        List<Abonnee> liste_abonnees = new ArrayList<>();

        for (Document doc : collection_abonnee.find()) {
            Abonnee abonnee = creerAbonnee(doc);
            List<ProduitVideo> produitsLoues = chargerProduitsLoues(doc, genres, acteurs_all, film_all);
            
            for (ProduitVideo prod : produitsLoues) {
                abonnee.louerProduit(prod);
            }
            liste_abonnees.add(abonnee);
        }
        return liste_abonnees;
    }

    /**
     * Crée un objet Abonnee à partir d'un document Abonnee de la base de données MongoDB .
     *
     * @param doc Le document MongoDB contenant les informations de l'abonné
     * @return Un nouvel objet Abonnee
     */
    private static Abonnee creerAbonnee(Document doc) {
        return new Abonnee(
            doc.getString("nom"),
            doc.getString("prenom"),
            doc.getInteger("age"),
            doc.getString("sexe"),
            doc.getDouble("revenu")
        );
    }

    /**
     * Charge les produits loués par un abonné.
     *
     * @param doc Le document MongoDB de l'abonné
     * @param genres La liste de tous les genres
     * @param acteurs_all La liste de tous les acteurs
     * @param film_all La liste de tous les films
     * @return Une liste de ProduitVideo loués par l'abonné
     */
    private static List<ProduitVideo> chargerProduitsLoues(Document doc, List<Genre> genres, List<Acteur> acteurs_all, List<Film> film_all) {
        List<ProduitVideo> produitsLoues = new ArrayList<>();
        List<ObjectId> produitIds = doc.getList("produit_louer", ObjectId.class);

        for (ObjectId produitId : produitIds) {
            ProduitVideo produit = chargerProduit(produitId, genres, acteurs_all, film_all);
            if (produit != null) {
                produitsLoues.add(produit);
            }
        }
        return produitsLoues;
    }

    /**
     * Charge un produit vidéo (film ou coffret) à partir de son ID.
     *
     * @param produitId L'ID du produit à charger
     * @param genres La liste de tous les genres
     * @param acteurs_all La liste de tous les acteurs
     * @param film_all La liste de tous les films
     * @return Un objet ProduitVideo (Film ou Coffret) ou null si non trouvé
     */
    private static ProduitVideo chargerProduit(ObjectId produitId, List<Genre> genres, List<Acteur> acteurs_all, List<Film> film_all) {
        Document produitDoc = collection_film.find(new Document("_id", produitId)).first();
        if (produitDoc != null) {
            return chargerFilm(produitDoc, genres, acteurs_all);
        }

        produitDoc = collection_coffret.find(new Document("_id", produitId)).first();
        if (produitDoc != null) {
            return chargerCoffret(produitDoc, genres, acteurs_all, film_all);
        }

        System.out.println("Le produit pour le chargement n'est ni dans film ni dans coffret !!! " + produitId);
        return null;
    }

    /**
     * Charge un film à partir d'un document MongoDB.
     *
     * @param produitDoc Le document MongoDB du film
     * @param genres La liste de tous les genres
     * @param acteurs_all La liste de tous les acteurs
     * @return Un objet Film
     */
    private static Film chargerFilm(Document produitDoc, List<Genre> genres, List<Acteur> acteurs_all) {
        Genre genre = trouverGenre(produitDoc.getObjectId("genre"), genres);
        List<Acteur> acteurs = trouverActeurs(produitDoc.getList("acteurs", ObjectId.class), acteurs_all);
        
        return new Film(produitDoc.getString("titre"), genre, acteurs, produitDoc.getBoolean("couleur"));
    }

    /**
     * Charge un coffret à partir d'un document MongoDB.
     *
     * @param produitDoc Le document MongoDB du coffret
     * @param genres La liste de tous les genres
     * @param acteurs_all La liste de tous les acteurs
     * @param film_all La liste de tous les films
     * @return Un objet Coffret
     */
    private static Coffret chargerCoffret(Document produitDoc, List<Genre> genres, List<Acteur> acteurs_all, List<Film> film_all) {
        Genre genre = trouverGenre(produitDoc.getObjectId("genre"), genres);
        List<Film> films_coffret = trouverFilms(produitDoc.getList("films", ObjectId.class), film_all);
        
        return new Coffret(produitDoc.getString("titre"), genre, acteurs_all, produitDoc.getBoolean("bonus"), films_coffret);
    }

    /**
     * Trouve un genre à partir de son ID.
     *
     * @param genreId L'ID du genre à trouver
     * @param genres La liste de tous les genres
     * @return Le Genre correspondant ou null si non trouvé
     */
    private static Genre trouverGenre(ObjectId genreId, List<Genre> genres) {
        Document genre_doc = collection_genre.find(new Document("_id", genreId)).first();
        if (genre_doc == null) {
            System.out.println("Ce film n'a pas de genre pour le chargement !!!");
            return null;
        }
        return genres.stream()
                    .filter(g -> g.getNom().equalsIgnoreCase(genre_doc.getString("nom")))
                    .findFirst()
                    .orElse(null);
    }

    /**
     * Trouve les acteurs correspondant à une liste d'IDs.
     *
     * @param acteurIds La liste des IDs des acteurs à trouver
     * @param acteurs_all La liste de tous les acteurs
     * @return Une liste d'Acteur correspondant aux IDs fournis
     */
    private static List<Acteur> trouverActeurs(List<ObjectId> acteurIds, List<Acteur> acteurs_all) {
        return acteurIds.stream()
                        .map(id -> collection_acteur.find(new Document("_id", id)).first())
                        .filter(Objects::nonNull)
                        .flatMap(acteur_doc -> acteurs_all.stream()
                            .filter(a -> a.getNom().equalsIgnoreCase(acteur_doc.getString("nom"))
                                    && a.getPrenom().equalsIgnoreCase(acteur_doc.getString("prenom"))))
                        .collect(Collectors.toList());
    }

    /**
     * Trouve les films correspondant à une liste d'IDs.
     *
     * @param filmIds La liste des IDs des films à trouver
     * @param film_all La liste de tous les films
     * @return Une liste de Film correspondant aux IDs fournis
     */
    private static List<Film> trouverFilms(List<ObjectId> filmIds, List<Film> film_all) {
    return filmIds.stream()
                  .map(id -> collection_film.find(new Document("_id", id)).first())
                  .filter(Objects::nonNull)
                  .flatMap(film_doc -> film_all.stream()
                      .filter(f -> f.getTitre().equalsIgnoreCase(film_doc.getString("titre"))
                                && f.getCouleur() == film_doc.getBoolean("couleur")))
                  .collect(Collectors.toList());
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
