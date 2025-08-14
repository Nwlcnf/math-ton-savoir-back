package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.service.AdminService;
import com.mathtonsavoir.backend.service.EleveService;
import com.mathtonsavoir.backend.service.EnseignantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UtilisateurServiceImplTest {

    @Mock
    private AdminService adminService;

    @Mock
    private EleveService eleveService;

    @Mock
    private EnseignantService enseignantService;

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_shouldReturnAdmin_whenFoundInAdmin() {
        UtilisateurDTO adminDto = new UtilisateurDTO("admin", "admin@mail.com", "pwd", Role.ADMIN, "niveau1");

        when(adminService.getById(1L)).thenReturn(adminDto);

        UtilisateurDTO result = utilisateurService.getById(1L);

        assertEquals(adminDto, result);
        verify(adminService).getById(1L);
        verifyNoInteractions(eleveService, enseignantService);
    }

    @Test
    void getById_shouldReturnEleve_whenNotFoundInAdminButFoundInEleve() {
        UtilisateurDTO eleveDto = new UtilisateurDTO("eleve", "eleve@mail.com", "pwd", Role.ELEVE, "niveau2");

        when(adminService.getById(1L)).thenThrow(new RuntimeException());
        when(eleveService.getById(1L)).thenReturn(eleveDto);

        UtilisateurDTO result = utilisateurService.getById(1L);

        assertEquals(eleveDto, result);
        verify(adminService).getById(1L);
        verify(eleveService).getById(1L);
        verifyNoInteractions(enseignantService);
    }

    @Test
    void getById_shouldReturnEnseignant_whenNotFoundInAdminAndEleveButFoundInEnseignant() {
        UtilisateurDTO ensDto = new UtilisateurDTO("ens", "ens@mail.com", "pwd", Role.ENSEIGNANT, "niveau3");

        when(adminService.getById(1L)).thenThrow(new RuntimeException());
        when(eleveService.getById(1L)).thenThrow(new RuntimeException());
        when(enseignantService.getById(1L)).thenReturn(ensDto);

        UtilisateurDTO result = utilisateurService.getById(1L);

        assertEquals(ensDto, result);
        verify(adminService).getById(1L);
        verify(eleveService).getById(1L);
        verify(enseignantService).getById(1L);
    }

    @Test
    void getById_shouldThrow_whenNotFoundAnywhere() {
        when(adminService.getById(1L)).thenThrow(new RuntimeException());
        when(eleveService.getById(1L)).thenThrow(new RuntimeException());
        when(enseignantService.getById(1L)).thenThrow(new RuntimeException());

        RuntimeException ex = assertThrows(RuntimeException.class, () -> utilisateurService.getById(1L));
        assertEquals("Utilisateur introuvable", ex.getMessage());
    }

    @Test
    void getAll_shouldReturnAllUsersFromAllServices() {
        List<UtilisateurDTO> admins = new ArrayList<>();
        admins.add(new UtilisateurDTO("admin", "admin@mail.com", "pwd", Role.ADMIN, "niv1"));

        List<UtilisateurDTO> eleves = new ArrayList<>();
        eleves.add(new UtilisateurDTO("eleve", "eleve@mail.com", "pwd", Role.ELEVE, "niv2"));

        List<UtilisateurDTO> enseignants = new ArrayList<>();
        enseignants.add(new UtilisateurDTO("ens", "ens@mail.com", "pwd", Role.ENSEIGNANT, "niv3"));

        when(adminService.getAll()).thenReturn(admins);
        when(eleveService.getAll()).thenReturn(eleves);
        when(enseignantService.getAll()).thenReturn(enseignants);

        List<UtilisateurDTO> allUsers = utilisateurService.getAll();

        assertEquals(3, allUsers.size());
        assertTrue(allUsers.containsAll(admins));
        assertTrue(allUsers.containsAll(eleves));
        assertTrue(allUsers.containsAll(enseignants));
    }

    @Test
    void create_shouldCallAdminService_whenRoleIsAdmin() {
        UtilisateurDTO dto = new UtilisateurDTO("admin", "admin@mail.com", "pwd", Role.ADMIN, "niv");
        when(adminService.create(dto)).thenReturn(dto);

        UtilisateurDTO result = utilisateurService.create(dto);

        assertEquals(dto, result);
        verify(adminService).create(dto);
        verifyNoInteractions(eleveService, enseignantService);
    }

    @Test
    void create_shouldCallEleveService_whenRoleIsEleve() {
        UtilisateurDTO dto = new UtilisateurDTO("eleve", "eleve@mail.com", "pwd", Role.ELEVE, "niv");
        when(eleveService.create(dto)).thenReturn(dto);

        UtilisateurDTO result = utilisateurService.create(dto);

        assertEquals(dto, result);
        verify(eleveService).create(dto);
        verifyNoInteractions(adminService, enseignantService);
    }

    @Test
    void create_shouldCallEnseignantService_whenRoleIsEnseignant() {
        UtilisateurDTO dto = new UtilisateurDTO("ens", "ens@mail.com", "pwd", Role.ENSEIGNANT, "niv");
        when(enseignantService.create(dto)).thenReturn(dto);

        UtilisateurDTO result = utilisateurService.create(dto);

        assertEquals(dto, result);
        verify(enseignantService).create(dto);
        verifyNoInteractions(adminService, eleveService);
    }

    @Test
    void update_shouldCallAdminService_whenRoleIsAdmin() {
        UtilisateurDTO dto = new UtilisateurDTO("admin", "admin@mail.com", "pwd", Role.ADMIN, "niv");
        when(adminService.update(1L, dto)).thenReturn(dto);

        UtilisateurDTO result = utilisateurService.update(1L, dto);

        assertEquals(dto, result);
        verify(adminService).update(1L, dto);
        verifyNoInteractions(eleveService, enseignantService);
    }

    @Test
    void update_shouldCallEleveService_whenRoleIsEleve() {
        UtilisateurDTO dto = new UtilisateurDTO("eleve", "eleve@mail.com", "pwd", Role.ELEVE, "niv");
        when(eleveService.update(1L, dto)).thenReturn(dto);

        UtilisateurDTO result = utilisateurService.update(1L, dto);

        assertEquals(dto, result);
        verify(eleveService).update(1L, dto);
        verifyNoInteractions(adminService, enseignantService);
    }

    @Test
    void update_shouldCallEnseignantService_whenRoleIsEnseignant() {
        UtilisateurDTO dto = new UtilisateurDTO("ens", "ens@mail.com", "pwd", Role.ENSEIGNANT, "niv");
        when(enseignantService.update(1L, dto)).thenReturn(dto);

        UtilisateurDTO result = utilisateurService.update(1L, dto);

        assertEquals(dto, result);
        verify(enseignantService).update(1L, dto);
        verifyNoInteractions(adminService, eleveService);
    }


    @Test
    void delete_shouldTryAllDeleteMethods() {
        // On ne teste pas l'effet exact, mais que toutes les méthodes sont appelées sans erreur

        doNothing().when(adminService).delete(1L);
        doNothing().when(eleveService).delete(1L);
        doNothing().when(enseignantService).delete(1L);

        utilisateurService.delete(1L);

        verify(adminService).delete(1L);
        verify(eleveService).delete(1L);
        verify(enseignantService).delete(1L);
    }

    @Test
    void delete_shouldIgnoreExceptions() {
        doThrow(new RuntimeException()).when(adminService).delete(1L);
        doThrow(new RuntimeException()).when(eleveService).delete(1L);
        doThrow(new RuntimeException()).when(enseignantService).delete(1L);

        assertDoesNotThrow(() -> utilisateurService.delete(1L));

        verify(adminService).delete(1L);
        verify(eleveService).delete(1L);
        verify(enseignantService).delete(1L);
    }
}
