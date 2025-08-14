package com.mathtonsavoir.backend.service;

import com.mathtonsavoir.backend.dto.CorrectionDTO;

import java.util.List;

public interface CorrectionService {
    CorrectionDTO getById(Long id);
    List<CorrectionDTO> getAll();
    CorrectionDTO create(CorrectionDTO correctionDTO);
    CorrectionDTO update(Long id, CorrectionDTO correctionDTO);
    void delete(Long id);
}
