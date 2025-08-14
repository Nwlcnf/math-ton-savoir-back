package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.mapper.UtilisateurMapper;
import com.mathtonsavoir.backend.model.Eleve;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.repository.EleveRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EleveServiceImplTest {

    @Mock
    private EleveRepository eleveRepository;

    @InjectMocks
    private EleveServiceImpl eleveService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnUtilisateurDTO_whenFound() {
        Eleve eleve = new Eleve();
        eleve.setPseudo("user1");
        eleve.setEmail("user1@mail.com");
        eleve.setMotDePasse("pwd");
        eleve.setRole(Role.ELEVE);

        UtilisateurDTO expectedDto = new UtilisateurDTO("user1", "user1@mail.com", "pwd", Role.ELEVE, "CINQUIEME");

        when(eleveRepository.findById(1L)).thenReturn(Optional.of(eleve));

        try (MockedStatic<UtilisateurMapper> mocked = mockStatic(UtilisateurMapper.class)) {
            mocked.when(() -> UtilisateurMapper.toDto(eleve)).thenReturn(expectedDto);

            UtilisateurDTO result = eleveService.getById(1L);

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void getById_shouldThrow_whenNotFound() {
        when(eleveRepository.findById(99L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> eleveService.getById(99L));
        assertEquals("Élève introuvable", ex.getMessage());
    }

    @Test
    void getAll_shouldReturnListOfDTOs() {
        Eleve eleve1 = new Eleve();
        Eleve eleve2 = new Eleve();

        UtilisateurDTO dto1 = new UtilisateurDTO("user1", "u1@mail.com", "pwd1", Role.ELEVE, "SIXIEME");
        UtilisateurDTO dto2 = new UtilisateurDTO("user2", "u2@mail.com", "pwd2", Role.ELEVE, "CINQUIEME");

        when(eleveRepository.findAll()).thenReturn(List.of(eleve1, eleve2));

        try (MockedStatic<UtilisateurMapper> mocked = mockStatic(UtilisateurMapper.class)) {
            mocked.when(() -> UtilisateurMapper.toDto(eleve1)).thenReturn(dto1);
            mocked.when(() -> UtilisateurMapper.toDto(eleve2)).thenReturn(dto2);

            List<UtilisateurDTO> results = eleveService.getAll();

            assertEquals(2, results.size());
            assertTrue(results.contains(dto1));
            assertTrue(results.contains(dto2));
        }
    }

    @Test
    void create_shouldSaveAndReturnDTO() {
        UtilisateurDTO dto = new UtilisateurDTO("newUser", "new@mail.com", "pwd", Role.ELEVE, "SIXIEME");
        Eleve eleve = new Eleve();
        Eleve savedEleve = new Eleve();

        UtilisateurDTO expectedDto = new UtilisateurDTO("newUser", "new@mail.com", "pwd", Role.ELEVE, "SIXIEME");

        // Mock UtilisateurMapper.updateEntityFromDto to do nothing (void static method)
        try (MockedStatic<UtilisateurMapper> mapperMock = mockStatic(UtilisateurMapper.class)) {
            mapperMock.when(() -> UtilisateurMapper.updateEntityFromDto(any(Eleve.class), eq(dto))).thenAnswer(invocation -> null);
            mapperMock.when(() -> UtilisateurMapper.toDto(any(Eleve.class))).thenReturn(expectedDto);

            when(eleveRepository.save(any(Eleve.class))).thenReturn(savedEleve);

            UtilisateurDTO result = eleveService.create(dto);

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void update_shouldModifyAndReturnDTO() {
        Long id = 1L;
        UtilisateurDTO dto = new UtilisateurDTO("updatedUser", "upd@mail.com", "newpwd", Role.ELEVE, "CINQUIEME");

        Eleve existingEleve = new Eleve();
        Eleve updatedEleve = new Eleve();

        UtilisateurDTO expectedDto = new UtilisateurDTO("updatedUser", "upd@mail.com", "newpwd", Role.ELEVE, "CINQUIEME");

        when(eleveRepository.findById(id)).thenReturn(Optional.of(existingEleve));

        try (MockedStatic<UtilisateurMapper> mapperMock = mockStatic(UtilisateurMapper.class)) {
            mapperMock.when(() -> UtilisateurMapper.updateEntityFromDto(existingEleve, dto)).thenAnswer(invocation -> null);
            mapperMock.when(() -> UtilisateurMapper.toDto(updatedEleve)).thenReturn(expectedDto);

            when(eleveRepository.save(existingEleve)).thenReturn(updatedEleve);

            UtilisateurDTO result = eleveService.update(id, dto);

            assertEquals(expectedDto, result);
        }
    }

    @Test
    void update_shouldThrow_whenNotFound() {
        when(eleveRepository.findById(123L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> eleveService.update(123L, new UtilisateurDTO("pseudo", "email@mail.com", "pwd", Role.ELEVE, "SIXIEME")));
        assertEquals("Élève introuvable", ex.getMessage());
    }

    @Test
    void delete_shouldCallRepositoryDeleteById() {
        eleveService.delete(1L);
        verify(eleveRepository, times(1)).deleteById(1L);
    }
}
