package com.location_video_club;

import java.util.Objects;

public class Genre {
    private final String nom;
    private final Genre parent;
    /**
     *
     * @param nom nom le nom du genre (ex: "Aventure", "Comédie").
     * @param parent le nom du genre parent, ou null si ce genre n'a pas de parent.
     * @throws IllegalArgumentException si les conditions de création du genre ne sont pas respectées.
     */
    public Genre(final String nom, final Genre parent) {
        // Vérification que 'Action' et 'Comédie' ne doivent pas avoir de parent.
        if (("Action".equalsIgnoreCase(nom) || "Comédie".equalsIgnoreCase(nom)) && parent != null) {
            throw new IllegalArgumentException("Le genre " + nom + " ne doit pas avoir de parent.");
        }

        // Vérification des relations spécifiques entre le genre et son parent.
        if (("Aventure".equalsIgnoreCase(nom) || "Western".equalsIgnoreCase(nom)) &&
                (parent == null || !"Action".equalsIgnoreCase(parent.nom))) {
            throw new IllegalArgumentException("Le parent du genre " + nom + " doit être 'Action'.");
        }
        if (("Musique".equalsIgnoreCase(nom) || "Romance".equalsIgnoreCase(nom)) &&
                (parent == null || !"Comédie".equalsIgnoreCase(parent.nom))) {
            throw new IllegalArgumentException("Le parent du genre " + nom + " doit être 'Comédie'.");
        }

        // Vérification que le genre et le parent ne sont pas identiques.
        if (parent != null && nom.equalsIgnoreCase(parent.nom)) {
            throw new IllegalArgumentException("Le genre et le parent ne doivent pas être identiques.");
        }

        this.nom = nom;
        this.parent = parent;
    }

    /**
     * Retourne le nom du genre.
     *
     * @return une chaîne de caractères représentant le nom du genre.
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le genre parent de ce genre.
     *
     * @return l'objet Genre représentant le parent de ce genre, ou null si ce genre n'a pas de parent.
     */
    public Genre getParent() {
        return parent;
    }

    @Override
    public String toString() {
        return "Genre{" +
                "nom='" + nom + '\'' +
                ", parent='" + parent + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Genre genre)) return false;
        return Objects.equals(nom, genre.nom) && Objects.equals(parent, genre.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nom, parent);
    }
}
