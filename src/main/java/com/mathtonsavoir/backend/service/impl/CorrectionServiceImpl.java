package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.CorrectionDTO;
import com.mathtonsavoir.backend.mapper.CorrectionMapper;
import com.mathtonsavoir.backend.model.Correction;
import com.mathtonsavoir.backend.model.ReponseExercice;
import com.mathtonsavoir.backend.model.Utilisateur;
import com.mathtonsavoir.backend.repository.CorrectionRepository;
import com.mathtonsavoir.backend.repository.ReponseExerciceRepository;
import com.mathtonsavoir.backend.repository.UtilisateurRepository;
import com.mathtonsavoir.backend.service.CorrectionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CorrectionServiceImpl implements CorrectionService {

    private final CorrectionRepository correctionRepository;
    private final ReponseExerciceRepository reponseExerciceRepository;
    private final UtilisateurRepository utilisateurRepository;

    @Override
    public CorrectionDTO getById(Long id) {
        Correction correction = correctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Correction introuvable"));
        return CorrectionMapper.toDto(correction);
    }

    @Override
    public List<CorrectionDTO> getAll() {
        return correctionRepository.findAll()
                .stream()
                .map(CorrectionMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public CorrectionDTO create(CorrectionDTO dto) {
        ReponseExercice reponse = reponseExerciceRepository.findById(dto.reponseId())
                .orElseThrow(() -> new RuntimeException("Réponse introuvable"));

        Utilisateur correcteur = utilisateurRepository.findByPseudo(dto.correcteur())
                .orElseThrow(() -> new RuntimeException("Utilisateur (correcteur) introuvable"));

        Correction correction = Correction.builder()
                .reponse(reponse)
                .correcteur(correcteur)
                .commentaire(dto.commentaire())
                .dateCorrection(dto.dateCorrection() != null ? dto.dateCorrection() : LocalDateTime.now())
                .build();

        Correction saved = correctionRepository.save(correction);
        return CorrectionMapper.toDto(saved);
    }

    @Override
    public CorrectionDTO update(Long id, CorrectionDTO dto) {
        Correction correction = correctionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Correction introuvable"));

        ReponseExercice reponse = reponseExerciceRepository.findById(dto.reponseId())
                .orElseThrow(() -> new RuntimeException("Réponse introuvable"));

        Utilisateur correcteur = utilisateurRepository.findByPseudo(dto.correcteur())
                .orElseThrow(() -> new RuntimeException("Utilisateur (correcteur) introuvable"));

        correction.setReponse(reponse);
        correction.setCorrecteur(correcteur);
        correction.setCommentaire(dto.commentaire());
        correction.setDateCorrection(dto.dateCorrection() != null ? dto.dateCorrection() : correction.getDateCorrection());

        Correction updated = correctionRepository.save(correction);
        return CorrectionMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        correctionRepository.deleteById(id);
    }
}
