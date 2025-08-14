package com.mathtonsavoir.backend.service;

import com.mathtonsavoir.backend.dto.ReponseExerciceDTO;
import java.util.List;

public interface ReponseExerciceService {
    ReponseExerciceDTO getById(Long id);
    List<ReponseExerciceDTO> getAll();
}
