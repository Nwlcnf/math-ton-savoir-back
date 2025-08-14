package com.mathtonsavoir.backend.dto;

import com.mathtonsavoir.backend.model.enums.Role;

public record UtilisateurDTO(
        String pseudo,
        String email,
        String motDePasse,
        Role role,
        String niveau
) {

    public UtilisateurDTO {
        if (pseudo == null || email == null || role == null ) {
            throw new IllegalArgumentException("All fields must be given");
        }
    }
}
