package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.CorrectionDTO;
import com.mathtonsavoir.backend.model.Correction;
import com.mathtonsavoir.backend.model.Enseignant;
import com.mathtonsavoir.backend.model.ReponseExercice;
import com.mathtonsavoir.backend.model.Utilisateur;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class CorrectionMapperTest {

    @Test
    void testToDto() {
        // Création des objets liés
        ReponseExercice reponse = new ReponseExercice();
        reponse.setIdReponse(123L);

        Utilisateur correcteur = new Enseignant();
        correcteur.setPseudo("professeur1");

        // Création de l'entité Correction
        Correction correction = Correction.builder()
                .idCorrection(1L)
                .reponse(reponse)
                .correcteur(correcteur)
                .commentaire("Très bien !")
                .dateCorrection(LocalDateTime.of(2025, 8, 15, 15, 30))
                .build();

        // Conversion via le mapper
        CorrectionDTO dto = CorrectionMapper.toDto(correction);

        // Assertions
        assertThat(dto).isNotNull();
        assertThat(dto.reponseId()).isEqualTo(123L);
        assertThat(dto.correcteur()).isEqualTo("professeur1");
        assertThat(dto.commentaire()).isEqualTo("Très bien !");
        assertThat(dto.dateCorrection()).isEqualTo(LocalDateTime.of(2025, 8, 15, 15, 30));
    }
}
