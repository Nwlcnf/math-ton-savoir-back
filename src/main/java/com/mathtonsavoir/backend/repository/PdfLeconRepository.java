package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.PdfLecon;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PdfLeconRepository extends JpaRepository<PdfLecon, Long> {
    Optional<PdfLecon> findByNomPdf(String nomPdf);
    Optional<PdfLecon> findByLecon_IdLecon(Long leconId);
}

