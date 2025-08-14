package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.mapper.UtilisateurMapper;
import com.mathtonsavoir.backend.model.Enseignant;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.repository.EnseignantRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnseignantServiceImplTest {

    @Mock
    private EnseignantRepository enseignantRepository;

    @InjectMocks
    private EnseignantServiceImpl enseignantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnUtilisateurDTO_whenFound() {
        Enseignant enseignant = new Enseignant();
        enseignant.setPseudo("prof1");
        enseignant.setEmail("prof1@mail.com");
        enseignant.setMotDePasse("pwd");
        enseignant.setRole(Role.ENSEIGNANT);

        UtilisateurDTO expectedDto = new UtilisateurDTO("prof1", "prof1@mail.com", "pwd", Role.ENSEIGNANT, "TROISIEME");

        when(enseignantRepository.findById(1L)).thenReturn(Optional.of(enseignant));

        try (MockedStatic<UtilisateurMapper> mocked = mockStatic(UtilisateurMapper.class)) {
            mocked.when(() -> UtilisateurMapper.toDto(enseignant)).thenReturn(expectedDto);

            UtilisateurDTO result = enseignantService.getById(1L);

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(enseignantRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> enseignantService.getById(99L));
        assertEquals("Enseignant introuvable", ex.getMessage());
    }

    @Test
    void getAll_shouldReturnListOfDTOs() {
        Enseignant e1 = new Enseignant();
        Enseignant e2 = new Enseignant();

        UtilisateurDTO dto1 = new UtilisateurDTO("prof1", "p1@mail.com", "pwd1", Role.ENSEIGNANT, "SIXIEME");
        UtilisateurDTO dto2 = new UtilisateurDTO("prof2", "p2@mail.com", "pwd2", Role.ENSEIGNANT, "CINQUIEME");

        when(enseignantRepository.findAll()).thenReturn(List.of(e1, e2));

        try (MockedStatic<UtilisateurMapper> mocked = mockStatic(UtilisateurMapper.class)) {
            mocked.when(() -> UtilisateurMapper.toDto(e1)).thenReturn(dto1);
            mocked.when(() -> UtilisateurMapper.toDto(e2)).thenReturn(dto2);

            List<UtilisateurDTO> results = enseignantService.getAll();

            assertEquals(2, results.size());
            assertTrue(results.contains(dto1));
            assertTrue(results.contains(dto2));
        }
    }

    @Test
    void create_shouldSaveAndReturnDTO() {
        UtilisateurDTO dto = new UtilisateurDTO("newProf", "new@mail.com", "pwd", Role.ENSEIGNANT, "SECONDE");
        Enseignant enseignant = new Enseignant();
        Enseignant savedEnseignant = new Enseignant();

        UtilisateurDTO expectedDto = new UtilisateurDTO("newProf", "new@mail.com", "pwd", Role.ENSEIGNANT, "SECONDE");

        try (MockedStatic<UtilisateurMapper> mapperMock = mockStatic(UtilisateurMapper.class)) {
            mapperMock.when(() -> UtilisateurMapper.updateEntityFromDto(any(Enseignant.class), eq(dto))).thenAnswer(invocation -> null);
            mapperMock.when(() -> UtilisateurMapper.toDto(any(Enseignant.class))).thenReturn(expectedDto);

            when(enseignantRepository.save(any(Enseignant.class))).thenReturn(savedEnseignant);

            UtilisateurDTO result = enseignantService.create(dto);

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        Long id = 1L;
        UtilisateurDTO dto = new UtilisateurDTO("updatedProf", "upd@mail.com", "newpwd", Role.ENSEIGNANT, "PREMIERE");

        Enseignant existingEnseignant = new Enseignant();
        Enseignant updatedEnseignant = new Enseignant();

        UtilisateurDTO expectedDto = new UtilisateurDTO("updatedProf", "upd@mail.com", "newpwd", Role.ENSEIGNANT, "PREMIERE");

        when(enseignantRepository.findById(id)).thenReturn(Optional.of(existingEnseignant));

        try (MockedStatic<UtilisateurMapper> mapperMock = mockStatic(UtilisateurMapper.class)) {
            mapperMock.when(() -> UtilisateurMapper.updateEntityFromDto(existingEnseignant, dto)).thenAnswer(invocation -> null);
            mapperMock.when(() -> UtilisateurMapper.toDto(updatedEnseignant)).thenReturn(expectedDto);

            when(enseignantRepository.save(existingEnseignant)).thenReturn(updatedEnseignant);

            UtilisateurDTO result = enseignantService.update(id, dto);

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        when(enseignantRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> enseignantService.update(123L, new UtilisateurDTO("pseudo", "email@mail.com", "pwd", Role.ENSEIGNANT, "SECONDE")));
        assertEquals("Enseignant introuvable", ex.getMessage());
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        enseignantService.delete(1L);
        verify(enseignantRepository, times(1)).deleteById(1L);
    }
}
