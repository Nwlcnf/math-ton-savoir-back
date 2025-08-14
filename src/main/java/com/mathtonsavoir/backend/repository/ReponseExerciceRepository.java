package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.ReponseExercice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReponseExerciceRepository extends JpaRepository<ReponseExercice, Long> {
    List<ReponseExercice> findByUtilisateur_Id(Long idUser);
    List<ReponseExercice> findByExercice_IdExercice(Long idExercice);
}
