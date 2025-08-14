package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.ExerciceDTO;
import com.mathtonsavoir.backend.model.Exercice;

public class ExerciceMapper {

    public static ExerciceDTO toDto(Exercice exercice) {
        return new ExerciceDTO(
                exercice.getTypeExercice(),
                exercice.getEnonceExercice(),
                exercice.getCorrectionAuto(),
                exercice.isEstAutoCorrecte(),
                exercice.getDifficulte(),
                exercice.getLecon().getIdLecon()
        );
    }
}
