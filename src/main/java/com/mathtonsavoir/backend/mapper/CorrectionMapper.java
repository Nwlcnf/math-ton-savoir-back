package com.mathtonsavoir.backend.mapper;

import com.mathtonsavoir.backend.dto.CorrectionDTO;
import com.mathtonsavoir.backend.model.Correction;

public class CorrectionMapper {

    public static CorrectionDTO toDto(Correction correction) {
        return new CorrectionDTO(
                correction.getReponse().getIdReponse(),
                correction.getCorrecteur().getPseudo(),
                correction.getCommentaire(),
                correction.getDateCorrection()
        );
    }
}
