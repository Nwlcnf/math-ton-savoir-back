package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.service.EleveService;
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

class EleveControllerTest {

    @Mock
    private EleveService eleveService;

    @InjectMocks
    private EleveController eleveController;

    private UtilisateurDTO utilisateurDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        utilisateurDTO = new UtilisateurDTO(
                "PseudoTest",
                "test@example.com",
                "motdepasse",
                Role.ELEVE,
                "1ère"
        );
    }

    @Test
    void testGetById_Success() {
        when(eleveService.getById(1L)).thenReturn(utilisateurDTO);

        ResponseEntity<UtilisateurDTO> response = eleveController.getById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(utilisateurDTO, response.getBody());
        verify(eleveService, times(1)).getById(1L);
    }

    @Test
    void testGetById_NotFound() {
        when(eleveService.getById(1L)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            eleveController.getById(1L);
        });

        assertTrue(exception.getMessage().contains("non trouvé"));
        verify(eleveService, times(1)).getById(1L);
    }

    @Test
    void testGetAll() {
        List<UtilisateurDTO> eleves = Arrays.asList(utilisateurDTO);
        when(eleveService.getAll()).thenReturn(eleves);

        ResponseEntity<List<UtilisateurDTO>> response = eleveController.getAll();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eleves, response.getBody());
        verify(eleveService, times(1)).getAll();
    }

    @Test
    void testCreate() {
        when(eleveService.create(utilisateurDTO)).thenReturn(utilisateurDTO);

        ResponseEntity<UtilisateurDTO> response = eleveController.create(utilisateurDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(utilisateurDTO, response.getBody());
        verify(eleveService, times(1)).create(utilisateurDTO);
    }

    @Test
    void testUpdate_Success() {
        when(eleveService.update(1L, utilisateurDTO)).thenReturn(utilisateurDTO);

        ResponseEntity<UtilisateurDTO> response = eleveController.update(1L, utilisateurDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(utilisateurDTO, response.getBody());
        verify(eleveService, times(1)).update(1L, utilisateurDTO);
    }

    @Test
    void testUpdate_NotFound() {
        when(eleveService.update(1L, utilisateurDTO)).thenReturn(null);

        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            eleveController.update(1L, utilisateurDTO);
        });

        assertTrue(exception.getMessage().contains("non trouvé"));
        verify(eleveService, times(1)).update(1L, utilisateurDTO);
    }

    @Test
    void testDelete() {
        doNothing().when(eleveService).delete(1L);

        ResponseEntity<Void> response = eleveController.delete(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(eleveService, times(1)).delete(1L);
    }
}
