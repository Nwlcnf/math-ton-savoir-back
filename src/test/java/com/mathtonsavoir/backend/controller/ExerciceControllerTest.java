package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.ExerciceDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.model.Exercice;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.service.ExerciceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ExerciceControllerTest {

    @Mock
    private ExerciceService exerciceService;

    @InjectMocks
    private ExerciceController exerciceController;

    private ExerciceDTO exerciceDTO;
    private Exercice exercice;
    private Lecon lecon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        lecon = Lecon.builder()
                .idLecon(1L)
                .nomLecon("Lecon Test")
                .nomFichierPdf("test.pdf")
                .build();

        exerciceDTO = new ExerciceDTO(
                "QCM",
                "Quel est le résultat de 2+2 ?",
                "4",
                true,
                "FACILE",
                lecon.getIdLecon()
        );

        exercice = Exercice.builder()
                .idExercice(1L)
                .typeExercice("QCM")
                .enonceExercice("Quel est le résultat de 2+2 ?")
                .correctionAuto("4")
                .estAutoCorrecte(true)
                .difficulte("FACILE")
                .lecon(lecon)
                .build();
    }

    @Test
    void testGetById_Success() {
        when(exerciceService.getById(1L)).thenReturn(exerciceDTO);

        ResponseEntity<ExerciceDTO> response = exerciceController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exerciceDTO, response.getBody());
        verify(exerciceService, times(1)).getById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(exerciceService.getById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            exerciceController.getById(1L);
        });

        assertTrue(exception.getMessage().contains("non trouvé"));
        verify(exerciceService, times(1)).getById(1L);
    }

    @Test
    void testGetAll() {
        List<Exercice> exercices = Arrays.asList(exercice);
        when(exerciceService.getAll()).thenReturn(exercices);

        ResponseEntity<List<Exercice>> response = exerciceController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exercices, response.getBody());
        verify(exerciceService, times(1)).getAll();
    }

    @Test
    void testGetAllAsDto() {
        List<ExerciceDTO> dtos = Arrays.asList(exerciceDTO);
        when(exerciceService.getAllAsDto()).thenReturn(dtos);

        ResponseEntity<List<ExerciceDTO>> response = exerciceController.getAllAsDto();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(dtos, response.getBody());
        verify(exerciceService, times(1)).getAllAsDto();
    }

    @Test
    void testCreate() {
        when(exerciceService.create(exerciceDTO)).thenReturn(exerciceDTO);

        ResponseEntity<ExerciceDTO> response = exerciceController.create(exerciceDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(exerciceDTO, response.getBody());
        verify(exerciceService, times(1)).create(exerciceDTO);
    }

    @Test
    void testUpdate_Success() {
        when(exerciceService.update(1L, exerciceDTO)).thenReturn(exerciceDTO);

        ResponseEntity<ExerciceDTO> response = exerciceController.update(1L, exerciceDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(exerciceDTO, response.getBody());
        verify(exerciceService, times(1)).update(1L, exerciceDTO);
    }

    @Test
    void testUpdate_NotFound() {
        when(exerciceService.update(1L, exerciceDTO)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            exerciceController.update(1L, exerciceDTO);
        });

        assertTrue(exception.getMessage().contains("non trouvé"));
        verify(exerciceService, times(1)).update(1L, exerciceDTO);
    }

    @Test
    void testDelete() {
        doNothing().when(exerciceService).delete(1L);

        ResponseEntity<Void> response = exerciceController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(exerciceService, times(1)).delete(1L);
    }
}
