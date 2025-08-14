package com.mathtonsavoir.backend.service.impl;

import com.mathtonsavoir.backend.dto.ExerciceDTO;
import com.mathtonsavoir.backend.mapper.ExerciceMapper;
import com.mathtonsavoir.backend.model.Exercice;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.repository.ExerciceRepository;
import com.mathtonsavoir.backend.repository.LeconRepository;
import com.mathtonsavoir.backend.service.ExerciceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExerciceServiceImpl implements ExerciceService {

    private final ExerciceRepository exerciceRepository;
    private final LeconRepository leconRepository;

    @Override
    public List<Exercice> getAll() {
        return exerciceRepository.findAll();
    }

    @Override
    public List<ExerciceDTO> getAllAsDto() {
        return exerciceRepository.findAll()
                .stream()
                .peek(e -> e.getLecon().getNomLecon()) // Force le chargement du nom de la leçon
                .map(ExerciceMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ExerciceDTO getById(Long id) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercice introuvable"));

        exercice.getLecon().getNomLecon(); // Force chargement de la leçon avant mapping

        return ExerciceMapper.toDto(exercice);
    }

    @Override
    public ExerciceDTO create(ExerciceDTO dto) {
        Lecon lecon = leconRepository.findById(dto.idLecon())
                .orElseThrow(() -> new RuntimeException("Leçon introuvable"));

        Exercice exercice = Exercice.builder()
                .typeExercice(dto.type())
                .enonceExercice(dto.enonce())
                .correctionAuto(dto.correctionAuto())
                .estAutoCorrecte(dto.estAutoCorrecte())
                .difficulte(dto.difficulte())
                .lecon(lecon)
                .build();

        return ExerciceMapper.toDto(exerciceRepository.save(exercice));
    }


    @Override
    public ExerciceDTO update(Long id, ExerciceDTO exerciceDTO) {
        Exercice exercice = exerciceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Exercice introuvable"));

        Lecon lecon = leconRepository.findById(exerciceDTO.idLecon())
                .orElseThrow(() -> new RuntimeException("Leçon introuvable"));

        lecon.getNomLecon(); // Force le chargement

        exercice.setTypeExercice(exerciceDTO.type());
        exercice.setEnonceExercice(exerciceDTO.enonce());
        exercice.setCorrectionAuto(exerciceDTO.correctionAuto());
        exercice.setEstAutoCorrecte(exerciceDTO.estAutoCorrecte());
        exercice.setDifficulte(exerciceDTO.difficulte());
        exercice.setLecon(lecon);

        Exercice updated = exerciceRepository.save(exercice);
        return ExerciceMapper.toDto(updated);
    }

    @Override
    public void delete(Long id) {
        exerciceRepository.deleteById(id);
    }
}
