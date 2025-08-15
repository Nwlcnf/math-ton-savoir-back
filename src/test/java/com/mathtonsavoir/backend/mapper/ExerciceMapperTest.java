package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.ExerciceDTO;
import com.mathtonsavoir.backend.model.Exercice;
import com.mathtonsavoir.backend.model.Lecon;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ExerciceMapperTest {

    @Test
    void testToDto() {
        Lecon lecon = new Lecon();
        lecon.setIdLecon(10L);
        lecon.setNomLecon("Leçon 1");
        lecon.setNomFichierPdf("lecon1.pdf");

        Exercice exercice = Exercice.builder()
                .typeExercice("QCM")
                .enonceExercice("Quel est le résultat de 2+2 ?")
                .correctionAuto("4")
                .estAutoCorrecte(true)
                .difficulte("FACILE")
                .lecon(lecon)
                .build();

        ExerciceDTO dto = ExerciceMapper.toDto(exercice);

        // Assertions
        assertThat(dto).isNotNull();
        assertThat(dto.type()).isEqualTo("QCM");
        assertThat(dto.enonce()).isEqualTo("Quel est le résultat de 2+2 ?");
        assertThat(dto.correctionAuto()).isEqualTo("4");
        assertThat(dto.estAutoCorrecte()).isTrue();
        assertThat(dto.difficulte()).isEqualTo("FACILE");
        assertThat(dto.idLecon()).isEqualTo(10L);
    }
}
