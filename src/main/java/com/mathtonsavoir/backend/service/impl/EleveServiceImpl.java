package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.mapper.UtilisateurMapper;
import com.mathtonsavoir.backend.model.Eleve;
import com.mathtonsavoir.backend.repository.EleveRepository;
import com.mathtonsavoir.backend.service.EleveService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EleveServiceImpl implements EleveService {

    private final EleveRepository eleveRepository;

    @Override
    public UtilisateurDTO getById(Long id) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));
        return UtilisateurMapper.toDto(eleve);
    }

    @Override
    public List<UtilisateurDTO> getAll() {
        return eleveRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurDTO create(UtilisateurDTO dto) {
        Eleve eleve = new Eleve();
        UtilisateurMapper.updateEntityFromDto(eleve, dto);
        Eleve saved = eleveRepository.save(eleve);
        return UtilisateurMapper.toDto(saved);
    }

    @Override
    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        Eleve eleve = eleveRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Élève introuvable"));
        UtilisateurMapper.updateEntityFromDto(eleve, dto);
        Eleve updated = eleveRepository.save(eleve);
        return UtilisateurMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        eleveRepository.deleteById(id);
    }
}
