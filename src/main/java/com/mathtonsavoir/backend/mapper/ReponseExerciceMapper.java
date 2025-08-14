package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.ReponseExerciceDTO;
import com.mathtonsavoir.backend.model.ReponseExercice;

public class ReponseExerciceMapper {

    public static ReponseExerciceDTO toDto(ReponseExercice reponse) {
        return new ReponseExerciceDTO(
                reponse.getIdReponse(),
                reponse.getUtilisateur().getPseudo(),
                reponse.getExercice().getEnonceExercice(),
                reponse.getReponseDonnee(),
                reponse.getDateReponse(),
                reponse.getEstJuste(),
                reponse.getNote()
        );
    }
}
