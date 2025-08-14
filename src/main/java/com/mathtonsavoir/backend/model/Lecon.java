package com.mathtonsavoir.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Lecon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idLecon;

    @Column(nullable = false)
    private String nomLecon;

    @Column(nullable = false)
    private String nomFichierPdf;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "chapitre_id", nullable = false)
    @JsonIgnoreProperties("lecons")
    private Chapitre chapitre;
}

