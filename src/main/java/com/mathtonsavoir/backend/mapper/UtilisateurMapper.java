package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.model.Admin;
import com.mathtonsavoir.backend.model.Eleve;
import com.mathtonsavoir.backend.model.Enseignant;
import com.mathtonsavoir.backend.model.Utilisateur;
import com.mathtonsavoir.backend.model.enums.Role;

public class UtilisateurMapper {

    public static UtilisateurDTO toDto(Utilisateur utilisateur) {

        return new UtilisateurDTO(
                utilisateur.getPseudo(),
                utilisateur.getEmail(),
                utilisateur.getMotDePasse(),
                utilisateur.getRole(),
                utilisateur.getClasse() != null ? utilisateur.getClasse().name() : null

        );
    }

    public static void updateEntityFromDto(Utilisateur utilisateur, UtilisateurDTO dto) {
        utilisateur.setPseudo(dto.pseudo());
        utilisateur.setEmail(dto.email());
        utilisateur.setMotDePasse(dto.motDePasse());
        utilisateur.setRole(Role.fromNomRole(dto.role().getNomRole()));
        utilisateur.setClasse(dto.niveau() != null ? Enum.valueOf(com.mathtonsavoir.backend.model.enums.Classe.class, dto.niveau()) : null);
    }
    public static Utilisateur toEntity(UtilisateurDTO dto) {
        Utilisateur utilisateur;

        switch (dto.role()) {
            case ELEVE -> utilisateur = new Eleve();
            case ENSEIGNANT -> utilisateur = new Enseignant();
            case ADMIN -> utilisateur = new Admin();
            default -> throw new IllegalArgumentException("Rôle inconnu : " + dto.role());
        }

        utilisateur.setPseudo(dto.pseudo());
        utilisateur.setEmail(dto.email());
        utilisateur.setMotDePasse(dto.motDePasse());
        utilisateur.setRole(dto.role());
        utilisateur.setClasse(dto.niveau() != null ?
                Enum.valueOf(com.mathtonsavoir.backend.model.enums.Classe.class, dto.niveau())
                : null
        );

        return utilisateur;
    }

}
