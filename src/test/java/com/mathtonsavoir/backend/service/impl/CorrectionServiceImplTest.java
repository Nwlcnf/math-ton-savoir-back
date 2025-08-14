package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.CorrectionDTO;
import com.mathtonsavoir.backend.mapper.CorrectionMapper;
import com.mathtonsavoir.backend.model.Correction;
import com.mathtonsavoir.backend.model.ReponseExercice;
import com.mathtonsavoir.backend.model.Utilisateur;
import com.mathtonsavoir.backend.repository.CorrectionRepository;
import com.mathtonsavoir.backend.repository.ReponseExerciceRepository;
import com.mathtonsavoir.backend.repository.UtilisateurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CorrectionServiceImplTest {

    @Mock
    private CorrectionRepository correctionRepository;

    @Mock
    private ReponseExerciceRepository reponseExerciceRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @InjectMocks
    private CorrectionServiceImpl correctionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnCorrectionDTO_whenFound() {
        Correction correction = Correction.builder()
                .commentaire("Bien corrigé")
                .dateCorrection(LocalDateTime.now())
                .build();

        CorrectionDTO expectedDto = new CorrectionDTO(1L, "correcteur1", "Bien corrigé", correction.getDateCorrection());

        when(correctionRepository.findById(1L)).thenReturn(Optional.of(correction));

        try (MockedStatic<CorrectionMapper> mocked = mockStatic(CorrectionMapper.class)) {
            mocked.when(() -> CorrectionMapper.toDto(correction)).thenReturn(expectedDto);

            CorrectionDTO result = correctionService.getById(1L);
            assertEquals(expectedDto, result);
        }
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(correctionRepository.findById(100L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> correctionService.getById(100L));
        assertEquals("Correction introuvable", ex.getMessage());
    }

    @Test
    void getAll_shouldReturnListOfCorrectionDTO() {
        Correction corr1 = Correction.builder().commentaire("c1").build();
        Correction corr2 = Correction.builder().commentaire("c2").build();

        when(correctionRepository.findAll()).thenReturn(List.of(corr1, corr2));

        try (MockedStatic<CorrectionMapper> mocked = mockStatic(CorrectionMapper.class)) {
            mocked.when(() -> CorrectionMapper.toDto(corr1)).thenReturn(new CorrectionDTO(1L, "c1", "c1", null));
            mocked.when(() -> CorrectionMapper.toDto(corr2)).thenReturn(new CorrectionDTO(2L, "c2", "c2", null));

            List<CorrectionDTO> result = correctionService.getAll();
            assertEquals(2, result.size());
        }
    }

    @Test
    void create_shouldSaveAndReturnDTO() {
        CorrectionDTO dto = new CorrectionDTO(5L, "correcteur", "Commentaire", null);

        ReponseExercice reponse = new ReponseExercice();
        Utilisateur correcteur = new Utilisateur() {}; // Utilisateur est abstrait, on crée une instance anonyme

        Correction savedCorrection = Correction.builder()
                .commentaire("Commentaire")
                .reponse(reponse)
                .correcteur(correcteur)
                .dateCorrection(LocalDateTime.now())
                .build();

        CorrectionDTO savedDto = new CorrectionDTO(5L, "correcteur", "Commentaire", savedCorrection.getDateCorrection());

        when(reponseExerciceRepository.findById(5L)).thenReturn(Optional.of(reponse));
        when(utilisateurRepository.findByPseudo("correcteur")).thenReturn(Optional.of(correcteur));
        when(correctionRepository.save(any(Correction.class))).thenReturn(savedCorrection);

        try (MockedStatic<CorrectionMapper> mocked = mockStatic(CorrectionMapper.class)) {
            mocked.when(() -> CorrectionMapper.toDto(savedCorrection)).thenReturn(savedDto);

            CorrectionDTO result = correctionService.create(dto);

            assertEquals(savedDto, result);
        }
    }

    @Test
    void create_shouldThrow_whenReponseNotFound() {
        CorrectionDTO dto = new CorrectionDTO(99L, "correcteur", "Commentaire", null);
        when(reponseExerciceRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> correctionService.create(dto));
        assertEquals("Réponse introuvable", ex.getMessage());
    }

    @Test
    void create_shouldThrow_whenCorrecteurNotFound() {
        CorrectionDTO dto = new CorrectionDTO(5L, "correcteur", "Commentaire", null);
        ReponseExercice reponse = new ReponseExercice();

        when(reponseExerciceRepository.findById(5L)).thenReturn(Optional.of(reponse));
        when(utilisateurRepository.findByPseudo("correcteur")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> correctionService.create(dto));
        assertEquals("Utilisateur (correcteur) introuvable", ex.getMessage());
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        Long id = 1L;
        Correction existing = Correction.builder()
                .commentaire("Old comment")
                .dateCorrection(LocalDateTime.of(2023,1,1,12,0))
                .build();

        CorrectionDTO dto = new CorrectionDTO(10L, "correcteur", "Nouveau commentaire", LocalDateTime.of(2024,5,5,15,0));

        ReponseExercice reponse = new ReponseExercice();
        Utilisateur correcteur = new Utilisateur() {};

        Correction updated = Correction.builder()
                .commentaire(dto.commentaire())
                .dateCorrection(dto.dateCorrection())
                .reponse(reponse)
                .correcteur(correcteur)
                .build();

        when(correctionRepository.findById(id)).thenReturn(Optional.of(existing));
        when(reponseExerciceRepository.findById(dto.reponseId())).thenReturn(Optional.of(reponse));
        when(utilisateurRepository.findByPseudo(dto.correcteur())).thenReturn(Optional.of(correcteur));
        when(correctionRepository.save(existing)).thenReturn(updated);

        try (MockedStatic<CorrectionMapper> mocked = mockStatic(CorrectionMapper.class)) {
            mocked.when(() -> CorrectionMapper.toDto(updated)).thenReturn(dto);

            CorrectionDTO result = correctionService.update(id, dto);

            assertEquals(dto, result);
            assertEquals("Nouveau commentaire", existing.getCommentaire());
            assertEquals(dto.dateCorrection(), existing.getDateCorrection());
            assertEquals(reponse, existing.getReponse());
            assertEquals(correcteur, existing.getCorrecteur());
        }
    }

    @Test
    void update_shouldThrow_whenCorrectionNotFound() {
        when(correctionRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> correctionService.update(123L, new CorrectionDTO(1L, "u", "c", null)));
        assertEquals("Correction introuvable", ex.getMessage());
    }

    @Test
    void update_shouldThrow_whenReponseNotFound() {
        Correction existing = Correction.builder().build();
        when(correctionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reponseExerciceRepository.findById(5L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> correctionService.update(1L, new CorrectionDTO(5L, "u", "c", null)));
        assertEquals("Réponse introuvable", ex.getMessage());
    }

    @Test
    void update_shouldThrow_whenCorrecteurNotFound() {
        Correction existing = Correction.builder().build();
        when(correctionRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(reponseExerciceRepository.findById(5L)).thenReturn(Optional.of(new ReponseExercice()));
        when(utilisateurRepository.findByPseudo("u")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> correctionService.update(1L, new CorrectionDTO(5L, "u", "c", null)));
        assertEquals("Utilisateur (correcteur) introuvable", ex.getMessage());
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        correctionService.delete(1L);
        verify(correctionRepository, times(1)).deleteById(1L);
    }
}
