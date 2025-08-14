package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.LeconDTO;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.Lecon;
import com.mathtonsavoir.backend.repository.ChapitreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LeconMapper {

    private final ChapitreRepository chapitreRepository;

    public Lecon toEntity(LeconDTO dto) {
        Lecon lecon = new Lecon();
        lecon.setNomLecon(dto.nomLecon());
        lecon.setNomFichierPdf(dto.nomFichierPdf());

        Chapitre chapitre = chapitreRepository.findById(dto.chapitreId())
                .orElseThrow(() -> new RuntimeException("Chapitre non trouvé"));
        lecon.setChapitre(chapitre);

        return lecon;
    }

    public LeconDTO toDto(Lecon lecon) {
        return LeconDTO.fromEntity(lecon);
    }
}