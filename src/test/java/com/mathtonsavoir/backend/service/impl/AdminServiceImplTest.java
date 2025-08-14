package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.mapper.UtilisateurMapper;
import com.mathtonsavoir.backend.model.Admin;
import com.mathtonsavoir.backend.model.enums.Classe;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnUtilisateurDTO_whenAdminExists() {
        Admin admin = Admin.builder()
                .id(1L)
                .pseudo("admin1")
                .email("admin1@mail.com")
                .motDePasse("pass")
                .role(Role.ADMIN)
                .classe(null)
                .build();

        UtilisateurDTO dto = new UtilisateurDTO(
                "",
                "admin1",
                "admin1@mail.com",
                Role.ADMIN,
                null
        );

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        try (MockedStatic<UtilisateurMapper> mocked = mockStatic(UtilisateurMapper.class)) {
            mocked.when(() -> UtilisateurMapper.toDto(admin)).thenReturn(dto);

            UtilisateurDTO result = adminService.getById(1L);
            assertEquals(dto, result);
        }
    }

    @Test
    void getById_shouldThrowException_whenAdminNotFound() {
        when(adminRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> adminService.getById(99L));
    }

    @Test
    void getAll_shouldReturnListOfDTOs() {
        Admin a1 = Admin.builder().id(1L).pseudo("a1").email("a1@mail.com").motDePasse("p1").role(Role.ADMIN).build();
        Admin a2 = Admin.builder().id(2L).pseudo("a2").email("a2@mail.com").motDePasse("p2").role(Role.ADMIN).build();

        when(adminRepository.findAll()).thenReturn(List.of(a1, a2));

        try (MockedStatic<UtilisateurMapper> mocked = mockStatic(UtilisateurMapper.class)) {
            mocked.when(() -> UtilisateurMapper.toDto(a1))
                    .thenReturn(new UtilisateurDTO("pseudoA", "a1", "a1@mail.com", Role.ADMIN, null));
            mocked.when(() -> UtilisateurMapper.toDto(a2))
                    .thenReturn(new UtilisateurDTO("pseudoB", "a2", "a2@mail.com", Role.ADMIN, null));

            List<UtilisateurDTO> result = adminService.getAll();

            assertEquals(2, result.size());
            assertEquals("pseudoA", result.get(0).pseudo());
            assertEquals("pseudoB", result.get(1).pseudo());
        }
    }

    @Test
    void create_shouldReturnSavedDTO() {
        UtilisateurDTO inputDto = new UtilisateurDTO("pseudo", "admin", "admin@mail.com", Role.ADMIN, null);
        Admin toSave = new Admin();
        Admin saved = Admin.builder()
                .id(10L)
                .pseudo("admin")
                .email("admin@mail.com")
                .motDePasse("secret")
                .role(Role.ADMIN)
                .classe(null)
                .build();

        UtilisateurDTO expectedDto = new UtilisateurDTO("pseudo", "admin", "admin@mail.com", Role.ADMIN,null);

        try (MockedStatic<UtilisateurMapper> mocked = mockStatic(UtilisateurMapper.class)) {
            mocked.when(() -> UtilisateurMapper.updateEntityFromDto(toSave, inputDto)).thenCallRealMethod();
            mocked.when(() -> UtilisateurMapper.toDto(saved)).thenReturn(expectedDto);

            when(adminRepository.save(any(Admin.class))).thenReturn(saved);

            UtilisateurDTO result = adminService.create(inputDto);
            assertEquals(expectedDto, result);
        }
    }

    @Test
    void update_shouldReturnUpdatedDTO() {
        Long id = 1L;
        UtilisateurDTO dto = new UtilisateurDTO("pseudo", "update", "update@mail.com", Role.ADMIN, null);
        Admin existing = new Admin();
        Admin updated = Admin.builder()
                .id(1L)
                .pseudo("update")
                .email("update@mail.com")
                .motDePasse("pwd")
                .role(Role.ADMIN)
                .classe(null)
                .build();

        UtilisateurDTO expected = new UtilisateurDTO("pseudo1", "update", "update@mail.com", Role.ADMIN, null);

        when(adminRepository.findById(id)).thenReturn(Optional.of(existing));
        when(adminRepository.save(existing)).thenReturn(updated);

        try (MockedStatic<UtilisateurMapper> mocked = mockStatic(UtilisateurMapper.class)) {
            mocked.when(() -> UtilisateurMapper.updateEntityFromDto(existing, dto)).thenCallRealMethod();
            mocked.when(() -> UtilisateurMapper.toDto(updated)).thenReturn(expected);

            UtilisateurDTO result = adminService.update(id, dto);
            assertEquals(expected, result);
        }
    }

    @Test
    void update_shouldThrow_whenAdminNotFound() {
        when(adminRepository.findById(42L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> adminService.update(42L,
                new UtilisateurDTO("pseudo42", "xx", "xx",  Role.ADMIN, null)));
    }

    @Test
    void delete_shouldCallRepository() {
        adminService.delete(10L);
        verify(adminRepository).deleteById(10L);
    }
}
