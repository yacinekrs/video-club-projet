package com.location_video_club;

import java.util.Objects;

public class Genre {
    private static final Genre GENRE_PARENT = new Genre("Genre", null); // Parent par défaut pour Action et Comédie
    private final String nom;
    private final Genre parent;
    
    /**
     *
     * @param nom nom le nom du genre (ex: "Aventure", "Comédie").
     * @param parent le nom du genre parent, ou null si ce genre n'a pas de parent.
     * @throws IllegalArgumentException si les conditions de création du genre ne sont pas respectées.
     */
    public Genre(final String nom, final Genre parent) {

        if (nom == null || nom.trim().isEmpty()) { // Vérifie si le nom est null ou une chaîne vide
            throw new IllegalArgumentException("Le nom du genre ne peut pas être null ou vide.");
        }

        // Si 'Action' ou 'Comédie' est créé avec un parent différent de 'Genre', corriger et avertir.
        if (("Action".equalsIgnoreCase(nom) || "Comédie".equalsIgnoreCase(nom)) && !GENRE_PARENT.equals(parent)) {
            System.out.println("Avertissement : Le genre '" + nom + "' doit avoir 'Genre' comme parent. " +
                    "Le parent a été automatiquement défini sur 'Genre'.");
            this.nom = nom;
            this.parent = GENRE_PARENT;

        } else if (("Aventure".equalsIgnoreCase(nom) || "Western".equalsIgnoreCase(nom)) &&
                (parent == null || !"Action".equalsIgnoreCase(parent.nom))) {
            // Vérification des sous-genres Aventure et Western
            throw new IllegalArgumentException("Le parent du genre " + nom + " doit être 'Action'.");

        } else if (("Musique".equalsIgnoreCase(nom) || "Romance".equalsIgnoreCase(nom)) &&
                (parent == null || !"Comédie".equalsIgnoreCase(parent.nom))) {
            // Vérification des sous-genres Musique et Romance
            throw new IllegalArgumentException("Le parent du genre " + nom + " doit être 'Comédie'.");

        } else if (parent != null && nom.equalsIgnoreCase(parent.nom)) {
            // Vérification pour éviter un parent identique au genre
            throw new IllegalArgumentException("Le genre et le parent ne doivent pas être identiques.");

        } else {
            this.nom = nom;
            this.parent = parent;
        }
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
