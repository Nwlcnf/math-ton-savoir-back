package com.mathtonsavoir.backend.repository;

import com.mathtonsavoir.backend.model.Chapitre;
import com.mathtonsavoir.backend.model.enums.Classe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChapitreRepository extends JpaRepository<Chapitre, Long> {
    List<Chapitre> findByClasse(Classe classe);
    Optional<Chapitre> findByIdChapitre(Long idChapitre);


}
