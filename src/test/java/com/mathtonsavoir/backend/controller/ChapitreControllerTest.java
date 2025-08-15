package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.ChapitreDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.service.ChapitreService;
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

class ChapitreControllerTest {

    @Mock
    private ChapitreService chapitreService;

    @InjectMocks
    private ChapitreController chapitreController;

    private Chapitre chapitre;
    private ChapitreDTO chapitreDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        chapitre = Chapitre.builder()
                .idChapitre(1L)
                .nomChapitre("Chapitre Test")
                .build();

        chapitreDTO = new ChapitreDTO("Chapitre Test", "1ère");
    }

    @Test
    void testGetById_Success() {
        when(chapitreService.getById(1L)).thenReturn(chapitre);

        ResponseEntity<Chapitre> response = chapitreController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chapitre, response.getBody());
        verify(chapitreService, times(1)).getById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(chapitreService.getById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            chapitreController.getById(1L);
        });

        assertTrue(exception.getMessage().contains("non trouvé"));
        verify(chapitreService, times(1)).getById(1L);
    }

    @Test
    void testGetAll() {
        List<Chapitre> chapitres = Arrays.asList(chapitre);
        when(chapitreService.getAll()).thenReturn(chapitres);

        ResponseEntity<List<Chapitre>> response = chapitreController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chapitres, response.getBody());
        verify(chapitreService, times(1)).getAll();
    }

    @Test
    void testCreate_Success() {
        when(chapitreService.create(chapitreDTO)).thenReturn(chapitreDTO);

        ResponseEntity<ChapitreDTO> response = chapitreController.create(chapitreDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(chapitreDTO, response.getBody());
        verify(chapitreService, times(1)).create(chapitreDTO);
    }

    @Test
    void testCreate_InvalidNom() {
        ChapitreDTO invalidDTO = new ChapitreDTO("", "1ère");

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            chapitreController.create(invalidDTO);
        });

        assertTrue(exception.getMessage().contains("Le nom du chapitre est requis"));
        verify(chapitreService, times(0)).create(any());
    }

    @Test
    void testUpdate_Success() {
        when(chapitreService.update(1L, chapitreDTO)).thenReturn(chapitreDTO);

        ResponseEntity<ChapitreDTO> response = chapitreController.update(1L, chapitreDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(chapitreDTO, response.getBody());
        verify(chapitreService, times(1)).update(1L, chapitreDTO);
    }

    @Test
    void testUpdate_NotFound() {
        when(chapitreService.update(1L, chapitreDTO)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            chapitreController.update(1L, chapitreDTO);
        });

        assertTrue(exception.getMessage().contains("Impossible de mettre à jour"));
        verify(chapitreService, times(1)).update(1L, chapitreDTO);
    }

    @Test
    void testDelete() {
        doNothing().when(chapitreService).delete(1L);

        ResponseEntity<Void> response = chapitreController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(chapitreService, times(1)).delete(1L);
    }
}
