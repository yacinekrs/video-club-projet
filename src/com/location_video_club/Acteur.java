package com.location_video_club;

public class Acteur {

        private final String nom;
        private final String prenom;
        /**
         * les attributs de l'acteur
         * @param anom  le nom de l'acteur
         * @param aprenom le prenom de l'acteur
         */
        public Acteur(final String anom, final String aprenom) {
            this.nom=anom;
            this.prenom=aprenom;            
        }
        /**
         * Retourne le nom de l'acteur
         * @return
         */
        public String getNom() {
            return nom;
            }
        /**
         * Retourne le prenom de l'acteur
         * @return
         */
        public String getPrenom() {
            return prenom;
            }

        @Override
        public String toString() {
            return nom + " " + prenom;
            }
        @Override
        public int hashCode() { 
            final int prime = 31;      
            int result = 1;
            result = prime * result + ((nom == null) ? 0 : nom.hashCode());
            result = prime * result + ((prenom == null) ? 0 : prenom.hashCode());
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
            Acteur other = (Acteur) obj;
            if (nom == null) {
                if (other.nom != null)
                    return false;
            } else if (!nom.equals(other.nom))
                return false;
            if (prenom == null) {
                if (other.prenom != null)
                    return false;
            } else if (!prenom.equals(other.prenom))
                return false;
            return true;
    }

}



