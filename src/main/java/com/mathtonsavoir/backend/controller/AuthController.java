package com.mathtonsavoir.backend.controller;

import com.mathtonsavoir.backend.auth.*;
import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.exception.ResourceNotFoundException;
import com.mathtonsavoir.backend.model.*;
import com.mathtonsavoir.backend.model.enums.Classe;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.repository.UtilisateurRepository;
import com.mathtonsavoir.backend.security.JwtService;
import com.mathtonsavoir.backend.security.SecurityUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.mathtonsavoir.backend.mapper.UtilisateurMapper;
import com.mathtonsavoir.backend.service.UtilisateurService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurRepository utilisateurRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UtilisateurService utilisateurService;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {

        // Création DTO avec mot de passe encodé
        UtilisateurDTO dto = new UtilisateurDTO(
                request.pseudo(),
                request.email(),
                passwordEncoder.encode(request.motDePasse()),
                request.role(),
                request.niveau()
        );

        UtilisateurDTO createdDto = utilisateurService.create(dto);

        Utilisateur createdEntity = UtilisateurMapper.toEntity(createdDto);

        String token = jwtService.generateToken(new SecurityUserDetails(createdEntity));

        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.email(), request.motDePasse())
            );
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Utilisateur utilisateur = utilisateurRepository.findByEmail(request.email())
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

        String token = jwtService.generateToken(new SecurityUserDetails(utilisateur));
        return ResponseEntity.ok(new AuthResponse(token));
    }

}
