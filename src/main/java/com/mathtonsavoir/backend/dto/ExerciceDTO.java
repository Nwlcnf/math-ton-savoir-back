package com.mathtonsavoir.backend.dto;

public record ExerciceDTO(
        String type,
        String enonce,
        String correctionAuto,
        boolean estAutoCorrecte,
        String difficulte,
        Long idLecon
) {}
