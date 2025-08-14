package com.mathtonsavoir.backend.dto;

import com.mathtonsavoir.backend.model.Lecon;

public record LeconDTO(
        Long idLecon,
        String nomLecon,
        String nomFichierPdf,
        Long chapitreId,
        String niveau,
        String description,
        String pdfUrl
) {
    public static LeconDTO fromEntity(Lecon lecon) {
        if (lecon == null) return null;

        String niveauLabel = null;
        String description = null;
        Long chapitreId = null;

        if (lecon.getChapitre() != null) {
            chapitreId = lecon.getChapitre().getIdChapitre();
            if (lecon.getChapitre().getClasse() != null) {
                niveauLabel = lecon.getChapitre().getClasse().getNomClasse();
            }
            description = lecon.getChapitre().getNomChapitre();
        }

        String pdfUrlComputed = lecon.getNomFichierPdf() != null
                ? "/api/lecons/pdf/" + lecon.getNomFichierPdf()
                : null;

        return new LeconDTO(
                lecon.getIdLecon(),
                lecon.getNomLecon(),
                lecon.getNomFichierPdf(),
                chapitreId,
                niveauLabel,
                description,
                pdfUrlComputed
        );
    }
}
