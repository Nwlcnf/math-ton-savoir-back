package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.NiveauScolaire;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NiveauScolaireRepository extends JpaRepository<NiveauScolaire, Long> {
    Optional<NiveauScolaire> findByNomNiveau(String nomNiveau);
}
