package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.LeconDTO;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.repository.ChapitreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class LeconMapperTest {

    private ChapitreRepository chapitreRepository;
    private LeconMapper leconMapper;

    @BeforeEach
    void setUp() {
        chapitreRepository = Mockito.mock(ChapitreRepository.class);
        leconMapper = new LeconMapper(chapitreRepository);
    }

    @Test
    void testToEntity_success() {
        // Création d'un chapitre simulé
        Chapitre chapitre = new Chapitre();
        chapitre.setIdChapitre(1L);

        // DTO à convertir
        LeconDTO dto = new LeconDTO(
                null,                // idLecon
                "Leçon 1",           // nomLecon
                "lecon1.pdf",        // nomFichierPdf
                1L,                  // chapitreId
                null, null, null     // niveau, description, pdfUrl (ignorés pour toEntity)
        );

        // Mock du repository
        Mockito.when(chapitreRepository.findById(1L)).thenReturn(Optional.of(chapitre));

        // Conversion
        Lecon lecon = leconMapper.toEntity(dto);

        // Assertions
        assertThat(lecon).isNotNull();
        assertThat(lecon.getNomLecon()).isEqualTo("Leçon 1");
        assertThat(lecon.getNomFichierPdf()).isEqualTo("lecon1.pdf");
        assertThat(lecon.getChapitre()).isEqualTo(chapitre);
    }

    @Test
    void testToEntity_chapitreNotFound() {
        LeconDTO dto = new LeconDTO(
                null, "Leçon inconnue", "file.pdf",
                99L, null, null, null
        );

        Mockito.when(chapitreRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> leconMapper.toEntity(dto));
    }

    @Test
    void testToDto() {
        // Chapitre et leçon simulés
        Chapitre chapitre = new Chapitre();
        chapitre.setIdChapitre(2L);
        chapitre.setNomChapitre("Chapitre Test");

        Lecon lecon = new Lecon();
        lecon.setIdLecon(5L);
        lecon.setNomLecon("Leçon 5");
        lecon.setNomFichierPdf("lecon5.pdf");
        lecon.setChapitre(chapitre);

        // Conversion via mapper
        LeconDTO dto = leconMapper.toDto(lecon);

        // Assertions
        assertThat(dto).isNotNull();
        assertThat(dto.idLecon()).isEqualTo(5L);
        assertThat(dto.nomLecon()).isEqualTo("Leçon 5");
        assertThat(dto.nomFichierPdf()).isEqualTo("lecon5.pdf");
        assertThat(dto.chapitreId()).isEqualTo(2L);
        assertThat(dto.description()).isEqualTo("Chapitre Test");
        assertThat(dto.pdfUrl()).isEqualTo("/api/lecons/pdf/lecon5.pdf");
    }
}
