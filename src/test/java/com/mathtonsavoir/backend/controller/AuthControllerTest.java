package com.mathtonsavoir.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mathtonsavoir.backend.auth.*;
import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.mapper.UtilisateurMapper;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.repository.UtilisateurRepository;
import com.mathtonsavoir.backend.security.JwtService;
import com.mathtonsavoir.backend.security.SecurityUserDetails;
import com.mathtonsavoir.backend.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuthControllerTest {

    private MockMvc mockMvc;
    private UtilisateurRepository utilisateurRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtService jwtService;
    private UtilisateurService utilisateurService;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        utilisateurRepository = mock(UtilisateurRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        authenticationManager = mock(AuthenticationManager.class);
        jwtService = mock(JwtService.class);
        utilisateurService = mock(UtilisateurService.class);
        objectMapper = new ObjectMapper();

        mockMvc = MockMvcBuilders
                .standaloneSetup(new AuthController(
                        utilisateurRepository,
                        passwordEncoder,
                        authenticationManager,
                        jwtService,
                        utilisateurService
                ))
                .setControllerAdvice(new TestExceptionHandler())
                .build();
    }

    @Test
    void testRegister() throws Exception {
        RegisterRequest request = new RegisterRequest(
                "pseudo1",
                "email@test.com",
                "password",
                Role.ADMIN,
                "SIXIEME"
        );

        // Mock password encoding
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        // Mock creation service
        UtilisateurDTO createdDto = new UtilisateurDTO(
                request.pseudo(),
                request.email(),
                "encodedPassword",
                request.role(),
                request.niveau()
        );
        when(utilisateurService.create(any(UtilisateurDTO.class))).thenReturn(createdDto);

        // Mock JWT token
        when(jwtService.generateToken(any(SecurityUserDetails.class))).thenReturn("jwtToken");

        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwtToken"));

        // Vérifie que le mot de passe encodé est bien passé
        ArgumentCaptor<UtilisateurDTO> captor = ArgumentCaptor.forClass(UtilisateurDTO.class);
        verify(utilisateurService).create(captor.capture());
        assertThat(captor.getValue().motDePasse()).isEqualTo("encodedPassword");
    }

    @Test
    void testLoginSuccess() throws Exception {
        LoginRequest request = new LoginRequest("email@test.com", "password");

        // Mock utilisateur existant
        UtilisateurDTO utilisateurDto = new UtilisateurDTO("pseudo1", "email@test.com", "pass", Role.ADMIN, "SIXIEME");
        when(utilisateurRepository.findByEmail("email@test.com")).thenReturn(Optional.of(UtilisateurMapper.toEntity(utilisateurDto)));
        when(jwtService.generateToken(any(SecurityUserDetails.class))).thenReturn("jwtToken");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwtToken"));

        verify(authenticationManager).authenticate(
                new UsernamePasswordAuthenticationToken("email@test.com", "password")
        );
    }

    @Test
    void testLoginUnauthorized() throws Exception {
        LoginRequest request = new LoginRequest("email@test.com", "password");

        doThrow(BadCredentialsException.class).when(authenticationManager).authenticate(any());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void testLoginUserNotFound() throws Exception {
        LoginRequest request = new LoginRequest("email@test.com", "password");

        // Simule utilisateur non trouvé
        when(utilisateurRepository.findByEmail("email@test.com")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Utilisateur non trouvé"));
    }
}
