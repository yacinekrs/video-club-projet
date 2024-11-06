package com.location_video_club;


public class Main {
    public static void main(String[] args) {

        Genre action = new Genre("Action", null);
        Genre comedie = new Genre("Comédie", null);
        Genre aventure = new Genre("Aventure", action);

        // Tentative de créer 'Action' avec un autre parent : correction automatique avec avertissement
        Genre incorrectParent = new Genre("Action", aventure);
    }


}
