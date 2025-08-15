package com.mathtonsavoir.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UtilisateurDetailsService utilisateurDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private final String token = "valid.jwt.token";
    private final String email = "test@email.com";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext();
    }

    @Test
    void testDoFilterInternal_WithValidToken() throws ServletException, IOException {
        // Simuler l'en-tête Authorization
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);

        // Simuler l'extraction de l'email
        when(jwtService.extractUsername(token)).thenReturn(email);

        // Simuler le chargement de l'utilisateur
        UserDetails userDetails = new User(email, "password", Collections.emptyList());
        when(utilisateurDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // Simuler la validation du token
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(true);

        // Appeler le filtre
        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // Vérifier que l'authentification a été mise dans le SecurityContext
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth);
        assertTrue(auth instanceof UsernamePasswordAuthenticationToken);
        assertEquals(email, auth.getName());

        // Vérifier que le filterChain continue
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_NoAuthHeader() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // SecurityContext doit rester vide
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }

    @Test
    void testDoFilterInternal_InvalidToken() throws ServletException, IOException {
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtService.extractUsername(token)).thenReturn(email);

        UserDetails userDetails = new User(email, "password", Collections.emptyList());
        when(utilisateurDetailsService.loadUserByUsername(email)).thenReturn(userDetails);

        // Token invalide
        when(jwtService.isTokenValid(token, userDetails)).thenReturn(false);

        jwtAuthFilter.doFilterInternal(request, response, filterChain);

        // SecurityContext doit rester vide
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
