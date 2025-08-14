package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.ReponseExerciceDTO;
import com.mathtonsavoir.backend.mapper.ReponseExerciceMapper;
import com.mathtonsavoir.backend.model.ReponseExercice;
import com.mathtonsavoir.backend.repository.ReponseExerciceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReponseExerciceServiceImplTest {

    @Mock
    private ReponseExerciceRepository reponseExerciceRepository;

    @InjectMocks
    private ReponseExerciceServiceImpl reponseExerciceService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnDto_whenFound() {
        Long id = 1L;
        ReponseExercice reponse = new ReponseExercice();
        ReponseExerciceDTO dto = new ReponseExerciceDTO(id, "utilisateur1", "exercice1", "ma réponse", LocalDateTime.now(), true, BigDecimal.TEN);

        when(reponseExerciceRepository.findById(id)).thenReturn(Optional.of(reponse));

        try (MockedStatic<ReponseExerciceMapper> mocked = mockStatic(ReponseExerciceMapper.class)) {
            mocked.when(() -> ReponseExerciceMapper.toDto(reponse)).thenReturn(dto);

            ReponseExerciceDTO result = reponseExerciceService.getById(id);

            assertEquals(dto, result);
        }
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(reponseExerciceRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> reponseExerciceService.getById(99L));
        assertEquals("Réponse exercice introuvable", ex.getMessage());
    }

    @Test
    void getAll_shouldReturnListOfDtos() {
        ReponseExercice r1 = new ReponseExercice();
        ReponseExercice r2 = new ReponseExercice();

        ReponseExerciceDTO dto1 = new ReponseExerciceDTO(1L, "user1", "ex1", "rep1", LocalDateTime.now(), true, BigDecimal.ONE);
        ReponseExerciceDTO dto2 = new ReponseExerciceDTO(2L, "user2", "ex2", "rep2", LocalDateTime.now(), false, BigDecimal.ZERO);

        when(reponseExerciceRepository.findAll()).thenReturn(List.of(r1, r2));

        try (MockedStatic<ReponseExerciceMapper> mocked = mockStatic(ReponseExerciceMapper.class)) {
            mocked.when(() -> ReponseExerciceMapper.toDto(r1)).thenReturn(dto1);
            mocked.when(() -> ReponseExerciceMapper.toDto(r2)).thenReturn(dto2);

            List<ReponseExerciceDTO> results = reponseExerciceService.getAll();

            assertEquals(2, results.size());
            assertTrue(results.contains(dto1));
            assertTrue(results.contains(dto2));
        }
    }
}
