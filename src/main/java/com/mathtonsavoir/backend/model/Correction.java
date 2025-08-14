package com.mathtonsavoir.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Correction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCorrection;

    @ManyToOne
    @JoinColumn(name = "reponse_id", nullable = false)
    private ReponseExercice reponse;

    @ManyToOne
    @JoinColumn(name = "correcteur_id", nullable = false)
    private Utilisateur correcteur;

    @Lob
    private String commentaire;

    private LocalDateTime dateCorrection;
}
