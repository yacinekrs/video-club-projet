package com.location_video_club;

import java.util.Objects;

public class Abonnee {

    private final String nom;
    private final String prenom;
    private final int age;
    private final Sexe sexe;
    private final double revenu;
    private final Fourchette fourchette;
    public enum Sexe {Masculin, Feminin, Autre;}
    public enum Fourchette {Faible, Moyen, Elevee;}

    /**
     *
     * @param nom Le Nom de l'abonné
     * @param prenom Le prenom de l'abonné
     * @param age L'age de l'abonné
     * @param sexe Sexe de l'abonné (Masculin, Feminin, Autre)
     * @param revenu Le revenue de l'abonné
     * @param fourchette Fourchette de l'abonné (Faible, Moyen, Elevée)
     */
    public Abonnee(String nom, String prenom, int age, String sexe, double revenu, Fourchette fourchette) {
        if (age < 0 && revenu < 0) {
            throw new IllegalArgumentException("L'âge et le revenu doit être positif.");
        }

        this.nom = nom;
        this.prenom = prenom;
        this.age = age;
        this.sexe = determinerSexe(sexe);
        this.revenu = revenu;
        this.fourchette = determinFourchette(revenu);
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

    private Fourchette determinFourchette(double revenu){
        if (revenu >= 20000 && revenu <= 35000) {
            return Fourchette.Faible;
        } else if (revenu >= 35001 && revenu <= 55000) {
            return Fourchette.Moyen;
        } else {
            return Fourchette.Elevee;
        }
    }
    public float calculerSimilarite(Abonnee autreAbonnee) {
        float simAge = Math.abs(this.age - autreAbonnee.age) / 10 ;
        float simSexe = this.sexe.equals(autreAbonnee.sexe)? 0 : 1;
        float simTranche = this.fourchette.equals(autreAbonnee.fourchette)? 0 : 1;

        return simAge + simSexe + simTranche;

    }

}