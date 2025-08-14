package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.ExerciceDTO;
import com.mathtonsavoir.backend.mapper.ExerciceMapper;
import com.mathtonsavoir.backend.model.Exercice;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.repository.ExerciceRepository;
import com.mathtonsavoir.backend.repository.LeconRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciceServiceImplTest {

    @Mock
    private ExerciceRepository exerciceRepository;

    @Mock
    private LeconRepository leconRepository;

    @InjectMocks
    private ExerciceServiceImpl exerciceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAll_shouldReturnListOfExercices() {
        Exercice ex1 = Exercice.builder().build();
        Exercice ex2 = Exercice.builder().build();

        when(exerciceRepository.findAll()).thenReturn(List.of(ex1, ex2));

        List<Exercice> result = exerciceService.getAll();

        assertEquals(2, result.size());
        assertTrue(result.contains(ex1));
        assertTrue(result.contains(ex2));
    }

    @Test
    void getAllAsDto_shouldReturnListOfExerciceDTOs() {
        Exercice ex1 = Exercice.builder().lecon(new Lecon()).build();
        Exercice ex2 = Exercice.builder().lecon(new Lecon()).build();

        when(exerciceRepository.findAll()).thenReturn(List.of(ex1, ex2));

        ExerciceDTO dto1 = new ExerciceDTO("type1", "enonce1", "corr1", true, "facile", 1L);
        ExerciceDTO dto2 = new ExerciceDTO("type2", "enonce2", "corr2", false, "moyen", 2L);

        try (MockedStatic<ExerciceMapper> mocked = mockStatic(ExerciceMapper.class)) {
            mocked.when(() -> ExerciceMapper.toDto(ex1)).thenReturn(dto1);
            mocked.when(() -> ExerciceMapper.toDto(ex2)).thenReturn(dto2);

            List<ExerciceDTO> results = exerciceService.getAllAsDto();

            assertEquals(2, results.size());
            assertTrue(results.contains(dto1));
            assertTrue(results.contains(dto2));
        }
    }

    @Test
    void getById_shouldReturnExerciceDTO_whenFound() {
        Long id = 10L;
        Lecon lecon = new Lecon();
        lecon.setNomLecon("Leçon 1");
        Exercice exercice = Exercice.builder().lecon(lecon).build();

        when(exerciceRepository.findById(id)).thenReturn(Optional.of(exercice));

        ExerciceDTO expectedDto = new ExerciceDTO("type", "enonce", "correction", true, "difficile", 1L);

        try (MockedStatic<ExerciceMapper> mocked = mockStatic(ExerciceMapper.class)) {
            mocked.when(() -> ExerciceMapper.toDto(exercice)).thenReturn(expectedDto);

            ExerciceDTO result = exerciceService.getById(id);

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(exerciceRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> exerciceService.getById(99L));
        assertEquals("Exercice introuvable", ex.getMessage());
    }

    @Test
    void create_shouldSaveAndReturnDTO() {
        Lecon lecon = new Lecon();
        lecon.setNomLecon("Leçon 1");
        ExerciceDTO dto = new ExerciceDTO("type", "enonce", "correction", true, "facile", 1L);

        when(leconRepository.findById(dto.idLecon())).thenReturn(Optional.of(lecon));

        Exercice savedExercice = Exercice.builder().lecon(lecon).build();
        ExerciceDTO expectedDto = new ExerciceDTO("type", "enonce", "correction", true, "facile", 1L);

        when(exerciceRepository.save(any(Exercice.class))).thenReturn(savedExercice);

        try (MockedStatic<ExerciceMapper> mocked = mockStatic(ExerciceMapper.class)) {
            mocked.when(() -> ExerciceMapper.toDto(savedExercice)).thenReturn(expectedDto);

            ExerciceDTO result = exerciceService.create(dto);

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void create_shouldThrow_whenLeconNotFound() {
        ExerciceDTO dto = new ExerciceDTO("type", "enonce", "correction", true, "facile", 99L);

        when(leconRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> exerciceService.create(dto));
        assertEquals("Leçon introuvable", ex.getMessage());
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        Long id = 5L;
        Lecon lecon = new Lecon();
        lecon.setNomLecon("Leçon modifiée");

        Exercice exercice = Exercice.builder().build();

        ExerciceDTO dto = new ExerciceDTO("type modifié", "enonce modifié", "correction modifiée", false, "moyen", 2L);

        when(exerciceRepository.findById(id)).thenReturn(Optional.of(exercice));
        when(leconRepository.findById(dto.idLecon())).thenReturn(Optional.of(lecon));

        Exercice updatedExercice = Exercice.builder().lecon(lecon).build();
        ExerciceDTO expectedDto = new ExerciceDTO("type modifié", "enonce modifié", "correction modifiée", false, "moyen", 2L);

        when(exerciceRepository.save(exercice)).thenReturn(updatedExercice);

        try (MockedStatic<ExerciceMapper> mocked = mockStatic(ExerciceMapper.class)) {
            mocked.when(() -> ExerciceMapper.toDto(updatedExercice)).thenReturn(expectedDto);

            ExerciceDTO result = exerciceService.update(id, dto);

            assertEquals(expectedDto, result);
            assertEquals("type modifié", exercice.getTypeExercice());
            assertEquals("enonce modifié", exercice.getEnonceExercice());
            assertEquals("correction modifiée", exercice.getCorrectionAuto());
            assertFalse(exercice.isEstAutoCorrecte());
            assertEquals("moyen", exercice.getDifficulte());
            assertEquals(lecon, exercice.getLecon());
        }
    }

    @Test
    void update_shouldThrow_whenExerciceNotFound() {
        when(exerciceRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> exerciceService.update(123L, new ExerciceDTO(null,null,null,false,null,null)));
        assertEquals("Exercice introuvable", ex.getMessage());
    }

    @Test
    void update_shouldThrow_whenLeconNotFound() {
        Exercice exercice = Exercice.builder().build();

        when(exerciceRepository.findById(1L)).thenReturn(Optional.of(exercice));
        when(leconRepository.findById(99L)).thenReturn(Optional.empty());

        ExerciceDTO dto = new ExerciceDTO("type", "enonce", "correction", true, "facile", 99L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> exerciceService.update(1L, dto));
        assertEquals("Leçon introuvable", ex.getMessage());
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        exerciceService.delete(1L);
        verify(exerciceRepository, times(1)).deleteById(1L);
    }
}
