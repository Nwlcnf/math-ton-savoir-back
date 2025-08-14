package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.model.enums.Role;
import com.mathtonsavoir.backend.service.AdminService;
import com.mathtonsavoir.backend.service.EleveService;
import com.mathtonsavoir.backend.service.EnseignantService;
import com.mathtonsavoir.backend.service.UtilisateurService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class UtilisateurServiceImpl implements UtilisateurService {

    private final AdminService adminService;
    private final EleveService eleveService;
    private final EnseignantService enseignantService;


    @Override
    public UtilisateurDTO getById(Long id) {
        try {
            return adminService.getById(id);
        } catch (RuntimeException ignored) {}
        try {
            return eleveService.getById(id);
        } catch (RuntimeException ignored) {}
        try {
            return enseignantService.getById(id);
        } catch (RuntimeException ignored) {}
        throw new RuntimeException("Utilisateur introuvable");
    }

    @Override
    public List<UtilisateurDTO> getAll() {
        var all = adminService.getAll();
        all.addAll(eleveService.getAll());
        all.addAll(enseignantService.getAll());
        return all;
    }

    @Override
    public UtilisateurDTO create(UtilisateurDTO dto) {
        Role role;
        try {
            role = Role.valueOf(dto.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rôle invalide : " + dto.role());
        }

        return switch (role) {
            case ADMIN -> adminService.create(dto);
            case ELEVE -> eleveService.create(dto);
            case ENSEIGNANT -> enseignantService.create(dto);
        };
    }

    @Override
    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        Role role;
        try {
            role = Role.valueOf(dto.role().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Rôle invalide : " + dto.role());
        }

        return switch (role) {
            case ADMIN -> adminService.update(id, dto);
            case ELEVE -> eleveService.update(id, dto);
            case ENSEIGNANT -> enseignantService.update(id, dto);
        };
    }

    @Override
    public void delete(Long id) {
        try { adminService.delete(id); } catch (Exception ignored) {}
        try { eleveService.delete(id); } catch (Exception ignored) {}
        try { enseignantService.delete(id); } catch (Exception ignored) {}
    }
}
