package com.mathtonsavoir.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReponseExerciceDTO(
        Long id,
        String utilisateur,
        String exercice,
        String reponseDonnee,
        LocalDateTime dateReponse,
        Boolean estJuste,
        BigDecimal note
) {}
