package com.mathtonsavoir.backend.service;

import com.mathtonsavoir.backend.dto.ExerciceDTO;
import com.mathtonsavoir.backend.model.Exercice;

import java.util.List;

public interface ExerciceService {
    ExerciceDTO getById(Long id);
    List<Exercice> getAll();                  // → retourne les entités pour Swagger
    List<ExerciceDTO> getAllAsDto();          // → retourne les DTO pour ton frontend
    ExerciceDTO create(ExerciceDTO exerciceDTO);
    ExerciceDTO update(Long id, ExerciceDTO exerciceDTO);
    void delete(Long id);
}
