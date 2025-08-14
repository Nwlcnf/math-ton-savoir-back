// LeconServiceImpl.java
package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.LeconDTO;
import com.mathtonsavoir.backend.mapper.LeconMapper;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.model.enums.Classe;
import com.mathtonsavoir.backend.repository.ChapitreRepository;
import com.mathtonsavoir.backend.repository.LeconRepository;
import com.mathtonsavoir.backend.service.LeconService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LeconServiceImpl implements LeconService {

    private final LeconRepository leconRepository;
    private final LeconMapper leconMapper;
    private final ChapitreRepository chapitreRepository;

    @Override
    public LeconDTO getById(Long id) {
        Lecon lecon = leconRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leçon introuvable"));
        return leconMapper.toDto(lecon);
    }

    @Override
    public List<Lecon> getAll() {
        return leconRepository.findAll();
    }

    @Override
    public LeconDTO update(Long id, LeconDTO dto) {
        Lecon existing = leconRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leçon non trouvée"));

        existing.setNomLecon(dto.nomLecon());
        existing.setNomFichierPdf(dto.nomFichierPdf());

        Chapitre chapitre = chapitreRepository.findById(dto.chapitreId())
                .orElseThrow(() -> new RuntimeException("Chapitre non trouvé"));
        existing.setChapitre(chapitre);

        return leconMapper.toDto(leconRepository.save(existing));
    }

    @Override
    public LeconDTO create(LeconDTO leconDTO) {
        Chapitre chapitre = chapitreRepository.findById(leconDTO.chapitreId())
                .orElseThrow(() -> new RuntimeException("Chapitre non trouvé"));

        Lecon lecon = leconMapper.toEntity(leconDTO);
        lecon.setChapitre(chapitre);

        Lecon savedLecon = leconRepository.save(lecon);
        return leconMapper.toDto(savedLecon);
    }

    @Override
    public void delete(Long id) {
        if (!leconRepository.existsById(id)) {
            throw new RuntimeException("Leçon non trouvée");
        }
        leconRepository.deleteById(id);
    }
    @Override
    @Transactional
    public List<Lecon> getLeconsByClasse(String classe) {
        if (classe == null || classe.isBlank()) {
            throw new IllegalArgumentException("La classe ne peut pas être vide");
        }
        try {
            Classe.fromLabel(classe);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Classe invalide : " + classe);
        }
        return leconRepository.findByChapitreClasse(Classe.fromLabel(classe));
    }

}
