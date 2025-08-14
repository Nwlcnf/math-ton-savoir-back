package com.mathtonsavoir.backend.service;

import com.mathtonsavoir.backend.dto.LeconDTO;
import com.mathtonsavoir.backend.model.Lecon;

import java.util.List;

public interface LeconService {
    LeconDTO getById(Long id);
    List<Lecon> getAll();

    LeconDTO create(LeconDTO leconDTO);

    LeconDTO update(Long id, LeconDTO leconDTO);
    void delete(Long id);
    public List<Lecon> getLeconsByClasse(String classe);
}
