package com.mathtonsavoir.backend.service;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;

import java.util.List;

public interface EnseignantService {
    UtilisateurDTO getById(Long id);
    List<UtilisateurDTO> getAll();
    UtilisateurDTO create(UtilisateurDTO dto);
    UtilisateurDTO update(Long id, UtilisateurDTO dto);
    void delete(Long id);
}
