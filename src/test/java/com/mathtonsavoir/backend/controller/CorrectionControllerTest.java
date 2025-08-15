package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.CorrectionDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.service.CorrectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CorrectionControllerTest {

    @Mock
    private CorrectionService correctionService;

    @InjectMocks
    private CorrectionController correctionController;

    private CorrectionDTO correctionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        correctionDTO = new CorrectionDTO(
                1L,
                "Correcteur Test",
                "Commentaire test",
                LocalDateTime.now()
        );
    }

    @Test
    void testGetById_Success() {
        when(correctionService.getById(1L)).thenReturn(correctionDTO);

        ResponseEntity<CorrectionDTO> response = correctionController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(correctionDTO, response.getBody());
        verify(correctionService, times(1)).getById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(correctionService.getById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            correctionController.getById(1L);
        });

        assertTrue(exception.getMessage().contains("non trouvée"));
        verify(correctionService, times(1)).getById(1L);
    }

    @Test
    void testGetAll() {
        List<CorrectionDTO> corrections = Arrays.asList(correctionDTO);
        when(correctionService.getAll()).thenReturn(corrections);

        ResponseEntity<List<CorrectionDTO>> response = correctionController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(corrections, response.getBody());
        verify(correctionService, times(1)).getAll();
    }

    @Test
    void testCreate() {
        when(correctionService.create(correctionDTO)).thenReturn(correctionDTO);

        ResponseEntity<CorrectionDTO> response = correctionController.create(correctionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(correctionDTO, response.getBody());
        verify(correctionService, times(1)).create(correctionDTO);
    }

    @Test
    void testUpdate_Success() {
        when(correctionService.update(1L, correctionDTO)).thenReturn(correctionDTO);

        ResponseEntity<CorrectionDTO> response = correctionController.update(1L, correctionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(correctionDTO, response.getBody());
        verify(correctionService, times(1)).update(1L, correctionDTO);
    }

    @Test
    void testUpdate_NotFound() {
        when(correctionService.update(1L, correctionDTO)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            correctionController.update(1L, correctionDTO);
        });

        assertTrue(exception.getMessage().contains("non trouvée"));
        verify(correctionService, times(1)).update(1L, correctionDTO);
    }

    @Test
    void testDelete() {
        doNothing().when(correctionService).delete(1L);

        ResponseEntity<Void> response = correctionController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(correctionService, times(1)).delete(1L);
    }
}
