package com.mathtonsavoir.backend.model;

import com.mathtonsavoir.backend.model.enums.Classe;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Chapitre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idChapitre;

    @Column(nullable = false)
    private String nomChapitre;

    private Classe classe;
}
