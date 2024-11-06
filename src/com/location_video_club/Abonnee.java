package com.location_video_club;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;

public class Abonnee {
    private final String nom;
    private final String prenom;
    private final int age;
    private final Sexe sexe;
    private final double revenu;
    private final Fourchette fourchette;
    private final List<ProduitVideo> produits_louer;
    public enum Sexe {Masculin, Feminin, Autre;}
    public enum Fourchette {Faible, Moyen, Elevee;}
    

    /**
     *  Constructeur d'un abonné 
     * @param nom Le Nom de l'abonné
     * @param prenom Le prenom de l'abonné
     * @param age L'age de l'abonné
     * @param sexe Sexe de l'abonné (Masculin, Feminin, Autre)
     * @param revenu Le revenue de l'abonné
     */
    public Abonnee(String nom, String prenom, int age, String sexe, double revenu, List<ProduitVideo> produits) {
        if (age <= 0 && revenu < 0) {
            throw new IllegalArgumentException("L'âge et le revenu doit être positif.");
        }

        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.sexe = determinerSexe(sexe);
        this.revenu = revenu;
        this.fourchette = determinFourchette(revenu);

        if (produits ==  null){
            this.produits_louer = new ArrayList<ProduitVideo>();
        } else {
            this.produits_louer = produits;
        }
    }

    /**
     * Constructeur d'un abonné sans liste de produit
     * @param nom nom de l'abonné
     * @param prenom prenom de l'abonné
     * @param age age de l'abonné
     * @param sexe sexe de l'abonné
     * @param revenu revenu de l'abonné
     */
    public Abonnee(String nom, String prenom, int age, String sexe, double revenu) {
        this(nom, prenom, age, sexe, revenu, null);
    }

    /**
     *
     * @return une chaîne de caractères représentant le nom de l'abonné.
     */
    public String getNom() {
        return nom;
    }

    /**
     *
     * @return une chaîne de caractères représentant le prénom de l'abonné.
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     *
     * @return un entier représentant l'âge de l'abonné.
     */
    public int getAge() {
        return age;
    }

    /**
     *
     * @return une valeur de l'énumération Sexe représentant le sexe de l'abonné (MASCULIN, FEMININ ou AUTRE).
     */
    public Sexe getSexe() {
        return sexe;
    }

    /**
     *
     * @return un float représentant le revenue de l'abonné
     */
    public double getRevenue() {
        return revenu;
    }

    /**
     *
     * @return une valeur de l'énumération Fourchette représentant la classification de l'abonné.
     */
    public Fourchette getFourchette() {
        return fourchette;
    }

    /**
     * Retourne la liste des produits loué par l'abonné
     * @return 
     */
    public List<ProduitVideo> getProduits() {
        return produits_louer;
    }

    /**
     * Méthode pour déterminer le sexe à partir d'une chaîne de caractères.
     *
     * @param sexeStr Le sexe en format texte ("M", "H" "Homme", "Femme", etc.).
     * @return une valeur de l'énumération Sexe correspondant au texte fourni.
     */
    private Sexe determinerSexe(String sexeStr) {
        if (sexeStr == null) {
            return Sexe.Autre;
        }

        String sexeNormalized = sexeStr.trim().toLowerCase();
        switch (sexeNormalized) {
            case "h":
            case "m":
            case "homme":
            case "masculin":
                return Sexe.Masculin;
            case "f":
            case "femme":
            case "féminin":
            case "feminin":
                return Sexe.Feminin;
            default:
                return Sexe.Autre;
        }
    }

    /**
     * Détermine la fourchette de revenu en fonction d'un montant donné.
     *
     * @param revenu Le montant du revenu à analyser.
     * @return La fourchette de revenu : Faible, Moyen ou Élevée.
     *         - Faible : revenu entre 20,000 et 35,000 inclus.
     *         - Moyen : revenu entre 35,001 et 55,000 inclus.
     *         - Élevée : revenu supérieur à 55,000.
     */
    public static Fourchette determinFourchette(double revenu){
        if (revenu >= 20000 && revenu <= 35000) {
            return Fourchette.Faible;
        } else if (revenu >= 35001 && revenu <= 55000) {
            return Fourchette.Moyen;
        } else {
            return Fourchette.Elevee;
        }
    }

    /**
     * Ajoute un produit à la liste des produits loués par cet abonné.
     * @param produit Le produit vidéo à ajouter à la liste des locations de l'abonné.
     */
    public  void louerProduit(ProduitVideo produit){
        this.produits_louer.add(produit);
    }

    /**
     * Retire un produit de la liste des produits loués par cet abonné.
     * @param produit Le produit vidéo à retirer de la liste des locations de l'abonné.
     */
    public  void rendreProduit(ProduitVideo produit){
        this.produits_louer.remove(produit);
    }

    /**
     * Calcule la similarité entre cet abonné et un autre abonné selon
     * l'âge, le sexe et la tranche de revenu.
     *
     * @param autreAbonnee L'abonné à comparer.
     * @return Score de similarité, plus faible pour plus de ressemblance.
     */
    public float calculerSimilarite(Abonnee autreAbonnee) {
        float simAge = (float) Math.abs(this.age - autreAbonnee.age) / 10 ;
        float simSexe = this.sexe.equals(autreAbonnee.sexe)? 0 : 1;

        float simTranche = 0;
        if ((this.fourchette == Fourchette.Faible && autreAbonnee.fourchette == Fourchette.Elevee) ||
                (this.fourchette == Fourchette.Elevee && autreAbonnee.fourchette == Fourchette.Faible)) {
            simTranche = 1;
        }
        return simAge + simSexe + simTranche;
    }

    @Override
    public String toString() {
        return "Abonnee{" +
                "nom='" + nom + '\'' +
                ", prenom='" + prenom + '\'' +
                ", age=" + age +
                ", sexe=" + sexe +
                ", fourchette=" + fourchette +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Abonnee abonnee)) return false;
        return age == abonnee.age && Objects.equals(nom, abonnee.nom) && Objects.equals(prenom, abonnee.prenom) && sexe == abonnee.sexe && fourchette == abonnee.fourchette;
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, prenom, age, sexe, fourchette);
    }
}
