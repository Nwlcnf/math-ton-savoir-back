package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.Correction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CorrectionRepository extends JpaRepository<Correction, Long> {
    List<Correction> findByReponse_IdReponse(Long idReponse);
}
