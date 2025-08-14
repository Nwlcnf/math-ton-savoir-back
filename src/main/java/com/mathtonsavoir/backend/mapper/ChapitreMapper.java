package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.ChapitreDTO;
import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.enums.Classe;

public class ChapitreMapper {

    public static ChapitreDTO toDto(Chapitre chapitre) {
        return new ChapitreDTO(
                chapitre.getNomChapitre(),
                chapitre.getClasse().getNomClasse()
        );
    }
    public static Chapitre toEntity(ChapitreDTO chapitreDTO) {
        Chapitre chapitre = new Chapitre();
        chapitre.setNomChapitre(chapitreDTO.nomChapitre());
        chapitre.setClasse(Classe.fromLabel(chapitreDTO.niveau()));
        return chapitre;
    }


}
