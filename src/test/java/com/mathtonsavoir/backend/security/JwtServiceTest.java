package com.mathtonsavoir.backend.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JwtServiceTest {

    private JwtService jwtService;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
        userDetails = new User("test@email.com", "password", java.util.Collections.emptyList());
    }

    @Test
    void testGenerateAndExtractUsername() {
        String token = jwtService.generateToken(userDetails);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals(userDetails.getUsername(), username);
    }

    @Test
    void testIsTokenValid_ValidToken() {
        String token = jwtService.generateToken(userDetails);
        assertTrue(jwtService.isTokenValid(token, userDetails));
    }

    @Test
    void testIsTokenValid_InvalidUser() {
        String token = jwtService.generateToken(userDetails);
        UserDetails otherUser = new User("other@email.com", "password", java.util.Collections.emptyList());
        assertFalse(jwtService.isTokenValid(token, otherUser));
    }

    @Test
    void testIsTokenExpired() throws InterruptedException {
        JwtService shortLivedJwtService = new JwtService() {
            @Override
            public String generateToken(UserDetails userDetails) {
                return io.jsonwebtoken.Jwts.builder()
                        .setSubject(userDetails.getUsername())
                        .setIssuedAt(new java.util.Date())
                        .setExpiration(new java.util.Date(System.currentTimeMillis() + 50)) // 50 ms
                        .signWith(io.jsonwebtoken.security.Keys.hmacShaKeyFor(
                                "mathTonSavoirSuperCleSecretePourJWTSigné".getBytes()), io.jsonwebtoken.SignatureAlgorithm.HS256)
                        .compact();
            }
        };

        String token = shortLivedJwtService.generateToken(userDetails);
        Thread.sleep(100); // attendre l'expiration

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            shortLivedJwtService.extractUsername(token);
        });
    }


    @Test
    void testExtractTokenFromRequest() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("Bearer testtoken123");

        String token = jwtService.extractTokenFromRequest(request);
        assertEquals("testtoken123", token);
    }

    @Test
    void testExtractTokenFromRequest_NoHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn(null);

        String token = jwtService.extractTokenFromRequest(request);
        assertNull(token);
    }

    @Test
    void testExtractTokenFromRequest_InvalidHeader() {
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getHeader("Authorization")).thenReturn("InvalidHeader testtoken123");

        String token = jwtService.extractTokenFromRequest(request);
        assertNull(token);
    }
}
