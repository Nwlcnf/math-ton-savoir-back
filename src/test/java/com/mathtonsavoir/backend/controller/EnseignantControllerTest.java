package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.service.EnseignantService;
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

class EnseignantControllerTest {

    @Mock
    private EnseignantService enseignantService;

    @InjectMocks
    private EnseignantController enseignantController;

    private UtilisateurDTO utilisateurDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utilisateurDTO = new UtilisateurDTO(
                "ProfTest",
                "prof@example.com",
                "motdepasse",
                Role.ENSEIGNANT,
                "Terminale"
        );
    }

    @Test
    void testGetById_Success() {
        when(enseignantService.getById(1L)).thenReturn(utilisateurDTO);

        ResponseEntity<UtilisateurDTO> response = enseignantController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(utilisateurDTO, response.getBody());
        verify(enseignantService, times(1)).getById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(enseignantService.getById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            enseignantController.getById(1L);
        });

        assertTrue(exception.getMessage().contains("non trouvé"));
        verify(enseignantService, times(1)).getById(1L);
    }

    @Test
    void testGetAll() {
        List<UtilisateurDTO> enseignants = Arrays.asList(utilisateurDTO);
        when(enseignantService.getAll()).thenReturn(enseignants);

        ResponseEntity<List<UtilisateurDTO>> response = enseignantController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(enseignants, response.getBody());
        verify(enseignantService, times(1)).getAll();
    }

    @Test
    void testCreate() {
        when(enseignantService.create(utilisateurDTO)).thenReturn(utilisateurDTO);

        ResponseEntity<UtilisateurDTO> response = enseignantController.create(utilisateurDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(utilisateurDTO, response.getBody());
        verify(enseignantService, times(1)).create(utilisateurDTO);
    }

    @Test
    void testUpdate_Success() {
        when(enseignantService.update(1L, utilisateurDTO)).thenReturn(utilisateurDTO);

        ResponseEntity<UtilisateurDTO> response = enseignantController.update(1L, utilisateurDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(utilisateurDTO, response.getBody());
        verify(enseignantService, times(1)).update(1L, utilisateurDTO);
    }

    @Test
    void testUpdate_NotFound() {
        when(enseignantService.update(1L, utilisateurDTO)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            enseignantController.update(1L, utilisateurDTO);
        });

        assertTrue(exception.getMessage().contains("non trouvé"));
        verify(enseignantService, times(1)).update(1L, utilisateurDTO);
    }

    @Test
    void testDelete() {
        doNothing().when(enseignantService).delete(1L);

        ResponseEntity<Void> response = enseignantController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(enseignantService, times(1)).delete(1L);
    }
}
