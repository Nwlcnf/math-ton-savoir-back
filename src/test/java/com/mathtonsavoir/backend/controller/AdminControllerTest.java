package com.mathtonsavoir.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest {

    private MockMvc mockMvc;
    private AdminService adminService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        adminService = Mockito.mock(AdminService.class);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders.standaloneSetup(new AdminController(adminService))
                .setControllerAdvice(new TestExceptionHandler()) // Gestionnaire global d'erreurs
                .build();
    }

    @Test
    void testGetByIdFound() throws Exception {
        UtilisateurDTO dto = new UtilisateurDTO("admin1", "admin1@test.com", "pass", Role.ADMIN, "N1");

        Mockito.when(adminService.getById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pseudo").value("admin1"))
                .andExpect(jsonPath("$.email").value("admin1@test.com"));
    }

    @Test
    void testGetByIdNotFound() throws Exception {
        Mockito.when(adminService.getById(1L)).thenReturn(null);

        mockMvc.perform(get("/api/admins/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Admin avec ID 1 non trouvé"));
    }

    @Test
    void testGetAll() throws Exception {
        UtilisateurDTO dto1 = new UtilisateurDTO("admin1", "admin1@test.com", "pass", Role.ADMIN, "N1");
        UtilisateurDTO dto2 = new UtilisateurDTO("admin2", "admin2@test.com", "pass", Role.ADMIN, "N2");

        List<UtilisateurDTO> admins = Arrays.asList(dto1, dto2);
        Mockito.when(adminService.getAll()).thenReturn(admins);

        mockMvc.perform(get("/api/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pseudo").value("admin1"))
                .andExpect(jsonPath("$[1].pseudo").value("admin2"));
    }

    @Test
    void testCreate() throws Exception {
        UtilisateurDTO requestDto = new UtilisateurDTO("newAdmin", "new@test.com", "pass", Role.ADMIN, "N1");
        UtilisateurDTO createdDto = new UtilisateurDTO("newAdmin", "new@test.com", "pass", Role.ADMIN, "N1");

        Mockito.when(adminService.create(any(UtilisateurDTO.class))).thenReturn(createdDto);

        mockMvc.perform(post("/api/admins")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.pseudo").value("newAdmin"))
                .andExpect(jsonPath("$.email").value("new@test.com"));
    }

    @Test
    void testUpdateFound() throws Exception {
        UtilisateurDTO requestDto = new UtilisateurDTO("updatedAdmin", "update@test.com", "pass", Role.ADMIN, "N2");
        UtilisateurDTO updatedDto = new UtilisateurDTO("updatedAdmin", "update@test.com", "pass", Role.ADMIN, "N2");

        Mockito.when(adminService.update(eq(1L), any(UtilisateurDTO.class))).thenReturn(updatedDto);

        mockMvc.perform(put("/api/admins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.pseudo").value("updatedAdmin"));
    }

    @Test
    void testUpdateNotFound() throws Exception {
        UtilisateurDTO requestDto = new UtilisateurDTO("updatedAdmin", "update@test.com", "pass", Role.ADMIN, "N2");

        Mockito.when(adminService.update(eq(1L), any(UtilisateurDTO.class))).thenReturn(null);

        mockMvc.perform(put("/api/admins/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Admin avec ID 1 non trouvé"));
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/api/admins/1"))
                .andExpect(status().isNoContent());

        Mockito.verify(adminService).delete(1L);
    }
}
