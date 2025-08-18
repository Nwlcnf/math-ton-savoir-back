package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.model.Admin;
import com.mathtonsavoir.backend.model.Eleve;
import com.mathtonsavoir.backend.model.Enseignant;
import com.mathtonsavoir.backend.model.Utilisateur;
import com.mathtonsavoir.backend.model.enums.Classe;
import com.mathtonsavoir.backend.model.enums.Role;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UtilisateurMapperTest {

    @Test
    void testToDto() {
        Eleve eleve = new Eleve();
        eleve.setPseudo("eleve1");
        eleve.setEmail("eleve@test.com");
        eleve.setMotDePasse("secret");
        eleve.setRole(Role.ELEVE);
        eleve.setClasse(Classe.SIXIEME);

        UtilisateurDTO dto = UtilisateurMapper.toDto(eleve);

        assertThat(dto.pseudo()).isEqualTo("eleve1");
        assertThat(dto.email()).isEqualTo("eleve@test.com");
        assertThat(dto.motDePasse()).isEqualTo("secret");
        assertThat(dto.role()).isEqualTo(Role.ELEVE);
        assertThat(dto.niveau()).isEqualTo("SIXIEME");
    }

    @Test
    void testToEntity_eleve() {
        UtilisateurDTO dto = new UtilisateurDTO("eleve1", "eleve@test.com", "secret", Role.ELEVE, "SIXIEME");

        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);

        assertThat(utilisateur).isInstanceOf(Eleve.class);
        assertThat(utilisateur.getPseudo()).isEqualTo("eleve1");
        assertThat(utilisateur.getEmail()).isEqualTo("eleve@test.com");
        assertThat(utilisateur.getMotDePasse()).isEqualTo("secret");
        assertThat(utilisateur.getRole()).isEqualTo(Role.ELEVE);
        assertThat(utilisateur.getClasse()).isEqualTo(Classe.SIXIEME);
    }

    @Test
    void testToEntity_enseignant() {
        UtilisateurDTO dto = new UtilisateurDTO("prof1", "prof@test.com", "secret", Role.ENSEIGNANT, null);

        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);

        assertThat(utilisateur).isInstanceOf(Enseignant.class);
        assertThat(utilisateur.getPseudo()).isEqualTo("prof1");
        assertThat(utilisateur.getRole()).isEqualTo(Role.ENSEIGNANT);
        assertThat(utilisateur.getClasse()).isNull();
    }

    @Test
    void testToEntity_admin() {
        UtilisateurDTO dto = new UtilisateurDTO("admin", "admin@test.com", "secret", Role.ADMIN, null);

        Utilisateur utilisateur = UtilisateurMapper.toEntity(dto);

        assertThat(utilisateur).isInstanceOf(Admin.class);
        assertThat(utilisateur.getPseudo()).isEqualTo("admin");
        assertThat(utilisateur.getRole()).isEqualTo(Role.ADMIN);
        assertThat(utilisateur.getClasse()).isNull();
    }

    @Test
    void testUpdateEntityFromDto() {
        Eleve eleve = new Eleve();
        eleve.setPseudo("old");
        eleve.setEmail("old@test.com");
        eleve.setMotDePasse("oldpwd");
        eleve.setRole(Role.ELEVE);
        eleve.setClasse(Classe.SIXIEME);

        UtilisateurDTO dto = new UtilisateurDTO("newEleve", "new@test.com", "newpwd", Role.ELEVE, "CINQUIEME");

        UtilisateurMapper.updateEntityFromDto(eleve, dto);

        assertThat(eleve.getPseudo()).isEqualTo("newEleve");
        assertThat(eleve.getEmail()).isEqualTo("new@test.com");
        assertThat(eleve.getMotDePasse()).isEqualTo("newpwd");
        assertThat(eleve.getRole()).isEqualTo(Role.ELEVE);
        assertThat(eleve.getClasse()).isEqualTo(Classe.CINQUIEME);
    }
}
