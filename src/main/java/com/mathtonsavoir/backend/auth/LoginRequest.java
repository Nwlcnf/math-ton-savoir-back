package com.mathtonsavoir.backend.auth;

public record LoginRequest(
        String email,
        String motDePasse
) {}
