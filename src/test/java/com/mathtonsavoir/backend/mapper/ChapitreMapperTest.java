package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.ChapitreDTO;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.enums.Classe;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChapitreMapperTest {

    @Test
    void testToDto() {
        Chapitre chapitre = new Chapitre();
        chapitre.setNomChapitre("Chapitre 1");
        chapitre.setClasse(Classe.SIXIEME);

        ChapitreDTO dto = ChapitreMapper.toDto(chapitre);

        assertThat(dto).isNotNull();
        assertThat(dto.nomChapitre()).isEqualTo("Chapitre 1");
        assertThat(dto.niveau()).isEqualTo("6eme");
    }

    @Test
    void testToEntity() {
        ChapitreDTO dto = new ChapitreDTO("Chapitre 2", "5eme");

        Chapitre chapitre = ChapitreMapper.toEntity(dto);

        assertThat(chapitre).isNotNull();
        assertThat(chapitre.getNomChapitre()).isEqualTo("Chapitre 2");
        assertThat(chapitre.getClasse()).isEqualTo(Classe.CINQUIEME);
    }

    @Test
    void testRoundTripConversion() {
        Chapitre original = new Chapitre();
        original.setNomChapitre("Chapitre Test");
        original.setClasse(Classe.QUATRIEME);

        ChapitreDTO dto = ChapitreMapper.toDto(original);
        Chapitre converted = ChapitreMapper.toEntity(dto);

        assertThat(converted.getNomChapitre()).isEqualTo(original.getNomChapitre());
        assertThat(converted.getClasse()).isEqualTo(original.getClasse());
    }
}
