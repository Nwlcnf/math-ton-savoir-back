package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.ReponseExerciceDTO;
import com.mathtonsavoir.backend.model.Exercice;
import com.mathtonsavoir.backend.model.ReponseExercice;
import com.mathtonsavoir.backend.model.Utilisateur;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ReponseExerciceMapperTest {

    // Sous-classe concrète pour test de l'utilisateur
    static class TestUtilisateur extends Utilisateur {}

    @Test
    void testToDto() {
        // Création des objets liés
        TestUtilisateur utilisateur = new TestUtilisateur();
        utilisateur.setPseudo("eleve123");

        Exercice exercice = new Exercice();
        exercice.setEnonceExercice("Quel est le résultat de 2+2 ?");

        ReponseExercice reponse = ReponseExercice.builder()
                .idReponse(42L)
                .utilisateur(utilisateur)
                .exercice(exercice)
                .reponseDonnee("4")
                .dateReponse(LocalDateTime.of(2025, 8, 15, 16, 0))
                .estJuste(true)
                .note(new BigDecimal("10"))
                .build();

        // Conversion via le mapper
        ReponseExerciceDTO dto = ReponseExerciceMapper.toDto(reponse);

        // Assertions
        assertThat(dto).isNotNull();
        assertThat(dto.id()).isEqualTo(42L);
        assertThat(dto.utilisateur()).isEqualTo("eleve123");
        assertThat(dto.exercice()).isEqualTo("Quel est le résultat de 2+2 ?");
        assertThat(dto.reponseDonnee()).isEqualTo("4");
        assertThat(dto.dateReponse()).isEqualTo(LocalDateTime.of(2025, 8, 15, 16, 0));
        assertThat(dto.estJuste()).isTrue();
        assertThat(dto.note()).isEqualByComparingTo(new BigDecimal("10"));
    }
}
