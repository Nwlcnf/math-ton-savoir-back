package com.mathtonsavoir.backend.dto;

import java.time.LocalDateTime;

public record CorrectionDTO(
        Long reponseId,
        String correcteur,
        String commentaire,
        LocalDateTime dateCorrection
) {}
