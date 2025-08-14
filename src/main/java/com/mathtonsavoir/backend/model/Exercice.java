package com.mathtonsavoir.backend.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exercice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExercice;

    @Column(nullable = false)
    private String typeExercice;

    @Lob
    @Column(nullable = false)
    private String enonceExercice;

    @Lob
    private String correctionAuto;

    private String difficulte;

    @Column(nullable = false)
    private boolean estAutoCorrecte;

    @ManyToOne
    @JoinColumn(name = "lecon_id", nullable = false)
    private Lecon lecon;
}

