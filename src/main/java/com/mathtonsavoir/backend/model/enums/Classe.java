package com.mathtonsavoir.backend.model.enums;

public enum Classe {
    SIXIEME("6eme"),
    CINQUIEME("5eme"),
    QUATRIEME("4eme"),
    TROISIEME("3eme");


    private final String nomClasse;

    Classe(String nomClasse) {
        this.nomClasse = nomClasse;
    }

    public String getNomClasse() {
        return nomClasse;
    }

    public static Classe fromLabel(String label) {
        for (Classe c : values()) {
            if (c.nomClasse.equalsIgnoreCase(label)) {
                return c;
            }
        }
        throw new IllegalArgumentException("Classe inconnue : " + label);
    }
}
