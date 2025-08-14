package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.ReponseExerciceDTO;
import com.mathtonsavoir.backend.mapper.ReponseExerciceMapper;
import com.mathtonsavoir.backend.model.ReponseExercice;
import com.mathtonsavoir.backend.repository.ReponseExerciceRepository;
import com.mathtonsavoir.backend.service.ReponseExerciceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReponseExerciceServiceImpl implements ReponseExerciceService {

    @Autowired
    private final ReponseExerciceRepository reponseExerciceRepository;

    @Override
    public ReponseExerciceDTO getById(Long id) {
        ReponseExercice reponse = reponseExerciceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Réponse exercice introuvable"));
        return ReponseExerciceMapper.toDto(reponse);
    }

    @Override
    public List<ReponseExerciceDTO> getAll() {
        return reponseExerciceRepository.findAll()
                .stream()
                .map(ReponseExerciceMapper::toDto)
                .collect(Collectors.toList());
    }
}
