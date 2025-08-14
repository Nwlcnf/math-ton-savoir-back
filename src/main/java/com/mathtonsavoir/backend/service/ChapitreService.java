package com.mathtonsavoir.backend.service;

import com.mathtonsavoir.backend.dto.ChapitreDTO;
import com.mathtonsavoir.backend.model.Chapitre;

import java.util.List;

public interface ChapitreService {
    Chapitre getById(Long id);
    List<Chapitre> getAll();
    ChapitreDTO create(ChapitreDTO chapitreDTO);

    ChapitreDTO update(Long id, ChapitreDTO chapitreDTO);

    void delete(Long id);
}
