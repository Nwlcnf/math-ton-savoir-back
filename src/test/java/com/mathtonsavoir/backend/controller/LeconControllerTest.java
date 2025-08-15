package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.dto.LeconDTO;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.model.PdfLecon;
import com.mathtonsavoir.backend.repository.ChapitreRepository;
import com.mathtonsavoir.backend.repository.LeconRepository;
import com.mathtonsavoir.backend.repository.PdfLeconRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LeconControllerTest {

    @Mock
    private LeconRepository leconRepository;

    @Mock
    private ChapitreRepository chapitreRepository;

    @Mock
    private PdfLeconRepository pdfLeconRepository;

    @InjectMocks
    private LeconController leconController;

    private Chapitre chapitre;
    private Lecon lecon;
    private PdfLecon pdfLecon;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        chapitre = Chapitre.builder()
                .idChapitre(1L)
                .nomChapitre("Chapitre Test")
                .build();

        lecon = Lecon.builder()
                .idLecon(1L)
                .nomLecon("Lecon Test")
                .nomFichierPdf("fichier.pdf")
                .chapitre(chapitre)
                .build();

        pdfLecon = PdfLecon.builder()
                .id(1L)
                .nomPdf("fichier.pdf")
                .pdfData("test pdf".getBytes())
                .lecon(lecon)
                .build();
    }

    @Test
    void testUploadLecon_Success() throws Exception {
        MultipartFile file = new MockMultipartFile("pdf", "test.pdf", "application/pdf", "contenu".getBytes());

        when(chapitreRepository.findById(1L)).thenReturn(Optional.of(chapitre));
        when(leconRepository.save(any(Lecon.class))).thenReturn(lecon);
        when(pdfLeconRepository.save(any(PdfLecon.class))).thenReturn(pdfLecon);

        ResponseEntity<?> response = leconController.uploadLecon("Lecon Test", 1L, file);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertTrue(response.getBody() instanceof LeconDTO);
        verify(chapitreRepository, times(1)).findById(1L);
        verify(leconRepository, times(1)).save(any(Lecon.class));
        verify(pdfLeconRepository, times(1)).save(any(PdfLecon.class));
    }

    @Test
    void testUploadLecon_ChapitreNotFound() throws Exception {
        MultipartFile file = new MockMultipartFile("pdf", "test.pdf", "application/pdf", "contenu".getBytes());

        when(chapitreRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = leconController.uploadLecon("Lecon Test", 1L, file);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("Chapitre introuvable"));
        verify(chapitreRepository, times(1)).findById(1L);
        verify(leconRepository, never()).save(any());
        verify(pdfLeconRepository, never()).save(any());
    }

    @Test
    void testGetAllLecons() {
        when(leconRepository.findAll()).thenReturn(Arrays.asList(lecon));

        ResponseEntity<List<LeconDTO>> response = leconController.getAllLecons();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("Lecon Test", response.getBody().get(0).nomLecon());
        verify(leconRepository, times(1)).findAll();
    }

    @Test
    void testGetLeconById_Success() {
        when(leconRepository.findById(1L)).thenReturn(Optional.of(lecon));

        ResponseEntity<LeconDTO> response = leconController.getLeconById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Lecon Test", response.getBody().nomLecon());
        verify(leconRepository, times(1)).findById(1L);
    }

    @Test
    void testGetLeconById_NotFound() {
        when(leconRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            leconController.getLeconById(1L);
        });

        assertTrue(exception.getMessage().contains("Leçon introuvable"));
        verify(leconRepository, times(1)).findById(1L);
    }

    @Test
    void testGetPdfByLeconId_Success() {
        when(pdfLeconRepository.findByLecon_IdLecon(1L)).thenReturn(Optional.of(pdfLecon));

        ResponseEntity<byte[]> response = leconController.getPdfByLeconId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals("test pdf".getBytes(), response.getBody());
        assertEquals("application/pdf", response.getHeaders().getContentType().toString());
        assertTrue(response.getHeaders().get(HttpHeaders.CONTENT_DISPOSITION).get(0).contains("fichier.pdf"));
        verify(pdfLeconRepository, times(1)).findByLecon_IdLecon(1L);
    }

    @Test
    void testGetPdfByLeconId_NotFound() {
        when(pdfLeconRepository.findByLecon_IdLecon(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(RuntimeException.class, () -> {
            leconController.getPdfByLeconId(1L);
        });

        assertTrue(exception.getMessage().contains("PDF introuvable"));
        verify(pdfLeconRepository, times(1)).findByLecon_IdLecon(1L);
    }
}
