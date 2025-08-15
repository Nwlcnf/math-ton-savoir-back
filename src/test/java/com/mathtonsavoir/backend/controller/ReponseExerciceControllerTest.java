package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.ReponseExerciceDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.service.ReponseExerciceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReponseExerciceControllerTest {

    @Mock
    private ReponseExerciceService reponseExerciceService;

    @InjectMocks
    private ReponseExerciceController reponseExerciceController;

    private ReponseExerciceDTO reponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        reponseDTO = new ReponseExerciceDTO(
                1L,
                "UtilisateurTest",
                "ExerciceTest",
                "Ma réponse",
                LocalDateTime.now(),
                true,
                BigDecimal.valueOf(10)
        );
    }

    @Test
    void testGetById_Success() {
        when(reponseExerciceService.getById(1L)).thenReturn(reponseDTO);

        ResponseEntity<ReponseExerciceDTO> response = reponseExerciceController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(reponseDTO, response.getBody());
        verify(reponseExerciceService, times(1)).getById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(reponseExerciceService.getById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            reponseExerciceController.getById(1L);
        });

        assertTrue(exception.getMessage().contains("non trouvée"));
        verify(reponseExerciceService, times(1)).getById(1L);
    }

    @Test
    void testGetAll() {
        List<ReponseExerciceDTO> dtos = Arrays.asList(reponseDTO);
        when(reponseExerciceService.getAll()).thenReturn(dtos);

        ResponseEntity<List<ReponseExerciceDTO>> response = reponseExerciceController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());
        verify(reponseExerciceService, times(1)).getAll();
    }
}
