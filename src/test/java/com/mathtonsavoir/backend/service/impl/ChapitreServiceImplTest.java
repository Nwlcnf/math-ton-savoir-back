package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.ChapitreDTO;
import com.mathtonsavoir.backend.mapper.ChapitreMapper;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.enums.Classe;
import com.mathtonsavoir.backend.repository.ChapitreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ChapitreServiceImplTest {

    @Mock
    private ChapitreRepository chapitreRepository;

    @InjectMocks
    private ChapitreServiceImpl chapitreService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnChapitre_whenFound() {
        Chapitre chapitre = Chapitre.builder()
                .idChapitre(1L)
                .nomChapitre("Chapitre 1")
                .classe(Classe.CINQUIEME)
                .build();

        when(chapitreRepository.findById(1L)).thenReturn(Optional.of(chapitre));

        Chapitre result = chapitreService.getById(1L);
        assertEquals(chapitre, result);
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(chapitreRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> chapitreService.getById(99L));
        assertEquals("Chapitre introuvable", ex.getMessage());
    }

    @Test
    void getAll_shouldReturnList() {
        Chapitre c1 = Chapitre.builder().idChapitre(1L).nomChapitre("C1").classe(Classe.SIXIEME).build();
        Chapitre c2 = Chapitre.builder().idChapitre(2L).nomChapitre("C2").classe(Classe.CINQUIEME).build();

        when(chapitreRepository.findAll()).thenReturn(List.of(c1, c2));

        List<Chapitre> result = chapitreService.getAll();
        assertEquals(2, result.size());
        assertTrue(result.contains(c1));
        assertTrue(result.contains(c2));
    }

    @Test
    void create_shouldSaveAndReturnDTO() {
        ChapitreDTO inputDto = new ChapitreDTO("Nouveau Chapitre", "SIXIEME");
        Chapitre entityToSave = Chapitre.builder()
                .nomChapitre("Nouveau Chapitre")
                .classe(Classe.SIXIEME)
                .build();

        Chapitre savedEntity = Chapitre.builder()
                .idChapitre(10L)
                .nomChapitre("Nouveau Chapitre")
                .classe(Classe.SIXIEME)
                .build();

        ChapitreDTO expectedDto = new ChapitreDTO("Nouveau Chapitre", "SIXIEME");

        try (MockedStatic<ChapitreMapper> mocked = mockStatic(ChapitreMapper.class)) {
            mocked.when(() -> ChapitreMapper.toEntity(inputDto)).thenReturn(entityToSave);
            mocked.when(() -> ChapitreMapper.toDto(savedEntity)).thenReturn(expectedDto);

            when(chapitreRepository.save(entityToSave)).thenReturn(savedEntity);

            ChapitreDTO result = chapitreService.create(inputDto);
            assertEquals(expectedDto, result);
        }
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        Long id = 1L;
        Chapitre existing = Chapitre.builder()
                .idChapitre(id)
                .nomChapitre("Ancien nom")
                .classe(Classe.SIXIEME)
                .build();

        ChapitreDTO updateDto = new ChapitreDTO("Chapitre Modifié", "CINQUIEME");

        Chapitre updatedEntity = Chapitre.builder()
                .idChapitre(id)
                .nomChapitre("Chapitre Modifié")
                .classe(Classe.CINQUIEME)
                .build();

        ChapitreDTO expectedDto = new ChapitreDTO("Chapitre Modifié", "CINQUIEME");

        when(chapitreRepository.findById(id)).thenReturn(Optional.of(existing));
        when(chapitreRepository.save(existing)).thenReturn(updatedEntity);

        try (MockedStatic<ChapitreMapper> mocked = mockStatic(ChapitreMapper.class)) {
            mocked.when(() -> ChapitreMapper.toDto(updatedEntity)).thenReturn(expectedDto);

            ChapitreDTO result = chapitreService.update(id, updateDto);

            assertEquals(expectedDto, result);
            assertEquals("Chapitre Modifié", existing.getNomChapitre());
            assertEquals(Classe.CINQUIEME, existing.getClasse());
        }
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        when(chapitreRepository.findById(100L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> chapitreService.update(100L,
                new ChapitreDTO("X", "SIXIEME")));
        assertEquals("Chapitre non trouvé", ex.getMessage());
    }

    @Test
    void delete_shouldCallRepositoryDelete_whenExists() {
        when(chapitreRepository.existsById(1L)).thenReturn(true);

        assertDoesNotThrow(() -> chapitreService.delete(1L));

        verify(chapitreRepository, times(1)).deleteById(1L);
    }

    @Test
    void delete_shouldThrow_whenNotExists() {
        when(chapitreRepository.existsById(50L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> chapitreService.delete(50L));
        assertEquals("Chapitre introuvable", ex.getMessage());
    }
}
