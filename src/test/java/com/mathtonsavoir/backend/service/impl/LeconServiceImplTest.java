package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.LeconDTO;
import com.mathtonsavoir.backend.mapper.LeconMapper;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.model.enums.Classe;
import com.mathtonsavoir.backend.repository.ChapitreRepository;
import com.mathtonsavoir.backend.repository.LeconRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeconServiceImplTest {

    @InjectMocks
    private LeconServiceImpl leconService;

    @Mock
    private LeconRepository leconRepository;

    @Mock
    private ChapitreRepository chapitreRepository;

    @Mock
    private LeconMapper leconMapper;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnLeconDTO_whenLeconExists() {
        Lecon lecon = new Lecon();
        LeconDTO leconDTO = new LeconDTO(
                1L,
                "NomLecon",
                "NomFichier.pdf",
                2L,
                "5eme",
                "Chapitre Description",
                "/api/lecons/pdf/NomFichier.pdf"
        );

        when(leconRepository.findById(1L)).thenReturn(Optional.of(lecon));
        when(leconMapper.toDto(lecon)).thenReturn(leconDTO);

        LeconDTO result = leconService.getById(1L);

        assertEquals(leconDTO, result);
    }

    @Test
    void getById_shouldThrowException_whenLeconNotFound() {
        when(leconRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> leconService.getById(99L));
    }

    @Test
    void getAll_shouldReturnListOfLecons() {
        List<Lecon> lecons = List.of(new Lecon(), new Lecon());
        when(leconRepository.findAll()).thenReturn(lecons);

        List<Lecon> result = leconService.getAll();

        assertEquals(2, result.size());
    }

    @Test
    void create_shouldReturnCreatedLeconDTO() {
        LeconDTO dto = new LeconDTO(
                null,
                "Nom",
                "fichier.pdf",
                10L,
                "6eme",
                "Chapitre Test",
                "/api/lecons/pdf/fichier.pdf"
        );

        Chapitre chapitre = new Chapitre();
        chapitre.setIdChapitre(10L);

        Lecon lecon = new Lecon();
        Lecon savedLecon = new Lecon();
        LeconDTO expectedDto = new LeconDTO(
                1L,
                "TestNom",
                "fichier.pdf",
                10L,
                "6eme",
                "Chapitre Test",
                "/api/lecons/pdf/fichier.pdf"
        );

        when(chapitreRepository.findById(10L)).thenReturn(Optional.of(chapitre));
        when(leconMapper.toEntity(dto)).thenReturn(lecon);
        when(leconRepository.save(lecon)).thenReturn(savedLecon);
        when(leconMapper.toDto(savedLecon)).thenReturn(expectedDto);

        LeconDTO result = leconService.create(dto);

        assertEquals(expectedDto, result);
    }

    @Test
    void update_shouldModifyAndReturnUpdatedLeconDTO() {
        Long leconId = 1L;

        LeconDTO dto = new LeconDTO(
                leconId,
                "NomLecon",
                "NouveauNom.pdf",
                20L,
                "4eme",
                "Chapitre Modifié",
                "/api/lecons/pdf/NouveauNom.pdf"
        );

        Lecon existing = new Lecon();
        Chapitre chapitre = new Chapitre();
        chapitre.setIdChapitre(20L);

        Lecon saved = new Lecon();
        LeconDTO expectedDto = new LeconDTO(
                leconId,
                "NomLecon",
                "NouveauNom.pdf",
                20L,
                "4eme",
                "Chapitre Modifié",
                "/api/lecons/pdf/NouveauNom.pdf"
        );

        when(leconRepository.findById(leconId)).thenReturn(Optional.of(existing));
        when(chapitreRepository.findById(20L)).thenReturn(Optional.of(chapitre));
        when(leconRepository.save(existing)).thenReturn(saved);
        when(leconMapper.toDto(saved)).thenReturn(expectedDto);

        LeconDTO result = leconService.update(leconId, dto);

        assertEquals(expectedDto, result);
    }

    @Test
    void delete_shouldRemoveLecon_whenExists() {
        when(leconRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> leconService.delete(1L));
        verify(leconRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrowException_whenLeconNotFound() {
        when(leconRepository.existsById(99L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> leconService.delete(99L));
    }

    @Test
    void getLeconsByClasse_shouldReturnLecons_whenClasseValide() {
        String classeStr = "5eme";
        Classe classeEnum = Classe.fromLabel(classeStr);
        List<Lecon> lecons = List.of(new Lecon());

        when(leconRepository.findByChapitreClasse(classeEnum)).thenReturn(lecons);

        List<Lecon> result = leconService.getLeconsByClasse(classeStr);

        assertEquals(1, result.size());
    }

    @Test
    void getLeconsByClasse_shouldThrowException_whenClasseInvalide() {
        assertThrows(IllegalArgumentException.class, () -> leconService.getLeconsByClasse("INVALID_CLASSE"));
    }

    @Test
    void getLeconsByClasse_shouldThrowException_whenClasseIsEmpty() {
        assertThrows(IllegalArgumentException.class, () -> leconService.getLeconsByClasse(""));
    }
}
