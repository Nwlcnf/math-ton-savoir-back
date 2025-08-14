package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.Eleve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EleveRepository extends JpaRepository<Eleve, Long> {
}
