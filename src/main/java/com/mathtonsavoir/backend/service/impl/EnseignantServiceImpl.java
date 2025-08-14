package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.UtilisateurDTO;
import com.mathtonsavoir.backend.mapper.UtilisateurMapper;
import com.mathtonsavoir.backend.model.Enseignant;
import com.mathtonsavoir.backend.repository.EnseignantRepository;
import com.mathtonsavoir.backend.service.EnseignantService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnseignantServiceImpl implements EnseignantService {

    private final EnseignantRepository enseignantRepository;

    @Override
    public UtilisateurDTO getById(Long id) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));
        return UtilisateurMapper.toDto(enseignant);
    }

    @Override
    public List<UtilisateurDTO> getAll() {
        return enseignantRepository.findAll()
                .stream()
                .map(UtilisateurMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public UtilisateurDTO create(UtilisateurDTO dto) {
        Enseignant enseignant = new Enseignant();
        UtilisateurMapper.updateEntityFromDto(enseignant, dto);
        Enseignant saved = enseignantRepository.save(enseignant);
        return UtilisateurMapper.toDto(saved);
    }

    @Override
    public UtilisateurDTO update(Long id, UtilisateurDTO dto) {
        Enseignant enseignant = enseignantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Enseignant introuvable"));
        UtilisateurMapper.updateEntityFromDto(enseignant, dto);
        Enseignant updated = enseignantRepository.save(enseignant);
        return UtilisateurMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        enseignantRepository.deleteById(id);
    }
}
