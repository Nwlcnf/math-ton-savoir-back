package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.Eleve;
import com.mathtonsavoir.backend.model.Enseignant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnseignantRepository extends JpaRepository<Enseignant, Long> {
}
