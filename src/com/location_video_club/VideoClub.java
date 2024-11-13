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
    static MongoClient mongoClient ;
    static MongoDatabase database;
    static MongoCollection<Document> collection_coffret;
    static MongoCollection<Document> collection_acteur;
    static MongoCollection<Document> collection_film;
    static MongoCollection<Document> collection_genre;
    static MongoCollection<Document> collection_abonnee;

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
    public Genre extraireGenrePopulaires() {
        Map<Genre, Integer> compteur = new HashMap<>();
        int maxNb = 0;
        Genre genrePopulaire = null;

        for (Abonnee a : abonnees) {
            List<ProduitVideo> listProduitsVideo = a.getProduits();

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
    
    private static  void connectionMongodb(){
           mongoClient = MongoClients.create("mongodb+srv://madiassalakamyd:uKp4gTCeI3EfcICl@cluster0.neped.mongodb.net/");
           database = mongoClient.getDatabase("video_club_project");
            collection_coffret= database.getCollection("Coffrets");
            collection_acteur= database.getCollection("Acteurs");
            collection_film= database.getCollection("Films");
            collection_genre= database.getCollection("Genres");
           collection_abonnee= database.getCollection("Abonnees");
           System.out.println("Connexion à MongoDB réussie");
           
    }
    private static void endConnection(){ mongoClient.close();}

    /*************la fonction de sauvegarde  */
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
    public void AddinDocuments(Document isDocument,Document document, List<Document> collection){
        if(isDocument != null){ 
            System.out.println("Cet abonnee existe deja!!!");
        }else{
            document.append("_id", new ObjectId());
            collection.add(document);
        }
    }
    public void SauvegardeAll(){
        sauvegardeGenre();
        sauvegardeActeur();
        sauvegardeFilm();
        sauvegardeCoffret();
        sauvegardeAbonnee();
    }
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
                } 
                if(produit instanceof Coffret){
                    Document coffret_g = new Document("titre",((Coffret)produit).getTitre())
                                        .append("bonus",((Coffret)produit).isAbonus());
                    Document genre_film = new Document("nom",((Coffret)produit).getGenre().getNom());
                    ObjectId idgenre = getIdGenre(genre_film);
                    coffret_g.append("genre",idgenre);
                    Document docCoffret = collection_coffret.find(coffret_g).first();
                    if(docCoffret!=null){
                        liste_id_produit.add(docCoffret.getObjectId("_id"));
                    } else {
                        throw new NullPointerException("pas de coffret trouver");
                    }
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
/******************fin de la foncion de sauvegarde */
/******debut fonction de chargement ******/
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
public static List<Acteur> chargementActeurs(){
       List<Acteur> acteurs = new ArrayList<>();
       for (Document doc : collection_acteur.find()) {
           String nom = doc.getString("nom");
           String prenom = doc.getString("prenom");
           acteurs.add(new Acteur(nom, prenom));
       }
       return acteurs;
}
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

public static List<Abonnee> chargementAbonnees(){
    List<Genre> genres = chargementGenres(); 
    List<Acteur> acteurs_all = chargementActeurs(); 
    List<Film> film_all = chargementFilms(); 
    List<Abonnee> liste_abonnees = new ArrayList<>();
    for (Document doc : collection_abonnee.find()) {
        String nomAbonnee = doc.getString("nom");
        String prenomAbonnee = doc.getString("prenom");
        int ageAbonnee = doc.getInteger("age");
        String sexeAbonneeStr = doc.getString("sexe");
        double revenuAbonnee = doc.getDouble("revenu");
        Abonnee abonnee = new Abonnee(nomAbonnee, prenomAbonnee, ageAbonnee, sexeAbonneeStr, revenuAbonnee);
        List<ProduitVideo>  produitsLoues= new ArrayList<>();
        List<ObjectId> produitIds = doc.getList("produit_louer", ObjectId.class); 
        for (ObjectId produitId : produitIds) {
            Document produitDoc = collection_film.find(new Document("_id", produitId)).first();
            if (produitDoc != null) {
               sousChargementAbonnee(produitDoc, produitsLoues, genres, acteurs_all,null);
            }else{
                System.out.println("le produit pour le chargement n'est pas dans film !!!");
            }
            Document produitDocc = collection_coffret.find(new Document("_id", produitId)).first();
            if(produitDocc!=null){
                Coffret coffret=new Coffret(null, null, acteurs_all, false, null);
                sousChargementAbonnee(produitDocc, produitsLoues, genres, acteurs_all, coffret);

                List<Film> film_coffret=new ArrayList<>();  // les films du coffrets
                List<ObjectId> id_films = produitDocc.getList("films", ObjectId.class);
                for(ObjectId idfilm:id_films){
                  Document  films_doc=collection_film.find(new Document("_id", idfilm)).first();
                  if(films_doc!=null){
                      for(Film film_one:film_all){
                        if(film_one.getTitre().equalsIgnoreCase(films_doc.getString("titre"))
                        && film_one.getCouleur()==films_doc.getBoolean("couleur")){
                            film_coffret.add(film_one);
                        }
                      }
                  }else{
                      System.out.println("Ces films n'existe pas dans la base lors du chargement !!!");
                      return null;
                  }
                }  
             produitsLoues.add(new Coffret(produitDocc.getString("titre"),coffret.getGenre(),coffret.getActeurs(),produitDocc.getBoolean("bonus"),film_coffret));
            }else{
                System.out.println("le produit pour le chargement n'est pas dans coffret !!!");
            }
        }
        for(ProduitVideo prod:produitsLoues){
            abonnee.louerProduit(prod);
        }
        liste_abonnees.add(abonnee);
    }
    return liste_abonnees;
}

public static void sousChargementAbonnee(Document produitDoc, List<ProduitVideo>  produitsLoues,List<Genre> genres,List<Acteur> acteurs_all,Coffret coffret){
    ObjectId id_genre = produitDoc.getObjectId("genre");
    Document  genre_doc = collection_genre.find(new Document("_id", id_genre)).first();
    Genre genre_film = null; // genre du film
    if(genre_doc != null){
      for (Genre g : genres){
          if(g.getNom().equalsIgnoreCase(genre_doc.getString("nom"))){
              genre_film=g;
              break;
          }
      }
    }else{
      System.out.println("ce film na pas de genre pour le chargement!!!");
      return; // a revenir dessus 
    }
    List<Acteur> acteur_film=new ArrayList<>();  // les acteurs du film
    List<ObjectId> id_acteurs = produitDoc.getList("acteurs", ObjectId.class);
    for(ObjectId idacteur:id_acteurs){
      Document  acteurs_doc=collection_acteur.find(new Document("_id", idacteur)).first();
      if(acteurs_doc!=null){
          for(Acteur acteur_one:acteurs_all){
              if(acteur_one.getNom().equalsIgnoreCase(acteurs_doc.getString("nom"))
              && acteur_one.getPrenom().equalsIgnoreCase(acteurs_doc.getString("prenom"))){
                  acteur_film.add(acteur_one);
              }
          }
      }else{
          System.out.println("Ces acteurs n'existe pas dans la base lors du chargement !!!");
          return;
      }
    }
    if(coffret==null)
    produitsLoues.add(new Film(produitDoc.getString("titre"),genre_film,acteur_film,produitDoc.getBoolean("couleur")));
    else{
        coffret=new Coffret(produitDoc.getString("titre"), genre_film, acteurs_all, false, null);
    }
}


/*******fin de la fonction de chargement *******/
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
