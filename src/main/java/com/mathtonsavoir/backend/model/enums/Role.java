package com.mathtonsavoir.backend.model.enums;

public enum Role {
    ADMIN("Administrateur"),
    ENSEIGNANT("Enseignant"),
    ELEVE("Élève");

    private final String nomRole;

    Role(String nomRole) {
        this.nomRole = nomRole;
    }

    public static Role fromNomRole(String nomRole) {
        for (Role role : values()) {
            if (role.getNomRole().equalsIgnoreCase(nomRole)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Aucun rôle ne correspond à : " + nomRole);
    }


    public String getNomRole() {
        return nomRole;
    }

    public static Role fromLabel(String label) {
        for (Role r : values()) {
            if (r.nomRole.equalsIgnoreCase(label)) {
                return r;
            }
        }
        throw new IllegalArgumentException("Rôle inconnu : " + label);
    }

    public String toUpperCase() {
        return this.name().toUpperCase();
    }
}
