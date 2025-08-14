package com.mathtonsavoir.backend.auth;

import com.mathtonsavoir.backend.model.enums.Role;

public record RegisterRequest(
        String pseudo,
        String email,
        String motDePasse,
        Role role,
        String niveau
) {}
