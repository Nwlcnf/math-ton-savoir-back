package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.Exercice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciceRepository extends JpaRepository<Exercice, Long> {
    List<Exercice> findByLecon_IdLecon(Long idLecon);
}
