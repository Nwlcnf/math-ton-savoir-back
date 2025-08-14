package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.ChapitreDTO;
import com.mathtonsavoir.backend.mapper.ChapitreMapper;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.enums.Classe;
import com.mathtonsavoir.backend.repository.ChapitreRepository;
import com.mathtonsavoir.backend.service.ChapitreService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChapitreServiceImpl implements ChapitreService {

    @Autowired
    private final ChapitreRepository chapitreRepository;

    @Override
    public Chapitre getById(Long id) {
        Optional<Chapitre> chapitre = chapitreRepository.findById(id);
        if (chapitre.isPresent()) {
            return chapitre.get();
        } else {
            throw new RuntimeException("Chapitre introuvable");
        }
    }

    @Override
    public List<Chapitre> getAll() {
        return chapitreRepository.findAll();
    }

    @Override
    public ChapitreDTO create(ChapitreDTO chapitreDTO) {
        Chapitre chapitre = ChapitreMapper.toEntity(chapitreDTO);
        Chapitre savedChapitre = chapitreRepository.save(chapitre);
        return ChapitreMapper.toDto(savedChapitre);
    }

    @Override
    public ChapitreDTO update(Long id, ChapitreDTO chapitreDTO) {
        Chapitre existing = chapitreRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Chapitre non trouvé"));

        existing.setNomChapitre(chapitreDTO.nomChapitre());
        existing.setClasse(Classe.valueOf(chapitreDTO.niveau()));

        Chapitre updated = chapitreRepository.save(existing);
        return ChapitreMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        if (!chapitreRepository.existsById(id)) {
            throw new RuntimeException("Chapitre introuvable");
        }
        chapitreRepository.deleteById(id);
    }
}
